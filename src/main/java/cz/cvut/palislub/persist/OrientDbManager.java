package cz.cvut.palislub.persist;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import cz.cvut.palislub.entity.CustomNode;
import cz.cvut.palislub.entity.CustomRelationship;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Lubos Palisek
 * Date: 17. 2. 2015
 */
public class OrientDbManager {

	@Autowired
	AnnotationResolver annotationResolver;

	@Autowired
	OrientDbSchemaChecker orientDbSchemaChecker;

	@Autowired
	OrientGraphFactory factory;


	public OrientGraph getTxGraph() {
		return factory.getTx();
	}

	/**
	 * Vytvori uzel v databazi
	 */
	public OrientVertex createNode(CustomNode node) {
		OrientGraph graph = factory.getTx();
		OrientVertex vertex = null;
		try {
			vertex = getNode(graph, node.getLabel() + "." + node.getUniqueKey(), node.getProperty(node.getUniqueKey()));
			if (vertex == null) {
				vertex = graph.addVertex("class:" + node.getLabel(), node.getProperties());
			} else {
				vertex.setProperties(node.getProperties());
				vertex.save();
			}
			graph.commit();
		} catch (Exception e) {
			System.out.println("Nepodarilo se ulozit uzel " + vertex + " do databaze.");
			graph.rollback();
			e.printStackTrace();
		} finally {
			graph.shutdown();
			return vertex;
		}
	}

	/**
	 * Vytvori hranu v databazi
	 */
	public OrientEdge createRelationship(CustomRelationship relationship) {
		OrientGraph graph = factory.getTx();
		OrientEdge edge = null;
		try {
			OrientVertex vertextFrom = getNode(graph, relationship.getNodeLabelFrom(), relationship.getNodeValueFrom());
			OrientVertex vertexTo = getNode(graph, relationship.getNodeLabelTo(), relationship.getNodeValueTo());

			if (vertextFrom == null || vertexTo == null) {
				throw new NullPointerException("Pri ukladani hrany nastala chyba: Vychozi uzel (" + vertextFrom + ") nebo cilovy uzel (" + vertexTo + ") je null.");
			}

			if (relationship.isUnique()) {
				Iterable<Edge> outcomingEdges = vertextFrom.getEdges(Direction.OUT, relationship.getLabel());
				Iterable<Edge> incomingEdges = vertexTo.getEdges(Direction.IN, relationship.getLabel());
				for (Edge outEdge : outcomingEdges) {
					if (Iterables.contains(incomingEdges, outEdge)) {
						edge = (OrientEdge) outEdge;
						break;
					}
				}
			}

			if (edge == null) {
				edge = graph.addEdge(null, vertextFrom, vertexTo, relationship.getLabel());
			}

			for (String key : relationship.getProperties().keySet()) {
				edge.setProperty(key, relationship.getProperty(key));
			}

			edge.save();
			graph.commit();
		} catch (Exception e) {
			System.out.println("Nepodarilo se ulozit hranu " + edge + " do databaze.");
			graph.rollback();
			e.printStackTrace();
		} finally {
			graph.shutdown();
			return edge;
		}
	}


	public void batchCreateNodes(Iterable<CustomNode> nodes) {
		OrientGraph graph = factory.getTx();
		try {
			graph.declareIntent(new OIntentMassiveInsert());

			int cnt = 0;
			for (CustomNode node : nodes) {
				OrientVertex vertex = getNode(graph, node.getLabel() + "." + node.getUniqueKey(), node.getProperty(node.getUniqueKey()));
				if (vertex == null) {
					graph.addVertex("class:" + node.getLabel(), node.getProperties());
				} else {
					vertex.setProperties(node.getProperties());
					vertex.save();
				}
				cnt++;
				if (cnt == 1000) {
					// memory optimalization
					graph.commit();
					cnt = 0;
				}
			}
			graph.declareIntent(null);
			graph.commit();
		} catch (Exception e) {
			graph.rollback();
			e.printStackTrace();
		} finally {
			graph.shutdown();
		}
	}

	public void batchCreateRelationships(Iterable<CustomRelationship> relationships) {
		OrientGraph graph = factory.getTx();
		try {
			graph.declareIntent(new OIntentMassiveInsert());
			relationships.forEach(relationship -> {
				Edge edge = null;
				OrientVertex vertextFrom = getNode(graph, relationship.getNodeLabelFrom(), relationship.getNodeValueFrom());
				OrientVertex vertexTo = getNode(graph, relationship.getNodeLabelTo(), relationship.getNodeValueTo());

				if (vertextFrom == null || vertexTo == null) {
					throw new NullPointerException("Pri ukladani hrany nastala chyba: Vychozi uzel (" + vertextFrom + ") nebo cilovy uzel (" + vertexTo + ") je null.");
				}

				if (relationship.isUnique()) {
					Iterable<Edge> outcomingEdges = vertextFrom.getEdges(Direction.OUT, relationship.getLabel());
					Iterable<Edge> incomingEdges = vertexTo.getEdges(Direction.IN, relationship.getLabel());
					for (Edge outEdge : outcomingEdges) {
						if (Iterables.contains(incomingEdges, outEdge)) {
							edge = (OrientEdge) outEdge;
							break;
						}
					}
				}

				if (edge == null) {
					edge = graph.addEdge(null, vertextFrom, vertexTo, relationship.getLabel());
				}

				for (String key : relationship.getProperties().keySet()) {
					edge.setProperty(key, relationship.getProperty(key));
				}
			});
			graph.declareIntent(null);
			graph.commit();
		} catch (Exception e) {
			graph.rollback();
			e.printStackTrace();
		} finally {
			graph.shutdown();
		}
	}


	private OrientVertex getNode(OrientGraph graph, String key, Object value) {
		return (OrientVertex) graph.getVertexByKey(key, value);
	}

	public List<Object> getIdsOfVertexByProperty(Class type, String propertyName, Object value) {
		OrientGraph graph = factory.getTx();

		Iterable<Vertex> vertices = graph.getVertices(annotationResolver.getNodeName(type) + "." + propertyName, value);

		if (vertices == null) {
			return null;
		}

		List<Object> result = Lists.newArrayList(vertices).stream().map(vertex -> vertex.getProperty(annotationResolver.getUniquePropertyName(type))).collect(Collectors.toList());

		return result;
	}

	public long count(Class clazz) {
		OrientGraph graph = factory.getTx();
		long cnt;
		if (annotationResolver.isNodeEntity(clazz)) {
			cnt = graph.countVertices(annotationResolver.getNodeName(clazz));
		} else {
			cnt = graph.countEdges(annotationResolver.getRelationshipType(clazz));
		}
		graph.shutdown();
		return cnt;
	}

	public long countEdges() {
		OrientGraph graph = factory.getTx();
		long cnt = graph.countEdges();
		graph.shutdown();
		return cnt;
	}


	public long countVertices() {
		OrientGraph graph = factory.getTx();
		long cnt = graph.countVertices();
		graph.shutdown();
		return cnt;
	}

	public void removeAllNodes(Class clazz) {
		OrientGraph graph = factory.getTx();
		try {
			Iterator<Vertex> vertexIterator = graph.getVerticesOfClass(annotationResolver.getNodeName(clazz)).iterator();
			while (vertexIterator.hasNext()) {
				Vertex v = vertexIterator.next();
				v.remove();
			}
			graph.commit();
		} catch (Exception e) {
			graph.rollback();
		} finally {
			graph.shutdown();
		}
	}

	public void clearDatabase() {
		OrientGraph graph = factory.getTx();
		try {
			Iterator<Vertex> vertexIterator = graph.getVertices().iterator();
			while (vertexIterator.hasNext()) {
				Vertex v = vertexIterator.next();
				v.remove();
			}

			Iterator<Edge> edgeIterator = graph.getEdges().iterator();
			while (vertexIterator.hasNext()) {
				Edge e = edgeIterator.next();
				e.remove();
			}

			for (String key : graph.getIndexedKeys(Vertex.class, true)) {
				factory.getNoTx().dropIndex(key);
			}
			graph.commit();
		} catch (Exception e) {
			graph.rollback();
		} finally {
			graph.shutdown();
		}
	}

	public Iterable<Vertex> listVertices(String typename, String[] keys, Object[] values) {
		OrientGraph graph = factory.getTx();
		Iterable<Vertex> v = graph.getVertices(typename, keys, values);
		return v;
	}

	public Iterable<Vertex> listVertices(String typename) {
		OrientGraph graph = factory.getTx();
		Iterable<Vertex> v = graph.getVerticesOfClass(typename);
		return v;
	}

	public void delete(String typename, String fieldname, Object value) {
		OrientGraph graph = factory.getTx();
		try {
			Iterable<Vertex> verticesToDelete = graph.getVertices(typename + "." + fieldname, value);
			for (Vertex v : verticesToDelete) {
				v.remove();
			}
		} catch (Exception e) {
			graph.rollback();
		} finally {
			graph.shutdown();
		}
	}

	public Vertex getById(String typename, String fieldname, Object id) {
		OrientGraph graph = factory.getTx();
		Vertex v = null;
		try {
			v = graph.getVertexByKey(typename + "." + fieldname, id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return v;
		}
	}

	public void setProperty(Element element, String propertyName, Object propertyValue) {
		element.setProperty(propertyName, propertyValue);
	}

	public Object getProperty(Element element, String propertyName) {
		return element.getProperty(propertyName);
	}

	public OrientVertex getVertexByRid(String rid) {
		OrientGraph graph = factory.getTx();
		OrientVertex v = graph.getVertex(rid);
		return v;
	}

	public OrientEdge getEdgeByRid(String rid) {
		OrientGraph graph = factory.getTx();
		OrientEdge v = graph.getEdge(rid);
		return v;
	}
}

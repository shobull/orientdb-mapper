package cz.cvut.palislub.persist;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.*;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import cz.cvut.palislub.entity.CustomNode;
import cz.cvut.palislub.entity.CustomRelationship;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: L
 * Date: 17. 2. 2015
 */
public class OrientDbManager {

	@Autowired
	AnnotationResolver annotationResolver;

	@Autowired
	OrientDbSchemaChecker orientDbSchemaChecker;

	@Autowired
	OrientDbGraphFactory orientDbGraphFactory;

	public OrientGraph getGraph() {
		return orientDbGraphFactory.getGraphTx();
	}

	@PostConstruct
	public void checkSchema() {
		orientDbSchemaChecker.checkDbSchema();
	}

	public void createNode(CustomNode node) {
		try {
			Vertex vertex;

			if (nodeExists(node.getLabel() + "." + node.getUniqueKey(), node.getProperty(node.getUniqueKey()))) {
				vertex = getNode(node.getLabel() + "." + node.getUniqueKey(), node.getProperty(node.getUniqueKey()));
			} else {
				vertex = getGraph().addVertex("class:" + node.getLabel());
			}

			for (String key : node.getProperties().keySet()) {
				vertex.setProperty(key, node.getProperty(key));
			}

			getGraph().commit();
			System.out.println("UKLADAM DO DB UZEL: " + vertex);

		} catch (Exception e) {
			e.printStackTrace();
			getGraph().rollback();
			System.out.println("NEPODARILO SE ULOZIT UZEL DO DB");
		}

	}


	private boolean nodeExists(String key, Object value) {
		Iterator<Vertex> it = getGraph().getVertices(key, value).iterator();
		return it.hasNext();
	}

	private Vertex getNode(String key, Object value) {
		return getGraph().getVertexByKey(key, value);
	}


	public List<Object> getIdsOfVertexByProperty(Class type, String propertyName, Object value) {
		System.out.println("Hledam uzly na zaklade " + annotationResolver.getNodeName(type) + "." + propertyName + " a hodnoty " + value);
		Iterable<Vertex> vertices = getGraph().getVertices(annotationResolver.getNodeName(type) + "." + propertyName, value);

		if (vertices == null) {
			return null;
		}

		return Lists.newArrayList(vertices).stream().map(vertex -> vertex.getProperty(annotationResolver.getUniquePropertyName(type))).collect(Collectors.toList());
	}

	public void createRelationship(CustomRelationship relationship) {
		try {
			Vertex vertextFrom = getNode(relationship.getNodeLabelFrom(), relationship.getNodeValueFrom());
			Vertex vertexTo = getNode(relationship.getNodeLabelTo(), relationship.getNodeValueTo());

			if (vertextFrom == null || vertexTo == null) {
				throw new NullPointerException("Pri ukladani hrany nastala chyba: Vychozi uzel (" + vertextFrom + ") nebo cilovy uzel (" + vertexTo + ") je null.");
			}

			Iterable<Edge> outcomingEdges = vertextFrom.getEdges(Direction.OUT, relationship.getLabel());
			Iterable<Edge> incomingEdges = vertexTo.getEdges(Direction.IN, relationship.getLabel());

			Edge edge = null;

			if (relationship.isUnique()) {
				for (Edge outEdge : outcomingEdges) {
					if (Iterables.contains(incomingEdges, outEdge)) {
						edge = outEdge;
						break;
					}
				}
			}

			if (edge == null) {
				edge = getGraph().addEdge(null, vertextFrom, vertexTo, relationship.getLabel());
			}

			for (String key : relationship.getProperties().keySet()) {
				edge.setProperty(key, relationship.getProperty(key));
			}

			getGraph().commit();
			System.out.println("UKLADAM/UPRAVUJI DO DB HRANU: " + edge);

		} catch (Exception e) {
			getGraph().rollback();
			e.printStackTrace();
			System.out.println("NEPODARILO SE ULOZIT HRANU DO DB");
		}
	}

	public long count(Class clazz) {
		if (annotationResolver.isNodeEntity(clazz)) {
			return getGraph().countVertices(annotationResolver.getNodeName(clazz));
		} else {
			return getGraph().countEdges(annotationResolver.getRelationshipType(clazz));
		}
	}

	public long countEdges() {
		return getGraph().countEdges();
	}

	public long countVertices() {
		return getGraph().countVertices();
	}

	public void clearDatabase() {
		try {
			Iterator<Vertex> vertexIterator = getGraph().getVertices().iterator();
			while (vertexIterator.hasNext()) {
				Vertex v = vertexIterator.next();
				v.remove();
			}

			Iterator<Edge> edgeIterator = getGraph().getEdges().iterator();
			while (vertexIterator.hasNext()) {
				Edge e = edgeIterator.next();
				e.remove();
			}

			for (String key : getGraph().getIndexedKeys(Vertex.class, true)) {
				getGraph().dropIndex(key);
			}
			getGraph().commit();
		} catch (Exception e) {
			e.printStackTrace();
			getGraph().rollback();
		}
	}


	public Iterable<Vertex> listVertices(String typename) {
		return getGraph().getVerticesOfClass(typename);
	}

	public void delete(String typename, String fieldname, Object value) {
		try {
			Iterable<Vertex> verticesToDelete = getGraph().getVertices(typename + "." + fieldname, value);
			for (Vertex v : verticesToDelete) {
				v.remove();
			}
		} catch  (Exception e)  {
			getGraph().rollback();
			e.printStackTrace();
		}
	}

	public Vertex getById(String typename, String fieldname, Object id) {
		return getGraph().getVertexByKey(typename + "." + fieldname, id);
	}

}

package cz.cvut.palislub.persist;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import cz.cvut.palislub.entity.CustomNode;
import cz.cvut.palislub.entity.CustomRelationship;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
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

	@PostConstruct
	public void checkSchema() {
		orientDbSchemaChecker.checkDbSchema();
	}

	public OrientGraph getGraph() {
		return orientDbGraphFactory.getGraphTx();
	}

	public void createNode(CustomNode node) {
		Vertex vertex = null;
		try {
			vertex = getNode(node.getLabel() + "." + node.getUniqueKey(), node.getProperty(node.getUniqueKey()));
			if (vertex == null) {
				vertex = getGraph().addVertex("class:" + node.getLabel());
			}

			System.out.println("PRIPRAVUJI SE ULOZIT UZEL: " + vertex);

			for (String key : node.getProperties().keySet()) {
				vertex.setProperty(key, node.getProperty(key));
			}

			System.out.println("COMMIT DO DB UZEL: " + vertex);

		} catch (Exception e) {
			System.out.println("NEPODARILO SE ULOZIT UZEL DO DB " + vertex);
			throw e;
		}

	}

	private Vertex getNode(String key, Object value) {
		Vertex v = getGraph().getVertexByKey(key, value);
		return v;
	}


	public List<Object> getIdsOfVertexByProperty(Class type, String propertyName, Object value) {
		System.out.println("Hledam uzly na zaklade " + annotationResolver.getNodeName(type) + "." + propertyName + " a hodnoty " + value);
		Iterable<Vertex> vertices = getGraph().getVertices(annotationResolver.getNodeName(type) + "." + propertyName, value);

		if (vertices == null) {
			return null;
		}

		List<Object> result = Lists.newArrayList(vertices).stream().map(vertex -> vertex.getProperty(annotationResolver.getUniquePropertyName(type))).collect(Collectors.toList());

		return result;
	}

	public void createRelationship(CustomRelationship relationship) {
		try {
			System.out.println("AAAAA Jdu ukladat hranu " + relationship.getLabel());
			Vertex vertextFrom = getNode(relationship.getNodeLabelFrom(), relationship.getNodeValueFrom());
			System.out.println("AAAAA Hrana z " + vertextFrom);
			Vertex vertexTo = getNode(relationship.getNodeLabelTo(), relationship.getNodeValueTo());
			System.out.println("AAAAA Hrana do " + vertexTo);


			if (vertextFrom == null || vertexTo == null) {
				throw new NullPointerException("Pri ukladani hrany nastala chyba: Vychozi uzel (" + vertextFrom + ") nebo cilovy uzel (" + vertexTo + ") je null.");
			}

			Edge edge = null;

			if (relationship.isUnique()) {
				Iterable<Edge> outcomingEdges = vertextFrom.getEdges(Direction.OUT, relationship.getLabel());
				Iterable<Edge> incomingEdges = vertexTo.getEdges(Direction.IN, relationship.getLabel());
				for (Edge outEdge : outcomingEdges) {
					if (Iterables.contains(incomingEdges, outEdge)) {
						edge = outEdge;
						break;
					}
				}
			}

			if (edge == null) {
				System.out.println("AAAAA jdu vytvorit hranu");
				edge = getGraph().addEdge(null, vertextFrom, vertexTo, relationship.getLabel());
				System.out.println("AAAAA Vytvarim novou hranu " + edge);
			}

			for (String key : relationship.getProperties().keySet()) {
				edge.setProperty(key, relationship.getProperty(key));
			}

			System.out.println("COMMIT DO DB HRANU: " + edge);
		} catch (Exception e) {
			System.out.println("NEPODARILO SE ULOZIT HRANU DO DB");
			e.printStackTrace();
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
	}


	public Iterable<Vertex> listVertices(String typename) {
		return getGraph().getVerticesOfClass(typename);
	}

	public void delete(String typename, String fieldname, Object value) {
		Iterable<Vertex> verticesToDelete = getGraph().getVertices(typename + "." + fieldname, value);
		for (Vertex v : verticesToDelete) {
			v.remove();
		}
	}

	public Vertex getById(String typename, String fieldname, Object id) {
		Vertex v = getGraph().getVertexByKey(typename + "." + fieldname, id);
		return v;
	}

}

package cz.cvut.palislub.persist;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import cz.cvut.palislub.entity.CustomNode;
import cz.cvut.palislub.entity.CustomRelationship;
import cz.cvut.palislub.example.domain.User;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * User: L
 * Date: 17. 2. 2015
 */
public class OrientDbManager {

	@Autowired
	AnnotationResolver annotationResolver;

	@Autowired
	OrientDbSchemaChecker orientDbSchemaChecker;

	OrientGraphNoTx graph;

	@PostConstruct
	public void checkSchema() {
		orientDbSchemaChecker.checkDbSchema(this);
	}

	public void setGraph(OrientGraphNoTx graph) {
		this.graph = graph;
	}

	public void createNode(CustomNode node) {
		Vertex vertex;

		if (nodeExists(node.getLabel() + "." + node.getUniqueKey(), node.getProperty(node.getUniqueKey()))) {
			vertex = getNode(node.getLabel() + "." + node.getUniqueKey(), node.getProperty(node.getUniqueKey()));
		} else {
			vertex = graph.addVertex("class:" + node.getLabel());
		}

		for (String key : node.getProperties().keySet()) {
			vertex.setProperty(key, node.getProperty(key));
		}

		System.out.println("UKLADAM DO DB UZEL: " + vertex);
	}

	private boolean indexExists(String fullIndexname) {
		return graph.getIndexedKeys(Vertex.class, true).contains(fullIndexname);
	}

	private boolean nodeExists(String key, Object value) {
		Iterator<Vertex> it = graph.getVertices(key, value).iterator();
		return it.hasNext();
	}

	private Vertex getNode(String key, Object value) {
		return graph.getVertexByKey(key, value);
	}

	public void createRelationship(CustomRelationship relationship) {
		Vertex vertextFrom = getNode(relationship.getNodeLabelFrom(), relationship.getNodeValueFrom());
		Vertex vertexTo = getNode(relationship.getNodeLabelTo(), relationship.getNodeValueTo());

		Edge edge = graph.addEdge(null, vertextFrom, vertexTo, relationship.getLabel());
		for (String key : relationship.getProperties().keySet()) {
			edge.setProperty(key, relationship.getProperty(key));
		}

		System.out.println("UKLADAM DO DB HRANU: " + edge);
	}

	public long count(Class clazz) {
		if (annotationResolver.isNodeEntity(clazz)) {
			return graph.countVertices(annotationResolver.getNodeName(clazz));
		} else {
			return graph.countEdges(annotationResolver.getRelationshipType(clazz));
		}
	}

	public void load() {

		GremlinPipeline pipe = new GremlinPipeline();
		pipe.start(getNode("User.userId", 1)).out("buy").property("name");

		while (pipe.hasNext()) {
			System.out.println(pipe.next());
		}
	}

	public long countEdges() {
		return graph.countEdges();
	}

	public long countVertices() {
		return graph.countVertices();
	}

	public void clearDatabase() {
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
			graph.dropIndex(key);
		}
	}

	public OrientVertexType createVertexTypeIfNotExist(String typename) {
		if (graph.getVertexType(typename) == null) {
			System.out.println("Custom vertex type \"" + typename + "\" created.");
			return graph.createVertexType(typename);
		}
		return graph.getVertexType(typename);
	}

	public OProperty createVertexPropertyIfNotExist(String typename, String fieldname) {
		OrientVertexType customType = graph.getVertexType(typename);
		if (customType.getProperty(fieldname) == null) {
			System.out.println("Property \"" + fieldname + "\" for custom vertex type \"" + typename + "\" created.");
			return customType.createProperty(fieldname, OType.ANY);
		}
		return graph.getVertexType(typename).getProperty(fieldname);
	}

	public void createVertexPropertyKeyIndexIfNotExist(String typename, String fieldname) {
		if (!indexExists(typename + "." + fieldname)) {
			graph.createKeyIndex(fieldname, Vertex.class, new Parameter("type", "UNIQUE"), new Parameter("class", typename));
			System.out.println("Created index above property \"" + fieldname + "\" in custom vertex type \"" + typename + "\".");
		}
	}

	public void createVertexPropertyIndexIfNotExist(String typename, String fieldname) {
		if (!indexExists(typename + "." + fieldname)) {
			graph.createIndex(fieldname, Vertex.class, new Parameter("class", typename));
		}
	}

	public void removeVertexProperty(String typename, String fieldname) {
		graph.getVertexType(typename).dropProperty(fieldname);
		System.out.println("Deleting property \"" + fieldname + "\" from custom vertex type \"" + typename + "\".");
	}

	public OrientEdgeType createEdgeTypeIfNotExist(String typename) {
		if (graph.getEdgeType(typename) == null) {
			System.out.println("Custom edge type \"" + typename + "\" created.");
			return graph.createEdgeType(typename);
		}
		return graph.getEdgeType(typename);
	}

	public OProperty createEdgePropertyIfNotExist(String typename, String fieldname) {
		OrientEdgeType customType = graph.getEdgeType(typename);
		if (customType.getProperty(fieldname) == null) {
			System.out.println("Property \"" + fieldname + "\" for custom edge type \"" + typename + "\" created.");
			return customType.createProperty(fieldname, OType.ANY);
		}
		return graph.getEdgeType(typename).getProperty(fieldname);
	}
}
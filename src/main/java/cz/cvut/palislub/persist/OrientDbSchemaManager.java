package cz.cvut.palislub.persist;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

/**
 * Created by lubos on 21.3.2015.
 */
public class OrientDbSchemaManager {

	@Autowired
	private OrientGraphFactory orientGraphFactory;

	private boolean indexExists(String fullIndexname) {
		OrientGraphNoTx graph = orientGraphFactory.getNoTx();
		return graph.getIndexedKeys(Vertex.class, true).contains(fullIndexname);
	}

	public OrientVertexType createVertexTypeIfNotExist(String typename) {

		OrientGraphNoTx graph = orientGraphFactory.getNoTx();
		OrientVertexType vertexType;

		if (graph.getVertexType(typename) == null) {
			System.out.println("Custom vertex type \"" + typename + "\" created.");
			vertexType = graph.createVertexType(typename);
		} else {
			vertexType = graph.getVertexType(typename);
		}

		return vertexType;
	}

	public OProperty createVertexPropertyIfNotExist(String typename, Field field) {
		OrientGraphNoTx graph = orientGraphFactory.getNoTx();
		OProperty property;
		OrientVertexType customType = graph.getVertexType(typename);
		if (customType.getProperty(field.getName()) == null) {
			System.out.println("Property \"" + field.getName() + "\" for custom vertex type \"" + typename + "\" created.");
			OType type = OType.getTypeByClass(field.getType()) != null ? OType.getTypeByClass(field.getType()) : OType.ANY;
			property = customType.createProperty(field.getName(), type);
		} else {
			property = graph.getVertexType(typename).getProperty(field.getName());
		}

		return property;
	}

	public void createVertexPropertyKeyIndexIfNotExist(String typename, String fieldname) {
		OrientGraphNoTx graph = orientGraphFactory.getNoTx();
		if (!indexExists(typename + "." + fieldname)) {
			graph.createKeyIndex(fieldname, Vertex.class, new Parameter("type", "UNIQUE"), new Parameter("class", typename));
			System.out.println("Created index above property \"" + fieldname + "\" in custom vertex type \"" + typename + "\".");
		}

	}

	public void createVertexPropertyIndexIfNotExist(String typename, String fieldname) {
		OrientGraphNoTx graph = orientGraphFactory.getNoTx();
		if (!indexExists(typename + "." + fieldname)) {
			graph.createIndex(fieldname, Vertex.class, new Parameter("class", typename));
		}
	}

	public void removeVertexProperty(String typename, String fieldname) {
		OrientGraphNoTx graph = orientGraphFactory.getNoTx();
		System.out.println("Deleting property \"" + fieldname + "\" from custom vertex type \"" + typename + "\".");
		graph.getVertexType(typename).dropProperty(fieldname);
	}

	public OrientEdgeType createEdgeTypeIfNotExist(String typename) {
		OrientGraphNoTx graph = orientGraphFactory.getNoTx();
		OrientEdgeType edgeType;
		if (graph.getEdgeType(typename) == null) {
			System.out.println("Custom edge type \"" + typename + "\" created.");
			edgeType = graph.createEdgeType(typename);
		} else {
			edgeType = graph.getEdgeType(typename);
		}

		return edgeType;
	}

	public OProperty createEdgePropertyIfNotExist(String typename, Field field) {
		OrientGraphNoTx graph = orientGraphFactory.getNoTx();
		OProperty property;
		OrientEdgeType customType = graph.getEdgeType(typename);
		if (customType.getProperty(field.getName()) == null) {
			System.out.println("Property \"" + field.getName() + "\" for custom edge type \"" + typename + "\" created.");
			OType type = OType.getTypeByClass(field.getType()) != null ? OType.getTypeByClass(field.getType()) : OType.ANY;
			property = customType.createProperty(field.getName(), type);
		} else {
			property = graph.getEdgeType(typename).getProperty(field.getName());

		}

		return property;
	}

	public void removeEdgeProperty(String relationshipName, String name) {
		OrientGraphNoTx graph = orientGraphFactory.getNoTx();
		System.out.println("Deleting property \"" + name + "\" from custom edge type \"" + relationshipName + "\".");
		graph.getEdgeType(relationshipName).dropProperty(name);
	}


}

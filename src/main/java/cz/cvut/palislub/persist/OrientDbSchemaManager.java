package cz.cvut.palislub.persist;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

/**
 * Created by lubos on 21.3.2015.
 */
public class OrientDbSchemaManager {

	@Autowired
	OrientDbGraphFactory orientDbGraphFactory;

	public OrientGraphNoTx getGraph() {
		return orientDbGraphFactory.getGraphNoTx();
	}


	private boolean indexExists(String fullIndexname) {
		return getGraph().getIndexedKeys(Vertex.class, true).contains(fullIndexname);
	}

	public OrientVertexType createVertexTypeIfNotExist(String typename) {
		OrientVertexType vertexType;

		if (getGraph().getVertexType(typename) == null) {
			System.out.println("Custom vertex type \"" + typename + "\" created.");
			vertexType = getGraph().createVertexType(typename);
		} else {
			vertexType = getGraph().getVertexType(typename);
		}

		return vertexType;
	}

	public OProperty createVertexPropertyIfNotExist(String typename, Field field) {
		OProperty property;
		OrientVertexType customType = getGraph().getVertexType(typename);
		if (customType.getProperty(field.getName()) == null) {
			System.out.println("Property \"" + field.getName() + "\" for custom vertex type \"" + typename + "\" created.");
			OType type = OType.getTypeByClass(field.getType()) != null ? OType.getTypeByClass(field.getType()) : OType.ANY;
			property = customType.createProperty(field.getName(), type);
		} else {
			property = getGraph().getVertexType(typename).getProperty(field.getName());
		}

		return property;
	}

	public void createVertexPropertyKeyIndexIfNotExist(String typename, String fieldname) {
		if (!indexExists(typename + "." + fieldname)) {
			getGraph().createKeyIndex(fieldname, Vertex.class, new Parameter("type", "UNIQUE"), new Parameter("class", typename));
			System.out.println("Created index above property \"" + fieldname + "\" in custom vertex type \"" + typename + "\".");
		}

	}

	public void createVertexPropertyIndexIfNotExist(String typename, String fieldname) {
		if (!indexExists(typename + "." + fieldname)) {
			getGraph().createIndex(fieldname, Vertex.class, new Parameter("class", typename));
		}
	}

	public void removeVertexProperty(String typename, String fieldname) {
		System.out.println("Deleting property \"" + fieldname + "\" from custom vertex type \"" + typename + "\".");
		getGraph().getVertexType(typename).dropProperty(fieldname);
	}

	public OrientEdgeType createEdgeTypeIfNotExist(String typename) {
		OrientEdgeType edgeType;
		if (getGraph().getEdgeType(typename) == null) {
			System.out.println("Custom edge type \"" + typename + "\" created.");
			edgeType = getGraph().createEdgeType(typename);
		} else {
			edgeType = getGraph().getEdgeType(typename);
		}

		return edgeType;
	}

	public OProperty createEdgePropertyIfNotExist(String typename, Field field) {
		OProperty property;
		OrientEdgeType customType = getGraph().getEdgeType(typename);
		if (customType.getProperty(field.getName()) == null) {
			System.out.println("Property \"" + field.getName() + "\" for custom edge type \"" + typename + "\" created.");
			OType type = OType.getTypeByClass(field.getType()) != null ? OType.getTypeByClass(field.getType()) : OType.ANY;
			property = customType.createProperty(field.getName(), type);
		} else {
			property = getGraph().getEdgeType(typename).getProperty(field.getName());

		}

		return property;
	}

	public void removeEdgeProperty(String relationshipName, String name) {
		System.out.println("Deleting property \"" + name + "\" from custom edge type \"" + relationshipName + "\".");
		getGraph().getEdgeType(relationshipName).dropProperty(name);
	}


}

package cz.cvut.palislub.persist;

import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import cz.cvut.palislub.entity.CustomNode;
import cz.cvut.palislub.entity.CustomRelationship;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: L
 * Date: 16. 2. 2015
 */
public class OrientDbPersister {

	@Autowired
	AnnotationResolver annotationResolver;

	@Autowired
	private OrientDbManager graphManager;

	@Autowired
	private OrientDbConvertor convertor;

	public Object save(Object entity) {
		if (isNodeEntity(entity.getClass())) {
			CustomNode customNode = convertor.transformToCustomNode(entity);
			graphManager.createNode(customNode);
			return null;
		} else if (isRelationshopEntity(entity.getClass())) {
			saveRelationship(entity);
			return null;
		}
		throw new IllegalArgumentException("Trida musi obsahovat anotaci @Node nebo @Relationship.");
	}

	public void saveRelationship(Object entity) {
		if (annotationResolver.isRelationshopEntity(entity.getClass())) {
			CustomRelationship customRelationship = convertor.transformToCustomRelationship(entity);
			graphManager.createRelationship(customRelationship);
		}
	}

	public boolean isNodeEntity(Class<?> type) {
		return annotationResolver.isNodeEntity(type);
	}

	private boolean isRelationshopEntity(Class<?> type) {
		return annotationResolver.isRelationshopEntity(type);
	}

	public List listVertexIds(Class<?> type) {
		if (!annotationResolver.isNodeEntity(type)) {
			throw new IllegalArgumentException("Trida musi obsahovat anotaci @Node.");
		}
		Iterable<Vertex> vertices = graphManager.listVertices(annotationResolver.getNodeName(type));

		List ids = Lists.newArrayList();
		for (Vertex v : vertices) {
			ids.add(v.getProperty(annotationResolver.getUniquePropertyName(type)));
		}

		return ids;
	}

	public Iterable<Vertex> listVertices(Class<?> type) {
		if (!annotationResolver.isNodeEntity(type)) {
			throw new IllegalArgumentException("Trida musi obsahovat anotaci @Node.");
		}
		return graphManager.listVertices(annotationResolver.getNodeName(type));
	}

	public void delete(Class clazz, Object id) {
		String classname = annotationResolver.getNodeName(clazz);
		String property = annotationResolver.getUniquePropertyName(clazz);
		graphManager.delete(classname, property, id);
	}


	public List getIdsOfVertexByProperty(Class<?> type, String propertyName, Object value) {
		if (!annotationResolver.hasProperty(type, propertyName)) {
			throw new IllegalArgumentException("Trida " + type.getName() + " nema promennou " + propertyName + " anotovanou jako @NodeProperty.");
		}

		return graphManager.getIdsOfVertexByProperty(type, propertyName, value);
	}


	private static boolean set(Object object, String fieldName, Object fieldValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(object, fieldValue);
				return true;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return false;
	}

	public Object get(Class<?> clazz, Object id) {
		Vertex vertex = getVertex(clazz, id);
		if (vertex == null) {
			return null;
		}

		Object newInstance = null;
		try {
			newInstance = clazz.newInstance();

			for (Field f : clazz.getDeclaredFields()) {
				if (annotationResolver.isNodeProperty(f)) {
					set(newInstance, f.getName(), vertex.getProperty(f.getName()));
				}
			}

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return newInstance;
	}

	public Vertex getVertex(Class<?> clazz, Object id) {
		return graphManager.getById(annotationResolver.getNodeName(clazz), annotationResolver.getUniquePropertyName(clazz), id);
	}

	public long count(Class clazz) {
		if (isNodeEntity(clazz) || isRelationshopEntity(clazz)) {
			return graphManager.count(clazz);
		}
		throw new IllegalArgumentException("Trida musi obsahovat anotaci @Node nebo @Relationship.");
	}

	public long countEdges() {
		return graphManager.countEdges();
	}

	public long countVertices() {
		return graphManager.countVertices();
	}

	public void clearDatabase() {
		System.out.println("MAZU DATABAZI");
		graphManager.clearDatabase();
		System.out.println("V DB ZBYLO: " + countVertices() + " uzlu a " + countEdges() + " hran");
	}

	public Graph getGraph() {
		return graphManager.getGraph();
	}
}

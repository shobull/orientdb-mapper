package cz.cvut.palislub.persist;

import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import cz.cvut.palislub.entity.CustomNode;
import cz.cvut.palislub.entity.CustomRelationship;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: Lubos Palisek
 * Date: 16. 2. 2015
 */
public class OrientDbPersister {

	@Autowired
	AnnotationResolver annotationResolver;

	@Autowired
	private OrientDbManager graphManager;

	@Autowired
	private OrientDbConvertor convertor;

	public OrientGraph getTxGraph() {
		return graphManager.getTxGraph();
	}

	public Object save(Object entity) {
		if (isNodeEntity(entity.getClass())) {
			CustomNode customNode = convertor.transformToCustomNode(entity);
			OrientVertex v = saveNode(customNode);
			return getInstance(v, entity.getClass());
		} else if (isRelationshopEntity(entity.getClass())) {
			CustomRelationship customRelationship = convertor.transformToCustomRelationship(entity);
			OrientEdge e = saveRelationship(customRelationship);
			return getInstance(e, entity.getClass());
		}
		throw new IllegalArgumentException("Trida musi obsahovat anotaci @Node nebo @Relationship.");
	}

	public void saveAll(Iterable<Object> entities) {
		if (!entities.iterator().hasNext()) {
			return;
		}

		Object o = entities.iterator().next();

		if (isNodeEntity(o.getClass())) {
			Iterable<CustomNode> nodes = convertor.transformToCustomNodes(entities);
			graphManager.batchCreateNodes(nodes);
		} else if (isRelationshopEntity(o.getClass())) {
			Iterable<CustomRelationship> relationships = convertor.transformToCustomRelationships(entities);
			graphManager.batchCreateRelationships(relationships);
		}
	}

	private OrientVertex saveNode(CustomNode node) {
		return graphManager.createNode(node);
	}

	private OrientEdge saveRelationship(CustomRelationship relationship) {
		return graphManager.createRelationship(relationship);
	}

	private boolean isRelationshopEntity(Class<?> type) {
		return annotationResolver.isRelationshopEntity(type);
	}

	public boolean isNodeEntity(Class<?> type) {
		return annotationResolver.isNodeEntity(type);
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

	public <T> Iterable<Vertex> listVertices(Class<?> type, String[] keys, Object[] values) {
		if (!annotationResolver.isNodeEntity(type)) {
			throw new IllegalArgumentException("Trida musi obsahovat anotaci @Node.");
		}
		return graphManager.listVertices(annotationResolver.getNodeName(type), keys, values);
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

		return getInstance(vertex, clazz);
	}

	public Object getByRid(Class<?> clazz, String rid) {
		if (isNodeEntity(clazz)) {
			OrientVertex vertex = graphManager.getVertexByRid(rid);
			return getInstance(vertex, clazz);
		} else if (isRelationshopEntity(clazz)) {
			OrientEdge edge = graphManager.getEdgeByRid(rid);
			return getInstance(edge, clazz);
		}
		throw new IllegalArgumentException("Trida musi obsahovat anotaci @Node nebo @Relationship.");
	}

	public Vertex getVertex(Class<?> clazz, Object id) {
		return graphManager.getById(annotationResolver.getNodeName(clazz), annotationResolver.getUniquePropertyName(clazz), id);
	}

	private Object getInstance(Element element, Class clazz) {
		if (element == null) {
			return null;
		}

		try {
			Object newInstance = clazz.newInstance();

			for (Field f : clazz.getDeclaredFields()) {
				if (annotationResolver.isNodeProperty(f)) {
					set(newInstance, f.getName(), element.getProperty(f.getName()));
				} else if (annotationResolver.isRelationshopProperty(f)) {
					set(newInstance, f.getName(), element.getProperty(f.getName()));
				}
			}

			return newInstance;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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

	public void removeAllNodes(Class clazz) {
		System.out.println("Mazu z DB uzly " + annotationResolver.getNodeName(clazz));
		graphManager.removeAllNodes(clazz);
		System.out.println("V DB ZBYLO: " + count(clazz) + " uzlu.");
	}

	public void clearDatabase() {
		System.out.println("MAZU DATABAZI");
		graphManager.clearDatabase();
		System.out.println("V DB ZBYLO: " + countVertices() + " uzlu a " + countEdges() + " hran");
	}

	public void setProperty(Element element, String propertyName, Object propertyValue) {
		graphManager.setProperty(element, propertyName, propertyValue);
	}

	public Object getProperty(Element element, String propertyName) {
		return graphManager.getProperty(element, propertyName);
	}
}

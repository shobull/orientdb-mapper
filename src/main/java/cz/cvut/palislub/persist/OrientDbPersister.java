package cz.cvut.palislub.persist;

import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Vertex;
import cz.cvut.palislub.entity.CustomNode;
import cz.cvut.palislub.entity.CustomRelationship;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.beans.factory.annotation.Autowired;

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

	public long count(Class clazz) {
		if (isNodeEntity(clazz) || isRelationshopEntity(clazz)) {
			return graphManager.count(clazz);
		}
		throw new IllegalArgumentException("Trida musi obsahovat anotaci @Node nebo @Relationship.");
	}

	public long countVertices() {
		return graphManager.countVertices();
	}

	public long countEdges() {
		return graphManager.countEdges();
	}

	public boolean isNodeEntity(Class<?> type) {
		return annotationResolver.isNodeEntity(type);
	}

	private boolean isRelationshopEntity(Class<?> type) {
		return annotationResolver.isRelationshopEntity(type);
	}

	public List<Object> listVertices(Class<?> type) {
		if (!annotationResolver.isNodeEntity(type)) {
			throw new IllegalArgumentException("Trida musi obsahovat anotaci @Node.");
		}
		Iterable<Vertex> vertices = graphManager.listVertices(annotationResolver.getNodeName(type));

		List<Object> ids = Lists.newArrayList();
		for (Vertex v : vertices) {
			ids.add(v.getProperty(annotationResolver.getUniquePropertyName(type)));
		}

		return ids;
	}

	public void clearDatabase() {
		System.out.println("MAZU DATABAZI");
		graphManager.clearDatabase();
		System.out.println("V DB ZBYLO: " + countVertices() + " uzlu a " + countEdges() + " hran");
	}

	public void delete(Class clazz, Object id) {
		String classname = annotationResolver.getNodeName(clazz);
		String property = annotationResolver.getUniquePropertyName(clazz);
		graphManager.delete(classname, property, id);
	}
}

package cz.cvut.palislub.persist;

import cz.cvut.palislub.entity.CustomNode;
import cz.cvut.palislub.entity.CustomRelationship;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.beans.factory.annotation.Autowired;

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
			CustomRelationship customRelationship = convertor.transformToCustomRelationship(entity);
			graphManager.createRelationship(customRelationship);
			return null;
		}
		throw new IllegalArgumentException("Trida musi obsahovat anotaci @Node nebo @Relationship.");
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

	public void clearDatabase() {
		System.out.println("MAZU DATABAZI");
		graphManager.clearDatabase();
		System.out.println("V DB ZBYLO: " + countVertices() + " uzlu a " + countEdges() + " hran");
	}

	public void loadTest() {
		graphManager.load();
	}

}

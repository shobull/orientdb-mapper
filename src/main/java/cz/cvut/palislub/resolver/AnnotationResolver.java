package cz.cvut.palislub.resolver;

import cz.cvut.palislub.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * User: L
 * Date: 16. 2. 2015
 */
public class AnnotationResolver {

	public Class<? extends Annotation> nodeEntityAnnotation() {
		return Node.class;
	}

	public Class<? extends Annotation> nodePropertyEntityAnnotation() {
		return NodeProperty.class;
	}

	public Class<? extends Annotation> relationshipPropertyAnnotation() {
		return RelationshipProperty.class;
	}

	public Class<? extends Annotation> relationshipAnnotation() {
		return Relationship.class;
	}

	public boolean isNodeEntity(Class<?> type) {
		return type.isAnnotationPresent(Node.class);
	}

	public boolean isRelationshopEntity(Class<?> type) {
		return type.isAnnotationPresent(Relationship.class);
	}

	public boolean isNodeProperty(Field field) {
		return field.isAnnotationPresent(NodeProperty.class);
	}

	public String getNodeName(Class<?> type) {
		return type.getAnnotation(Node.class).name();
	}

	public boolean isRelationshipUnique(Class<?> type) {
		return type.getAnnotation(Relationship.class).unique();
	}

	public boolean isIndexed(Field field) {
		return field.isAnnotationPresent(Indexed.class);
	}

	public boolean isUnique(Field field) {
		return field.isAnnotationPresent(Unique.class);
	}

	public String getUniquePropertyName(Class<?> type) {
		for (Field f : type.getDeclaredFields()) {
			if (isUnique(f)) {
				return f.getName();
			}
		}
		return null;
	}

	public boolean hasProperty(Class<?> type, String propertyName) {
		for (Field f : type.getDeclaredFields()) {
			if (propertyName.equals(f.getName()) && f.isAnnotationPresent(NodeProperty.class)) {
				return true;
			}
		}
		return false;
	}

	public String getRelationshipType(Class<?> type) {
		return type.getAnnotation(Relationship.class).type();
	}

	public boolean isNodeFrom(Field field) {
		return field.isAnnotationPresent(NodeFrom.class);
	}

	public boolean isNodeTo(Field field) {
		return field.isAnnotationPresent(NodeTo.class);
	}


	public boolean isRelationshopProperty(Field field) {
		return field.isAnnotationPresent(RelationshipProperty.class);
	}
}

package cz.cvut.palislub.persist;

import cz.cvut.palislub.entity.CustomNode;
import cz.cvut.palislub.entity.CustomRelationship;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

/**
 * User: L
 * Date: 17. 2. 2015
 */
public class OrientDbConvertor {

	@Autowired
	AnnotationResolver annotationResolver;

	public CustomNode transformToCustomNode(Object entity) {
		CustomNode customNode = new CustomNode();

		String label = annotationResolver.getNodeName(entity.getClass());
		customNode.setLabel(label);

		try {
			for (Field f : entity.getClass().getDeclaredFields()) {
				if (annotationResolver.isNodeProperty(f)) {
					f.setAccessible(true);
					customNode.addProperty(f.getName(), f.get(entity));

					if (annotationResolver.isUnique(f) && customNode.getUniqueKey() == null) {
						customNode.setUniqueKey(f.getName());
					}

					if (annotationResolver.isIndexed(f)) {
						customNode.addIndex(f.getName());
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return customNode;
	}

	private String retrieveIndexedProperty(Object object) {
		for (Field f : object.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if (annotationResolver.isUnique(f)) {
				return f.getName();
			}
		}
		return null;
	}

	private Object retrieveFieldValue(Object entity, String fieldname) throws NoSuchFieldException, IllegalAccessException {
		Field field = entity.getClass().getDeclaredField(fieldname);
		field.setAccessible(true);
		return field.get(entity);
	}

	public CustomRelationship transformToCustomRelationship(Object entity) {
		CustomRelationship customRelationship = new CustomRelationship();

		String label = annotationResolver.getRelationshipType(entity.getClass());
		customRelationship.setLabel(label);

		try {
			for (Field f : entity.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if (annotationResolver.isNodeFrom(f)) {
					Object from = f.get(entity);
					String uniqueFieldName = retrieveIndexedProperty(from);
					String nodeLabelFrom = annotationResolver.getNodeName(from.getClass()) + "." + uniqueFieldName;
					Object nodeValueFrom = retrieveFieldValue(from, uniqueFieldName);

					customRelationship.setNodeLabelFrom(nodeLabelFrom);
					customRelationship.setNodeValueFrom(nodeValueFrom);
				} else if (annotationResolver.isNodeTo(f)) {
					Object to = f.get(entity);
					String uniqueFieldName = retrieveIndexedProperty(to);
					String nodeLabelFrom = annotationResolver.getNodeName(to.getClass()) + "." + uniqueFieldName;
					Object nodeValueFrom = retrieveFieldValue(to, uniqueFieldName);

					customRelationship.setNodeLabelTo(nodeLabelFrom);
					customRelationship.setNodeValueTo(nodeValueFrom);
				} else if (annotationResolver.isRelationshopProperty(f)) {
					customRelationship.addProperty(f.getName(), f.get(entity));
				}
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return customRelationship;
	}
}

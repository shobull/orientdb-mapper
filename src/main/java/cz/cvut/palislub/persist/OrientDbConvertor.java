package cz.cvut.palislub.persist;

import com.google.common.collect.Lists;
import cz.cvut.palislub.entity.CustomNode;
import cz.cvut.palislub.entity.CustomRelationship;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: Lubos Palisek
 * Date: 17. 2. 2015
 */
public class OrientDbConvertor {

	@Autowired
	AnnotationResolver annotationResolver;

	public void setAnnotationResolver(AnnotationResolver annotationResolver) {
		this.annotationResolver = annotationResolver;
	}

	public CustomNode transformToCustomNode(Object entity) {
		CustomNode customNode = new CustomNode();

		if (!annotationResolver.isNodeEntity(entity.getClass())) {
			throw new IllegalArgumentException("Parametr musi byt uzel.");
		}

		String label = annotationResolver.getNodeName(entity.getClass());
		customNode.setLabel(label);

		try {
			for (Field f : entity.getClass().getDeclaredFields()) {
				if (annotationResolver.isNodeProperty(f)) {
					f.setAccessible(true);
					if (f.get(entity) == null) {
						if (annotationResolver.isUnique(f)) {
							throw new IllegalArgumentException("Property " + f.getName() + " je anotovana @Unique a nesmi byt null.");
						}
						continue;
					}
					customNode.addProperty(f.getName(), f.get(entity));

					if (annotationResolver.isUnique(f)) {
						if (customNode.getUniqueKey() != null) {
							throw new IllegalArgumentException("Trida musi obsahovat pouze jednu anotaci @Unique.");
						}

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

	public Iterable<CustomNode> transformToCustomNodes(Iterable<Object> entities) {
		List<CustomNode> list = Lists.newArrayList();
		for (Object o : entities) {
			list.add(transformToCustomNode(o));
		}
		return list;
	}

	public CustomRelationship transformToCustomRelationship(Object entity) {
		CustomRelationship customRelationship = new CustomRelationship();

		if (!annotationResolver.isRelationshopEntity(entity.getClass())) {
			throw new IllegalArgumentException("Parametr musi byt hrana.");
		}

		String label = annotationResolver.getRelationshipType(entity.getClass());
		customRelationship.setLabel(label);

		boolean unique = annotationResolver.isRelationshipUnique(entity.getClass());
		customRelationship.setUnique(unique);

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

					if (f.get(entity) == null) {
						continue;
					}
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

	public Iterable<CustomRelationship> transformToCustomRelationships(Iterable<Object> entities) {
		List<CustomRelationship> list = Lists.newArrayList();
		for (Object o : entities) {
			list.add(transformToCustomRelationship(o));
		}
		return list;
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
}

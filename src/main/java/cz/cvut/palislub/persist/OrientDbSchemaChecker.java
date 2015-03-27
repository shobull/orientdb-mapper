package cz.cvut.palislub.persist;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;


/**
 * Created by lubos on 26.2.2015.
 */
public class OrientDbSchemaChecker {

	@Autowired
	AnnotationResolver annotationResolver;

	@Autowired
	private OrientDbSchemaManager orientDbSchemaManager;

	String scannedPackage;

	public void setScannedPackage(String scannedPackage) {
		this.scannedPackage = scannedPackage;
	}

	public void checkDbSchema() {
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage(scannedPackage))
				.setScanners(new FieldAnnotationsScanner(),
						new SubTypesScanner(),
						new TypeAnnotationsScanner()));

		checkVertexTypes(reflections);
		checkEdgeTypes(reflections);
	}

	private void checkEdgeTypes(Reflections reflections) {
		Set<Class<?>> relationships = reflections.getTypesAnnotatedWith(annotationResolver.relationshipAnnotation());
		Iterator<Class<?>> classIterator = relationships.iterator();
		while (classIterator.hasNext()) {
			Class<?> clazz = classIterator.next();
			String relationshipName = annotationResolver.getRelationshipType(clazz);

			OrientEdgeType orientEdgeType = orientDbSchemaManager.createEdgeTypeIfNotExist(relationshipName);
			Collection<String> existingProperties = orientEdgeType.propertiesMap().keySet();

			Set<Field> fields = getAllFields(clazz, withAnnotation(annotationResolver.relationshipPropertyAnnotation()));
			Iterator<Field> fieldIterator = fields.iterator();
			while (fieldIterator.hasNext()) {
				Field field = fieldIterator.next();
				OProperty createdProperty = orientDbSchemaManager.createEdgePropertyIfNotExist(annotationResolver.getRelationshipType(clazz), field);
				existingProperties.remove(createdProperty.getName());
			}

			for (String p : existingProperties) {
				orientDbSchemaManager.removeEdgeProperty(relationshipName, p);
			}
		}
	}

	private void checkVertexTypes(Reflections reflections) {
		Set<Class<?>> nodes = reflections.getTypesAnnotatedWith(annotationResolver.nodeEntityAnnotation());
		Iterator<Class<?>> classIterator = nodes.iterator();
		while (classIterator.hasNext()) {
			Class<?> clazz = classIterator.next();
			String nodename = annotationResolver.getNodeName(clazz);

			OrientVertexType orientVertexType = orientDbSchemaManager.createVertexTypeIfNotExist(nodename);
			Collection<String> existingProperties = orientVertexType.propertiesMap().keySet();

			Set<Field> fields = getAllFields(clazz, withAnnotation(annotationResolver.nodePropertyEntityAnnotation()));
			Iterator<Field> fieldIterator = fields.iterator();
			while (fieldIterator.hasNext()) {
				Field field = fieldIterator.next();
				if (annotationResolver.isUnique(field)) {
					orientDbSchemaManager.createVertexPropertyKeyIndexIfNotExist(nodename, field.getName());
				}

				if (annotationResolver.isIndexed(field)) {
					orientDbSchemaManager.createVertexPropertyIndexIfNotExist(nodename, field.getName());
				}

				OProperty createdProperty = orientDbSchemaManager.createVertexPropertyIfNotExist(nodename, field);
				existingProperties.remove(createdProperty.getName());
			}

			for (String p : existingProperties) {
				orientDbSchemaManager.removeVertexProperty(nodename, p);
			}
		}
	}
}

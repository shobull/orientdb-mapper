package cz.cvut.palislub.persist;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
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

	String scannedPackage;

	OrientDbManager graphManager;

	public void setScannedPackage(String scannedPackage) {
		this.scannedPackage = scannedPackage;
	}

	public void checkDbSchema(OrientDbManager graphManager) {
		this.graphManager = graphManager;

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage(scannedPackage))
				.setScanners(new FieldAnnotationsScanner(),
						new SubTypesScanner(),
						new TypeAnnotationsScanner()));

		checkVertexTypes(reflections);
		checkEdgeTypes(reflections);
		System.out.println("OrientDB schema check completed.");
	}

	private void checkEdgeTypes(Reflections reflections) {
		Set<Class<?>> relationships = reflections.getTypesAnnotatedWith(annotationResolver.relationshipAnnotation());
		Iterator<Class<?>> classIterator = relationships.iterator();
		while (classIterator.hasNext()) {
			Class<?> clazz = classIterator.next();
			String relationshipName = annotationResolver.getRelationshipType(clazz);

			OrientEdgeType orientEdgeType = graphManager.createEdgeTypeIfNotExist(relationshipName);
			Collection<OProperty> existingProperties = orientEdgeType.properties();

			Set<Field> fields = getAllFields(clazz, withAnnotation(annotationResolver.relationshipPropertyAnnotation()));
			Iterator<Field> fieldIterator = fields.iterator();
			while (fieldIterator.hasNext()) {
				Field field = fieldIterator.next();
				OProperty createdProperty = graphManager.createEdgePropertyIfNotExist(annotationResolver.getRelationshipType(clazz), field.getName());
				existingProperties.remove(createdProperty);
			}

			for (OProperty p : existingProperties) {
				graphManager.removeVertexProperty(relationshipName, p.getName());
			}
		}
	}

	private void checkVertexTypes(Reflections reflections) {
		Set<Class<?>> nodes = reflections.getTypesAnnotatedWith(annotationResolver.nodeEntityAnnotation());
		Iterator<Class<?>> classIterator = nodes.iterator();
		while (classIterator.hasNext()) {
			Class<?> clazz = classIterator.next();
			String nodename = annotationResolver.getNodeName(clazz);

			OrientVertexType orientVertexType = graphManager.createVertexTypeIfNotExist(nodename);
			Collection<OProperty> existingProperties = orientVertexType.properties();

			Set<Field> fields = getAllFields(clazz, withAnnotation(annotationResolver.nodePropertyEntityAnnotation()));
			Iterator<Field> fieldIterator = fields.iterator();
			while (fieldIterator.hasNext()) {
				Field field = fieldIterator.next();
				if (annotationResolver.isUnique(field)) {
					graphManager.createVertexPropertyKeyIndexIfNotExist(nodename, field.getName());
				}

				if (annotationResolver.isIndexed(field)) {
					graphManager.createVertexPropertyIndexIfNotExist(nodename, field.getName());
				}

				OProperty createdProperty = graphManager.createVertexPropertyIfNotExist(nodename, field.getName());
				existingProperties.remove(createdProperty);
			}

			for (OProperty p : existingProperties) {
				graphManager.removeVertexProperty(nodename, p.getName());
			}
		}
	}
}

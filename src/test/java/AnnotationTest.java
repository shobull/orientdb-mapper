import cz.cvut.palislub.resolver.AnnotationResolver;
import domain.GraphTestRelationship;
import domain.GraphTestUser;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: Lubos Palisekubos Palisek
 * Date: 13. 4. 2015
 */
public class AnnotationTest {

	AnnotationResolver resolver = new AnnotationResolver();


	Class nodeClazz = GraphTestUser.class;

	Class relationshipClazz = GraphTestRelationship.class;

	/**
	 * Node annotation resolver test
	 */
	@Test
	public void nodeAnnotationsTest() throws NoSuchFieldException {
		Assert.assertEquals("Wrong node name", "User", resolver.getNodeName(nodeClazz));

		Assert.assertTrue("Is not a node entity", resolver.isNodeEntity(nodeClazz));
		Assert.assertFalse("Is a relationship entity", resolver.isRelationshopEntity(nodeClazz));

		Assert.assertEquals("Wrong unique property", "userId", resolver.getUniquePropertyName(nodeClazz));

		Assert.assertTrue("Property doesn't exist", resolver.hasProperty(nodeClazz, "username"));
		Assert.assertFalse("Property exists", resolver.hasProperty(nodeClazz, "xxx"));

		Assert.assertTrue("Is not a node property", resolver.isNodeProperty(nodeClazz.getDeclaredField("username")));
		Assert.assertFalse("Is a node property", resolver.isNodeProperty(nodeClazz.getDeclaredField("age")));
	}

	/**
	 * Relationship annotation resolver test
	 */
	@Test
	public void relationshipAnnotationsTest() throws NoSuchFieldException {
		Assert.assertEquals("Wrong relationship name", "TestRelationship", resolver.getRelationshipType(relationshipClazz));

		Assert.assertTrue("Is not a relationship entity", resolver.isRelationshopEntity(relationshipClazz));
		Assert.assertFalse("Is a node entity", resolver.isNodeEntity(relationshipClazz));

		Assert.assertTrue("Not an unique relationship", resolver.isRelationshipUnique(relationshipClazz));

		Assert.assertTrue("Is not a relationship property", resolver.isRelationshopProperty(relationshipClazz.getDeclaredField("visitLength")));

		Assert.assertTrue("Is not a start node", resolver.isNodeFrom(relationshipClazz.getDeclaredField("user")));
		Assert.assertTrue("Is not an end node", resolver.isNodeTo(relationshipClazz.getDeclaredField("webpage")));
	}


}

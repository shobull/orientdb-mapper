import com.google.common.collect.Lists;
import cz.cvut.palislub.entity.CustomNode;
import cz.cvut.palislub.entity.CustomRelationship;
import cz.cvut.palislub.persist.OrientDbConvertor;
import cz.cvut.palislub.resolver.AnnotationResolver;
import domain.GraphTestRelationship;
import domain.GraphTestUser;
import domain.GraphTestWebPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * User: L
 * Date: 13. 4. 2015
 */
public class ConvertorTest {

	OrientDbConvertor convertor;


	@Before
	public void initConvertor() {
		convertor = new OrientDbConvertor();
		convertor.setAnnotationResolver(new AnnotationResolver());
	}

	/**
	 * Node convertor tests
	 */
	@Test
	public void convertNodeTest() {
		String userId = "1";
		String username = "lubos";

		GraphTestUser user = new GraphTestUser();

		user.setUserId(userId);
		user.setUsername(username);

		CustomNode customNode = convertor.transformToCustomNode(user);
		List<String> props = Lists.newArrayList(customNode.getProperties().keySet());

		Assert.assertEquals("Wrong properties parsed from node entity", props, Arrays.asList("userId", "username"));

		Assert.assertEquals("Wrong property parsed", customNode.getProperty("userId"), userId);
		Assert.assertEquals("Wrong property parsed", customNode.getProperty("username"), username);

		Assert.assertEquals("Indexed key find failed", customNode.getUniqueKey(), "userId");

		Assert.assertEquals("Wrong node label", customNode.getLabel(), "User");
	}

	/**
	 * Missing identifier test
	 */
	@Test(expected = IllegalArgumentException.class)
	public void wrongInputNodeConvertorTest() {
		GraphTestUser user = new GraphTestUser();
		convertor.transformToCustomNode(user);
	}

	/**
	 * Wrong parametr to transformToCustomNode() method test
	 */
	@Test(expected = IllegalArgumentException.class)
	public void wrongInputNodeConvertorTest2() {
		String userId = "1";
		String webPageId = "webPage1";

		GraphTestUser user = new GraphTestUser();
		user.setUserId(userId);
		GraphTestWebPage webPage = new GraphTestWebPage();
		webPage.setWebPageId(webPageId);
		GraphTestRelationship relationship = new GraphTestRelationship(user, webPage);

		convertor.transformToCustomNode(relationship);
	}

	/**
	 * Relationship convertor tests
	 */
	@Test
	public void convertRelationshipTest() {
		String userId = "1";
		String webPageId = "webPage1";

		GraphTestUser user = new GraphTestUser();
		user.setUserId(userId);
		GraphTestWebPage webPage = new GraphTestWebPage();
		webPage.setWebPageId(webPageId);
		GraphTestRelationship relationship = new GraphTestRelationship(user, webPage);

		CustomRelationship customRelationship = convertor.transformToCustomRelationship(relationship);
		List<String> props = Lists.newArrayList(customRelationship.getProperties().keySet());

		Assert.assertEquals("Wrong relationship label", customRelationship.getLabel(), "TestRelationship");

		Assert.assertTrue("Not an unique relationship", customRelationship.isUnique());

		Assert.assertEquals("Wrong starting node label", customRelationship.getNodeLabelFrom(), "User.userId");
		Assert.assertEquals("Wrong ending node label", customRelationship.getNodeLabelTo(), "WebPage.webPageId");

		Assert.assertEquals("Wrong properties parsed from relationship entity", props, Arrays.asList("visitLength"));
	}

	/**
	 * Missing start node indentifier test
	 */
	@Test(expected = IllegalArgumentException.class)
	public void wrongInputRelationshipConvertorTest2() {
		String webPageId = "webPage1";

		GraphTestUser user = new GraphTestUser();
		GraphTestWebPage webPage = new GraphTestWebPage();
		webPage.setWebPageId(webPageId);
		GraphTestRelationship relationship = new GraphTestRelationship(user, webPage);
		convertor.transformToCustomNode(relationship);
	}

	/**
	 * Missing end node indentifier
	 */
	@Test(expected = IllegalArgumentException.class)
	public void wrongInputRelationshipConvertorTest1() {
		String userId = "1";

		GraphTestUser user = new GraphTestUser();
		user.setUserId(userId);
		GraphTestWebPage webPage = new GraphTestWebPage();
		GraphTestRelationship relationship = new GraphTestRelationship(user, webPage);
		convertor.transformToCustomNode(relationship);
	}


}

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import config.TestConfig;
import cz.cvut.palislub.persist.OrientDbSchemaChecker;
import domain.GraphTestUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import repo.GraphTestUserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * User: Lubos Palisek
 * Date: 13. 4. 2015
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
public class GenericRepositoryTest {

	@Autowired
	OrientDbSchemaChecker orientDbSchemaManager;

	@Autowired
	GraphTestUserRepository userRepository;

	@Before
	public void beforeClass() {
		orientDbSchemaManager.checkDbSchema();
	}

	/**
	 * Loading entity from database by unique property test.
	 */
	@Test
	public void loadingByIdTest() {
		String userId = "1";
		String username = "lubos";

		OrientGraph graph = userRepository.getTxGraph();

		Map<String, String> params = Maps.newHashMap();
		params.put("userId", userId);
		params.put("username", username);

		graph.addVertex("class:User", params);
		graph.commit();

		GraphTestUser user = userRepository.get(userId);
		Assert.assertEquals("userId is not equal", user.getUserId(), userId);
		Assert.assertEquals("username is not equal", user.getUsername(), username);

		graph.shutdown();
	}

	/**
	 * Inserting node to database test
	 */
	@Test
	public void insertTest() {
		String userId = "1";
		String username = "lubos";

		GraphTestUser user = new GraphTestUser();
		user.setUserId(userId);
		user.setUsername(username);

		GraphTestUser loadedUser = userRepository.save(user);
		Assert.assertEquals("userId is not equal", loadedUser.getUserId(), userId);
		Assert.assertEquals("username is not equal", loadedUser.getUsername(), username);
	}


	@Test
	public void batchInsertTest() {
		String userId = "1";
		String username = "lubos";

		GraphTestUser user = new GraphTestUser();
		user.setUserId(userId);
		user.setUsername(username);

		String userId2 = "2";
		String username2 = "petr";

		GraphTestUser user2 = new GraphTestUser();
		user2.setUserId(userId2);
		user2.setUsername(username2);

		List<GraphTestUser> users = Arrays.asList(user, user2);

		userRepository.save(users);

		OrientGraph graph = userRepository.getTxGraph();
		Iterable<Vertex> usersIt = graph.getVerticesOfClass("User");

		List<Vertex> usersList = Lists.newArrayList(usersIt);

		Assert.assertEquals("Wrong number of nodes in DB", usersList.size(), users.size());

		GraphTestUser firstUser = userRepository.get(userId);
		GraphTestUser secondUser = userRepository.get(userId2);

		Assert.assertEquals("userId is not equal", firstUser.getUserId(), userId);
		Assert.assertEquals("username is not equal", firstUser.getUsername(), username);

		Assert.assertEquals("userId is not equal", secondUser.getUserId(), userId2);
		Assert.assertEquals("username is not equal", secondUser.getUsername(), username2);
	}


	//	@Test
//	public void test1() {
//		graph.addVertex("asd2");
//		System.out.println("Uvnitr1..." + graph.getVertices().iterator().next());
//		graph.commit();
//		System.out.println("Uvnitr1..." + graph.getVertices().iterator().next());
//	}
//	@Test(expected = Exception.class)
//	@Test
//	 public void test2() {
//		System.out.println("Uvnitr2..");
//	}

//	@After
//	public void afterClass() {
//		graph.shutdown();
//		System.out.println("Po...");
//	}

}

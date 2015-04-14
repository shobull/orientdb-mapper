import com.google.common.collect.Maps;
import com.tinkerpop.blueprints.Edge;
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
import java.util.UUID;

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
	public void setUp() {
		orientDbSchemaManager.checkDbSchema();
		clearDatabaste();
	}

	private void clearDatabaste() {
		OrientGraph graph = userRepository.getTxGraph();

		for (Vertex vertex : graph.getVertices()) {
			vertex.remove();
		}

		for (Edge edge : graph.getEdges()) {
			edge.remove();
		}

		graph.commit();
	}

	/**
	 * Loading entity from database by unique property test.
	 */
	@Test
	public void loadingNodeByIdTest() {
		String userId = UUID.randomUUID().toString();
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
	 * Inserting single node to database test
	 */
	@Test
	public void insertNodeTest() {
		String userId = UUID.randomUUID().toString();
		String username = "lubos";

		GraphTestUser user = new GraphTestUser();
		user.setUserId(userId);
		user.setUsername(username);

		GraphTestUser loadedUser = userRepository.save(user);
		Assert.assertEquals("userId is not equal", loadedUser.getUserId(), userId);
		Assert.assertEquals("username is not equal", loadedUser.getUsername(), username);
	}

	/**
	 * Inserting collection of nodes to database test
	 */
	@Test
	public void batchInsertNodeTest() {
		String userId = UUID.randomUUID().toString();
		String username = "lubos";

		GraphTestUser user = new GraphTestUser();
		user.setUserId(userId);
		user.setUsername(username);

		String userId2 = UUID.randomUUID().toString();
		String username2 = "petr";

		GraphTestUser user2 = new GraphTestUser();
		user2.setUserId(userId2);
		user2.setUsername(username2);

		List<GraphTestUser> users = Arrays.asList(user, user2);

		userRepository.save(users);

		OrientGraph graph = userRepository.getTxGraph();

		Assert.assertEquals("Wrong number of nodes in DB", graph.countVertices("User"), users.size());

		GraphTestUser firstUser = userRepository.get(userId);
		GraphTestUser secondUser = userRepository.get(userId2);

		Assert.assertEquals("userId is not equal", firstUser.getUserId(), userId);
		Assert.assertEquals("username is not equal", firstUser.getUsername(), username);

		Assert.assertEquals("userId is not equal", secondUser.getUserId(), userId2);
		Assert.assertEquals("username is not equal", secondUser.getUsername(), username2);
	}

	/**
	 * Deleting single node from database test
	 */
	@Test
	public void deleteNodeTest() {
		String userId = UUID.randomUUID().toString();
		String username = "lubos";

		OrientGraph graph = userRepository.getTxGraph();

		Map<String, String> params = Maps.newHashMap();
		params.put("userId", userId);
		params.put("username", username);

		graph.addVertex("class:User", params);
		graph.commit();

		Assert.assertEquals("Insertion to DB wasn't succesful", graph.countVertices("User"), 1);

		userRepository.delete(userId);

		Assert.assertEquals("Node delete wasn't successful", graph.countVertices("User"), 0);
	}

	/**
	 * Loading of ids test
	 */
	@Test
	public void listIdsTest() {
		String userId = UUID.randomUUID().toString();
		String username = "lubos";
		String userId2 = UUID.randomUUID().toString();
		String username2 = "petr";

		OrientGraph graph = userRepository.getTxGraph();

		Map<String, String> params = Maps.newHashMap();
		params.put("userId", userId);
		params.put("username", username);

		Map<String, String> params2 = Maps.newHashMap();
		params2.put("userId", userId2);
		params2.put("username", username2);

		graph.addVertex("class:User", params);
		graph.addVertex("class:User", params2);
		graph.commit();

		List<String> userIds = userRepository.listVertexIds();

		Assert.assertEquals("Wrong ids loaded", userIds, Arrays.asList(userId, userId2));
	}


}

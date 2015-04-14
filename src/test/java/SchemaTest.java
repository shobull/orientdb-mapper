import com.google.common.collect.Maps;
import com.orientechnologies.orient.core.storage.ORecordDuplicatedException;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import config.TestConfig;
import cz.cvut.palislub.persist.OrientDbSchemaChecker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Map;
import java.util.UUID;

/**
 * User: L
 * Date: 14. 4. 2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
public class SchemaTest {

	@Autowired
	OrientDbSchemaChecker orientDbSchemaManager;

	@Autowired
	OrientGraphFactory orientGraphFactory;

	@Before
	public void setUp() {
		orientDbSchemaManager.checkDbSchema();
	}

	@Test(expected = ORecordDuplicatedException.class)
	public void duplicateUniqueKeyTest() {
		String userId = UUID.randomUUID().toString();
		String username = "lubos";
		String username2 = "petr";

		OrientGraph graph = orientGraphFactory.getTx();

		Map<String, String> params = Maps.newHashMap();
		params.put("userId", userId);
		params.put("username", username);

		Map<String, String> params2 = Maps.newHashMap();
		params2.put("userId", userId);
		params2.put("username", username2);

		graph.addVertex("class:User", params);
		graph.addVertex("class:User", params2);
		graph.commit();
	}

}

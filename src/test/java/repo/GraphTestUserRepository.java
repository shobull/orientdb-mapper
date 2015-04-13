package repo;

import cz.cvut.palislub.repository.GenericGraphRepo;
import domain.GraphTestUser;

/**
 * User: L
 * Date: 13. 4. 2015
 */
public class GraphTestUserRepository extends GenericGraphRepo<GraphTestUser, String> {

	public GraphTestUserRepository() {
		super(GraphTestUser.class);
	}
}

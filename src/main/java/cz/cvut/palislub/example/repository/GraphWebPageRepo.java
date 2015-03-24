package cz.cvut.palislub.example.repository;

import cz.cvut.palislub.example.domain.nodes.GraphWebPage;
import cz.cvut.palislub.repository.GenericGraphRepo;

/**
 * Created by lubos on 24.3.2015.
 */
public class GraphWebPageRepo extends GenericGraphRepo<GraphWebPage, String> {

	public GraphWebPageRepo() {
		super(GraphWebPage.class);
	}
}

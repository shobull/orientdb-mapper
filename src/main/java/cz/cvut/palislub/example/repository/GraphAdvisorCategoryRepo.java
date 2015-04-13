package cz.cvut.palislub.example.repository;


import cz.cvut.palislub.example.domain.nodes.GraphAdvisorCategory;
import cz.cvut.palislub.repository.GenericGraphRepo;

/**
 * User: Lubos Palisek
 * Date: 9. 3. 2015
 */
public class GraphAdvisorCategoryRepo extends GenericGraphRepo<GraphAdvisorCategory, Long> {

	public GraphAdvisorCategoryRepo() {
		super(GraphAdvisorCategory.class);
	}
}

package cz.cvut.palislub.example.domain.nodes;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

/**
 * User: L
 * Date: 9. 3. 2015
 */
@Node(name = "AdvisorCategory")
public class GraphAdvisorCategory {

	@NodeProperty
	@Unique
	private long advisorCategoryId;

	@NodeProperty
	private String name;

	public GraphAdvisorCategory() {
	}


	public long getAdvisorCategoryId() {
		return advisorCategoryId;
	}

	public void setAdvisorCategoryId(long advisorCategoryId) {
		this.advisorCategoryId = advisorCategoryId;
	}

	public void setName(String name) {
		this.name = name;
	}
}

package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.example.domain.nodes.GraphAdvisorCategory;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;

/**
 * User: L
 * Date: 9. 3. 2015
 */
@Relationship(type = "has_advisor_category")
public class AdvisorCategoryRelationship {

	@NodeFrom
	private GraphProduct product;

	@NodeTo
	private GraphAdvisorCategory category;

	public AdvisorCategoryRelationship() {
	}

	public AdvisorCategoryRelationship(GraphProduct product, GraphAdvisorCategory category) {
		this.product = product;
		this.category = category;
	}
}

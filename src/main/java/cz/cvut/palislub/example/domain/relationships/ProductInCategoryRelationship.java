package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.annotations.RelationshipProperty;
import cz.cvut.palislub.example.domain.nodes.GraphCategory;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;

/**
 * Created by lubos on 1.3.2015.
 */
@Relationship(type = "belong_to_category")
public class ProductInCategoryRelationship {

	@NodeFrom
	private GraphProduct from;

	@NodeTo
	private GraphCategory category;

	@RelationshipProperty
	private boolean mainCategory = false;

	public ProductInCategoryRelationship() {
	}

	public ProductInCategoryRelationship(GraphProduct from, GraphCategory to) {
		this.from = from;
		this.category = to;
	}

	public void setMainCategory(boolean mainCategory) {
		this.mainCategory = mainCategory;
	}

	public void setFrom(GraphProduct from) {
		this.from = from;
	}

	public void setTo(GraphCategory category) {
		this.category = category;
	}
}

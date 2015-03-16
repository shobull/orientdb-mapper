package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.example.domain.nodes.GraphCategory;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;

/**
 * Created by lubos on 1.3.2015.
 */
@Relationship(type = "belong_to_category", unique = true)
public class ProductInCategoryRelationship {

	@NodeFrom
	private GraphProduct from;

	@NodeTo
	private GraphCategory category;

	public ProductInCategoryRelationship(GraphProduct from, GraphCategory to) {
		this.from = from;
		this.category = to;
	}

	public void setFrom(GraphProduct from) {
		this.from = from;
	}

	public void setTo(GraphCategory category) {
		this.category = category;
	}
}

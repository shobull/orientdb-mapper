package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.example.domain.nodes.GraphCategory;

/**
 * Created by lubos on 1.3.2015.
 */
@Relationship(type = "is_subcategory", unique = true)
public class SubcategoryRelationship {

	@NodeFrom
	private GraphCategory childCategory;

	@NodeTo
	private GraphCategory parentCategory;

	public SubcategoryRelationship(GraphCategory from, GraphCategory to) {
		this.childCategory = from;
		this.parentCategory = to;
	}

	public void setParentCategory(GraphCategory parentCategory) {
		this.parentCategory = parentCategory;
	}

	public void setChildCategory(GraphCategory childCategory) {
		this.childCategory = childCategory;
	}
}

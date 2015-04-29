package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.example.domain.nodes.GraphCategory;
import cz.cvut.palislub.example.domain.nodes.GraphUser;

/**
 * User: L
 * Date: 26. 3. 2015
 */
@Relationship(type = "viewed_category", unique = false)
public class CategoryViewRelationship {

	@NodeFrom
	private GraphUser user;

	@NodeTo
	private GraphCategory category;

	public CategoryViewRelationship() {
	}

	public CategoryViewRelationship(GraphUser user, GraphCategory graphCategory) {
		this.user = user;
		this.category = graphCategory;
	}

	@Override
	public String toString() {
		return "CategoryViewRelationship{" +
				"user=" + user +
				", category=" + category +
				'}';
	}
}

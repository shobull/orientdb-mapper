package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;
import cz.cvut.palislub.example.domain.nodes.GraphUser;

/**
 * User: L
 * Date: 11. 3. 2015
 */
@Relationship(type = "viewed_product", unique = false)
public class ProductViewRelationship {

	@NodeFrom
	private GraphUser user;

	@NodeTo
	private GraphProduct product;

	public ProductViewRelationship() {
	}

	public ProductViewRelationship(GraphUser user, GraphProduct product) {
		this.user = user;
		this.product = product;
	}

	@Override
	public String toString() {
		return "ProductViewRelationship{" +
				"user=" + user +
				", product=" + product +
				'}';
	}
}

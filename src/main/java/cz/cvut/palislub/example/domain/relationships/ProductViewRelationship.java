package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;
import cz.cvut.palislub.example.domain.nodes.GraphUser;

/**
 * Created by lubos on 16.3.2015.
 */
@Relationship(type = "viewed_product")
public class ProductViewRelationship {

	@NodeFrom
	private GraphUser user;

	@NodeTo
	private GraphProduct product;

	public ProductViewRelationship(GraphUser user, GraphProduct product) {
		this.user = user;
		this.product = product;
	}
}

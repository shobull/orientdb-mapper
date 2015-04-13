package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.example.domain.nodes.GraphOrder;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;

/**
 * User: Lubos Palisek
 * Date: 10. 3. 2015
 */
@Relationship(type = "contain_item")
public class ContainItemsRelationship {

	@NodeFrom
	private GraphOrder order;

	@NodeTo
	private GraphProduct product;

	public ContainItemsRelationship(GraphOrder order, GraphProduct product) {
		this.order = order;
		this.product = product;
	}
}

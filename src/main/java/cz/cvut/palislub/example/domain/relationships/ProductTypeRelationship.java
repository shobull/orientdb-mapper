package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;
import cz.cvut.palislub.example.domain.nodes.GraphProductType;

/**
 * Created by lubos on 16.3.2015.
 */
@Relationship(type = "is_product_type")
public class ProductTypeRelationship {

	@NodeFrom
	private GraphProduct product;

	@NodeTo
	private GraphProductType type;

	public ProductTypeRelationship(GraphProduct product, GraphProductType type) {
		this.product = product;
		this.type = type;
	}
}

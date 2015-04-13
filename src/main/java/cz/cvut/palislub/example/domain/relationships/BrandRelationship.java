package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.example.domain.nodes.GraphBrand;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;

/**
 * User: Lubos Palisek
 * Date: 9. 3. 2015
 */
@Relationship(type = "brand_of", unique = true)
public class BrandRelationship {

	@NodeFrom
	private GraphProduct product;

	@NodeTo
	private GraphBrand brand;

	public BrandRelationship(GraphProduct product, GraphBrand brand) {
		this.product = product;
		this.brand = brand;
	}
}

package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.example.domain.nodes.GraphManufacturer;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;

/**
 * User: Lubos Palisek
 * Date: 9. 3. 2015
 */
@Relationship(type = "is_manufactured", unique = true)
public class ManufacturedRelationship {

	@NodeFrom
	private GraphProduct product;

	@NodeTo
	private GraphManufacturer manufacturer;

	public ManufacturedRelationship(GraphProduct product, GraphManufacturer manufacturer) {
		this.product = product;
		this.manufacturer = manufacturer;
	}

	public void setProduct(GraphProduct product) {
		this.product = product;
	}

	public void setManufacturer(GraphManufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}
}

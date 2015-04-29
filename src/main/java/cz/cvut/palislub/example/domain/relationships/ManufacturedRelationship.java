package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.example.domain.nodes.GraphManufacturer;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;

/**
 * User: L
 * Date: 9. 3. 2015
 */
@Relationship(type = "is_manufactured")
public class ManufacturedRelationship {

	@NodeFrom
	private GraphProduct product;

	@NodeTo
	private GraphManufacturer manufacturer;

	public ManufacturedRelationship() {
	}

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

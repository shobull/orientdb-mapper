package cz.cvut.palislub.example.domain.nodes;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

/**
 * User: L
 * Date: 9. 3. 2015
 */
@Node(name = "Manufacturer")
public class GraphManufacturer {

	@NodeProperty
	@Unique
	private long manufacturerId;

	@NodeProperty
	private String name;

	public GraphManufacturer() {
	}

	public long getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public void setName(String name) {
		this.name = name;
	}
}

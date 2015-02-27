package cz.cvut.palislub.example.domain;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

/**
 * User: L
 * Date: 18. 2. 2015
 */
@Node(name = "Product")
public class Product {

	@NodeProperty
	@Unique
	private long productId;

	@NodeProperty
	private String name;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

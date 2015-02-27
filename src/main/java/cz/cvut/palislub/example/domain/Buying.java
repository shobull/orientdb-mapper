package cz.cvut.palislub.example.domain;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;

/**
 * User: L
 * Date: 18. 2. 2015
 */
@Relationship(type = "buy")
public class Buying {

	@NodeFrom
	private User user;

	@NodeTo
	private Product product;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}

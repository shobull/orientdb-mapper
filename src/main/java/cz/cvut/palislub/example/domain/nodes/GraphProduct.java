package cz.cvut.palislub.example.domain.nodes;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

/**
 * Created by lubos on 1.3.2015.
 */
@Node(name = "Product")
public class GraphProduct {

	@NodeProperty
	@Unique
	private long productId;

	@NodeProperty
	private String name;

	@NodeProperty
	private long priceWithVat;

	@NodeProperty
	private long recommended;

	@NodeProperty
	private boolean inSale;

	@NodeProperty
	private int test;

	public GraphProduct(long productId) {
		this.productId = productId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPriceWithVat(Long priceWithVat) {
		this.priceWithVat = priceWithVat;
	}

	public void addRecommended() {
		this.recommended++;
	}

}

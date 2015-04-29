package cz.cvut.palislub.example.domain.nodes;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

import java.math.BigDecimal;

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
	private BigDecimal price;

	@NodeProperty
	private Boolean inSale;

	@NodeProperty
	private Boolean inAction;

	@NodeProperty
	private Boolean basketOffer;

	public GraphProduct() {
	}

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

	public void setPriceWithVat(BigDecimal priceWithVat) {
		this.price = priceWithVat;
	}

	@Override
	public String toString() {
		return "GraphProduct{" +
				"productId=" + productId +
				", name='" + name + '\'' +
				", priceWithVat=" + price +
				", inAction=" + inAction +
				", inSale=" + inSale +
				'}';
	}
}

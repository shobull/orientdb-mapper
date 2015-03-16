package cz.cvut.palislub.example.domain.nodes;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

import java.util.Date;

/**
 * User: L
 * Date: 10. 3. 2015
 */
@Node(name = "Order")
public class GraphOrder {

	@NodeProperty
	@Unique
	private long orderId;

	@NodeProperty
	private Date created;

	@NodeProperty
	private long totalItemsPriceWithVat;

	public GraphOrder(long orderId) {
		this.orderId = orderId;
	}


	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setTotalItemsPriceWithVat(long totalItemsPriceWithVat) {
		this.totalItemsPriceWithVat = totalItemsPriceWithVat;
	}
}

package cz.cvut.palislub.example.domain.nodes;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
	private long orderIdDatabase;

	@NodeProperty
	private Date created;

	@NodeProperty
	private BigDecimal totalItemsPriceWithVat;

	public GraphOrder() {
	}

	public GraphOrder(long id, long orderIdDatabase, LocalDateTime created, BigDecimal totalItemsPriceWithVat) {
		this.orderId = id;
//		this.created = created;
		this.orderIdDatabase = orderIdDatabase;
		this.totalItemsPriceWithVat = totalItemsPriceWithVat;
	}

	public GraphOrder(long id) {
		this.orderId = id;
	}

	public void setOrderId(long id) {
		this.orderId = id;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setTotalItemsPriceWithVat(BigDecimal totalItemsPriceWithVat) {
		this.totalItemsPriceWithVat = totalItemsPriceWithVat;
	}

	public void setOrderIdDatabase(long orderIdDatabase) {
		this.orderIdDatabase = orderIdDatabase;
	}

	public long getOrderIdDatabase() {
		return orderIdDatabase;
	}

	public long getOrderId() {
		return orderId;
	}

	public Date getCreated() {
		return created;
	}

	public BigDecimal getTotalItemsPriceWithVat() {
		return totalItemsPriceWithVat;
	}
}

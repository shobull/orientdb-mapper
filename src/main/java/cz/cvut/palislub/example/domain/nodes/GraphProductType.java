package cz.cvut.palislub.example.domain.nodes;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

/**
 * Created by lubos on 16.3.2015.
 */
@Node(name = "ProductType")
public class GraphProductType {

	@NodeProperty
	@Unique
	private long productTypeId;

	@NodeProperty
	private String name;

	public long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(long productTypeId) {
		this.productTypeId = productTypeId;
	}

	public void setName(String name) {
		this.name = name;
	}
}

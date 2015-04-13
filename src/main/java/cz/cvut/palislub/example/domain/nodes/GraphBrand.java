package cz.cvut.palislub.example.domain.nodes;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

/**
 * User: Lubos Palisek
 * Date: 9. 3. 2015
 */
@Node(name = "Brand")
public class GraphBrand {

	@Unique
	@NodeProperty
	private long brandId;

	@NodeProperty
	private String name;

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public void setName(String name) {
		this.name = name;
	}
}

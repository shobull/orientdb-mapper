package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.example.domain.nodes.GraphOrder;
import cz.cvut.palislub.example.domain.nodes.GraphUser;

/**
 * User: Lubos Palisek
 * Date: 2. 3. 2015
 */
@Relationship(type = "created_order")
public class CreatedOrderRelationship {

	@NodeFrom
	private GraphUser user;

	@NodeTo
	private GraphOrder order;

	public CreatedOrderRelationship(GraphUser user, GraphOrder order) {
		this.user = user;
		this.order = order;
	}

	public void setUser(GraphUser user) {
		this.user = user;
	}

	public void setProduct(GraphOrder order) {
		this.order = order;
	}

}

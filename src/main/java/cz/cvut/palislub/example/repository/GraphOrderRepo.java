package cz.cvut.palislub.example.repository;

import cz.cvut.palislub.example.domain.nodes.GraphOrder;
import cz.cvut.palislub.example.domain.relationships.ContainItemsRelationship;
import cz.cvut.palislub.repository.GenericGraphRepo;

import java.util.List;

/**
 * User: Lubos Palisek
 * Date: 10. 3. 2015
 */
public class GraphOrderRepo extends GenericGraphRepo<GraphOrder, Long> {

	public GraphOrderRepo() {
		super(GraphOrder.class);
	}

	public void saveContainItemsRelationships(List<ContainItemsRelationship> containItemsRelationships) {
		containItemsRelationships.forEach(this::saveRelationship);
	}

	public void saveIterative(List<GraphOrder> userList) {
		userList.stream().forEach(order -> {
			save(order);
		});
	};
}

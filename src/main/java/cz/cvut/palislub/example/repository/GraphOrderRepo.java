package cz.cvut.palislub.example.repository;

import cz.cvut.palislub.example.domain.nodes.GraphOrder;
import cz.cvut.palislub.example.domain.relationships.ContainItemsRelationship;
import cz.cvut.palislub.repository.GenericGraphRepo;

import java.util.List;

/**
 * User: L
 * Date: 10. 3. 2015
 */
public class GraphOrderRepo extends GenericGraphRepo<GraphOrder, Long> {

	public GraphOrderRepo() {
		super(GraphOrder.class);
	}

	public void saveContainItemsRelationships(List<ContainItemsRelationship> containItemsRelationships) {
		containItemsRelationships.forEach(this::saveRelationship);
	}
}
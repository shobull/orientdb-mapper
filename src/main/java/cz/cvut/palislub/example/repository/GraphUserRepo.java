package cz.cvut.palislub.example.repository;

import cz.cvut.palislub.example.domain.nodes.GraphUser;
import cz.cvut.palislub.example.domain.relationships.CreatedOrderRelationship;
import cz.cvut.palislub.repository.GenericGraphRepo;

import java.util.List;

/**
 * User: L
 * Date: 10. 3. 2015
 */
public class GraphUserRepo extends GenericGraphRepo<GraphUser, String> {

	public GraphUserRepo() {
		super(GraphUser.class);
	}

	public void saveCreatedOrderRelationships(List<CreatedOrderRelationship> createdOrderRelationships) {
		createdOrderRelationships.forEach(this::saveRelationship);
	}
}

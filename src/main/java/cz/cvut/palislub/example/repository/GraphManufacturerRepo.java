package cz.cvut.palislub.example.repository;

import cz.cvut.palislub.example.domain.nodes.GraphManufacturer;
import cz.cvut.palislub.repository.GenericGraphRepo;

/**
 * User: Lubos Palisek
 * Date: 9. 3. 2015
 */
public class GraphManufacturerRepo extends GenericGraphRepo<GraphManufacturer, Long> {

	public GraphManufacturerRepo() {
		super(GraphManufacturer.class);
	}
}

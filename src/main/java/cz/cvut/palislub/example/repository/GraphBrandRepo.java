package cz.cvut.palislub.example.repository;


import cz.cvut.palislub.example.domain.nodes.GraphBrand;
import cz.cvut.palislub.repository.GenericGraphRepo;

/**
 * User: Lubos Palisek
 * Date: 9. 3. 2015
 */
public class GraphBrandRepo extends GenericGraphRepo<GraphBrand, Long> {

	public GraphBrandRepo() {
		super(GraphBrand.class);
	}
}

package cz.cvut.palislub.example.repository;

import cz.cvut.palislub.example.domain.nodes.GraphCategory;
import cz.cvut.palislub.example.domain.relationships.SubcategoryRelationship;
import cz.cvut.palislub.repository.GenericGraphRepo;

import java.util.List;

/**
 * User: Lubos Palisek
 * Date: 9. 3. 2015
 */
public class GraphCategoryRepo extends GenericGraphRepo<GraphCategory, Long> {

	public GraphCategoryRepo() {
		super(GraphCategory.class);
	}

	public void saveSubcategoryRelationships(List<SubcategoryRelationship> newSubcategoryRelationships) {
		newSubcategoryRelationships.forEach(this::saveRelationship);
	}
}

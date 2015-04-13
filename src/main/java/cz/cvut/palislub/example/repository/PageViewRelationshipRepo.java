package cz.cvut.palislub.example.repository;

import cz.cvut.palislub.example.domain.relationships.PageViewRelationship;
import cz.cvut.palislub.repository.GenericGraphRepo;

/**
 * User: Lubos Palisek
 * Date: 25. 3. 2015
 */
public class PageViewRelationshipRepo extends GenericGraphRepo<PageViewRelationship, String> {

	public PageViewRelationshipRepo() {
		super(PageViewRelationship.class);
	}
}

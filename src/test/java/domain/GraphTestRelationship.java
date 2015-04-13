package domain;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.annotations.RelationshipProperty;

/**
 * User: Lubos Palisek
 * Date: 13. 4. 2015
 */
@Relationship(type = "TestRelationship")
public class GraphTestRelationship {

	public GraphTestRelationship(GraphTestUser user, GraphTestWebPage webpage) {
		this.user = user;
		this.webpage = webpage;
	}

	@NodeFrom
	private GraphTestUser user;

	@NodeTo
	private GraphTestWebPage webpage;

	@RelationshipProperty
	private long visitLength;

}

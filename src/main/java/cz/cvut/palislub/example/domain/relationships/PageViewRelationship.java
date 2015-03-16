package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.annotations.RelationshipProperty;
import cz.cvut.palislub.example.domain.nodes.GraphUser;
import cz.cvut.palislub.example.domain.nodes.GraphWebPage;

/**
 * User: L
 * Date: 2. 3. 2015
 */
@Relationship(type = "viewed_page")
public class PageViewRelationship {

	@NodeFrom
	private GraphUser user;

	@NodeTo
	private GraphWebPage webpage;

	@RelationshipProperty
	private boolean enterVisit = false;

	public PageViewRelationship(GraphUser user, GraphWebPage webpage) {
		this.user = user;
		this.webpage = webpage;
	}

	public void setEnterVisit(boolean enterVisit) {
		this.enterVisit = enterVisit;
	}

	public void setUser(GraphUser user) {
		this.user = user;
	}

	public void setWebpage(GraphWebPage webpage) {
		this.webpage = webpage;
	}
}

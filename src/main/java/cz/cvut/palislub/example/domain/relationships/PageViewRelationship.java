package cz.cvut.palislub.example.domain.relationships;

import cz.cvut.palislub.annotations.NodeFrom;
import cz.cvut.palislub.annotations.NodeTo;
import cz.cvut.palislub.annotations.Relationship;
import cz.cvut.palislub.annotations.RelationshipProperty;
import cz.cvut.palislub.example.domain.nodes.GraphUser;
import cz.cvut.palislub.example.domain.nodes.GraphWebPage;

import java.util.Date;

/**
 * User: L
 * Date: 2. 3. 2015
 */
@Relationship(type = "viewed_page", unique = false)
public class PageViewRelationship {

	@NodeFrom
	private GraphUser user;

	@NodeTo
	private GraphWebPage webpage;

	@RelationshipProperty
	private boolean enterVisit = false;

	@RelationshipProperty
	private Date visitedTime;

	@RelationshipProperty
	private long visitLength;

	public PageViewRelationship() {
	}

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

	public void setVisitedTime(Date visitedTime) {
		this.visitedTime = visitedTime;
	}

	public void setVisitLength(long visitLength) {
		this.visitLength = visitLength;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("PageViewRelationship{");
		sb.append("user=").append(user);
		sb.append(", webpage=").append(webpage);
		sb.append(", enterVisit=").append(enterVisit);
		sb.append(", visitedTime=").append(visitedTime);
		sb.append(", visitLength=").append(visitLength);
		sb.append('}');
		return sb.toString();
	}
}

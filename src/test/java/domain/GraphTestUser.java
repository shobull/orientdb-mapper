package domain;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

/**
 * User: Lubos Palisek
 * Date: 13. 4. 2015
 */
@Node(name = "User")
public class GraphTestUser {

	@NodeProperty
	@Unique
	public String userId;

	@NodeProperty
	public String username = "";

	public long age;

	public GraphTestUser() {
	}

	public GraphTestUser(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("GraphUser{");
		sb.append("userId='").append(userId).append('\'');
		sb.append(", username='").append(username).append('\'');
		sb.append('}');
		return sb.toString();
	}
}

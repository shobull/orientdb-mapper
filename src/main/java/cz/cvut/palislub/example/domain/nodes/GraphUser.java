package cz.cvut.palislub.example.domain.nodes;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

/**
 * Created by lubos on 28.2.2015.
 */
@Node(name = "User")
public class GraphUser {

	@NodeProperty
	@Unique
	public String userId;

	@NodeProperty
	public String username = "";

	@NodeProperty
	public String email = "";

	public GraphUser() {
	}

	public GraphUser(String userId, String username, String email) {
		this.userId = userId;
		this.username = username != null ? username : null;
		this.email = email;
	}

	public GraphUser(String userId) {
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

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return "GraphUser{" +
				"userId='" + userId + '\'' +
				", username='" + username + '\'' +
				'}';
	}
}

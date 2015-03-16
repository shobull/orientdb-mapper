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
}

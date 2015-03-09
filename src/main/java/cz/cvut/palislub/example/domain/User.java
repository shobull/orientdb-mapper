package cz.cvut.palislub.example.domain;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

/**
 * User: L
 * Date: 16. 2. 2015
 */
@Node(name = "User")
public class User {

	@NodeProperty
	@Unique
	private String userId;

	@NodeProperty
	private String name;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

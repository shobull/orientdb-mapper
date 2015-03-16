package cz.cvut.palislub.example.domain.nodes;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

/**
 * Created by lubos on 1.3.2015.
 */
@Node(name = "Category")
public class GraphCategory {

	@NodeProperty
	@Unique
	private long categoryId;

	@NodeProperty
	private String name;

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public void setName(String name) {
		this.name = name;
	}
}

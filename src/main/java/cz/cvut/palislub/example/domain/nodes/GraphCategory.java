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

	public GraphCategory() {
	}

	public GraphCategory(Long id) {
		this.categoryId = id;
	}

	public GraphCategory(Long id, String name) {
		this.categoryId = id;
		this.name = name;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("GraphCategory{");
		sb.append("categoryId=").append(categoryId);
		sb.append(", name='").append(name).append('\'');
		sb.append('}');
		return sb.toString();
	}
}

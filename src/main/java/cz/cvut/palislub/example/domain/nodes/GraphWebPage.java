package cz.cvut.palislub.example.domain.nodes;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

/**
 * Created by lubos on 1.3.2015.
 */
@Node(name = "WebPage")
public class GraphWebPage {

	@NodeProperty
	@Unique
	private String path;

	public GraphWebPage() {
	}

	public GraphWebPage(String path) {
		this.path = path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("GraphWebPage{");
		sb.append("path='").append(path).append('\'');
		sb.append('}');
		return sb.toString();
	}
}

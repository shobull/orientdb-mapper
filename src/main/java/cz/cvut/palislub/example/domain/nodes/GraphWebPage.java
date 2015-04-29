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

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return "GraphWebPage{" +
				"path='" + path + '\'' +
				'}';
	}
}

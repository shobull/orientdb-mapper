package domain;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

/**
 * User: Lubos Palisek
 * Date: 13. 4. 2015
 */
@Node(name = "WebPage")
public class GraphTestWebPage {

	@NodeProperty
	@Unique
	private String webPageId;

	public void setWebPageId(String webPageId) {
		this.webPageId = webPageId;
	}
}

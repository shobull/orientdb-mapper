package cz.cvut.palislub.example.domain.dao;

import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesFunction;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;
import cz.cvut.palislub.example.domain.nodes.GraphUser;
import cz.cvut.palislub.example.domain.nodes.GraphWebPage;
import cz.cvut.palislub.example.domain.relationships.PageViewRelationship;
import cz.cvut.palislub.example.domain.relationships.ProductViewRelationship;
import cz.cvut.palislub.example.repository.GraphProductRepo;
import cz.cvut.palislub.example.repository.GraphUserRepo;
import cz.cvut.palislub.example.repository.GraphWebPageRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lubos on 17.3.2015.
 */
public class MyDao {

	@Autowired
	private GraphProductRepo graphProductRepo;

	@Autowired
	private GraphUserRepo graphUserRepo;

	@Autowired
	private GraphWebPageRepo graphWebPageRepo;


	public void getSimilarProducts(int i, String s, String page) {
		GraphUser graphUser = new GraphUser(s);
		GraphWebPage graphWebPage = new GraphWebPage(page);
		PageViewRelationship pageViewRelationship = new PageViewRelationship(graphUser, graphWebPage);
		graphUserRepo.save(graphUser);
		graphWebPageRepo.save(graphWebPage);
		graphUserRepo.saveRelationship(pageViewRelationship);
		trackPageViewByType(page, graphUser);
	}

	private void trackPageViewByType(String pageUrl, GraphUser graphUser) {
			GraphProduct gp = new GraphProduct(824);
			ProductViewRelationship productViewRelationship = new ProductViewRelationship(graphUser, gp);
			graphUserRepo.saveRelationship(productViewRelationship);
	}
}

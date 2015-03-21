package cz.cvut.palislub.example.domain.dao;

import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.PipesFunction;
import cz.cvut.palislub.example.repository.GraphProductRepo;
import cz.cvut.palislub.example.repository.GraphUserRepo;
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


	public void getSimilarProducts(int i, String s) {
		graphUserRepo.getAveragePriceOfViewedItemsFromSameCategory(s, i);
	}
}

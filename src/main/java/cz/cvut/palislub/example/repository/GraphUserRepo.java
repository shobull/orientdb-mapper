package cz.cvut.palislub.example.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngineFactory;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.transform.OrderPipe;
import com.tinkerpop.pipes.util.PipesFunction;
import cz.cvut.palislub.example.domain.nodes.GraphUser;
import cz.cvut.palislub.example.domain.relationships.CreatedOrderRelationship;
import cz.cvut.palislub.repository.GenericGraphRepo;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: L
 * Date: 10. 3. 2015
 */
public class GraphUserRepo extends GenericGraphRepo<GraphUser, String> {

	public GraphUserRepo() {
		super(GraphUser.class);
	}

	public void saveCreatedOrderRelationships(List<CreatedOrderRelationship> createdOrderRelationships) {
		createdOrderRelationships.forEach(this::saveRelationship);
	}

	public long getAveragePriceOfViewedItemsFromSameCategory(String userId, long productId) {

		GremlinPipeline userPipeline = new GremlinPipeline();
		userPipeline.start(getVertex(userId))
				.out("viewed_product").as("products")
				.out("belong_to_category")
				.in("belong_to_category").has("productId", productId)
				.back("products")
				.property("priceWithVat")
				.gather(new PipeFunction<List<Long>, Object>() {
					@Override
					public Object compute(List<Long> argument) {
						long sum = 0;
						for (Long price : argument) {
							sum += price;
						}
						return Math.round(sum / argument.size());
					};
				});


		System.out.println("xxxxxxxx" + userPipeline.next());


		// Return avg price of viewed products from the same category
		String SAME_CATEGORY_QUERY = "v.out('viewed_product').as('products').out('belong_to_category').in('belong_to_category').has('productId', productId).back('products').priceWithVat.mean()";

		// Return avg price of viewed products from the same advisor category
		String SAME_ADV_CATEGORY_QUERY = "v.out('viewed_product').as('products').out('has_advisor_category').in('has_advisor_category').has('productId', productId).back('products').priceWithVat.mean()";


		ScriptEngine engine = new GremlinGroovyScriptEngineFactory().getScriptEngine();

		Bindings bindings = engine.createBindings();
		bindings.put("g", getGraph());
		bindings.put("v", getVertex(userId));
		bindings.put("productId", productId);

		long averagePrice = 0;

		try {
			averagePrice += (long) Math.floor((double) engine.eval(SAME_CATEGORY_QUERY, bindings));
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		try {
			averagePrice += (long) Math.floor((double) engine.eval(SAME_ADV_CATEGORY_QUERY, bindings));
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		return averagePrice;
	}

}

package cz.cvut.palislub.example.repository;

import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.util.PipesFunction;
import cz.cvut.palislub.example.domain.nodes.GraphUser;
import cz.cvut.palislub.example.domain.relationships.CreatedOrderRelationship;
import cz.cvut.palislub.repository.GenericGraphRepo;

import java.util.List;

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

		List<Long> list = Lists.newArrayList();

		GremlinPipeline userPipeline = new GremlinPipeline();
		userPipeline.start(getVertex(userId))
				.out("viewed_product").as("products")
				.out("belong_to_category")
				.in("belong_to_category").has("productId", productId)
				.back("products").property("priceWithVat").fill(list);

		Long sum = 0l;
		if (!list.isEmpty()) {
			for (Long price : list) {
				sum += price;
			}
			return (long) sum.doubleValue() / list.size();
		}
		return sum;
	}

}

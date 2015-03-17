package cz.cvut.palislub.example.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.groovy.Gremlin;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.transform.GatherPipe;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;
import cz.cvut.palislub.example.domain.relationships.AdvisorCategoryRelationship;
import cz.cvut.palislub.example.domain.relationships.BrandRelationship;
import cz.cvut.palislub.example.domain.relationships.ManufacturedRelationship;
import cz.cvut.palislub.example.domain.relationships.ProductInCategoryRelationship;
import cz.cvut.palislub.repository.GenericGraphRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: L
 * Date: 9. 3. 2015
 */
public class GraphProductRepo extends GenericGraphRepo<GraphProduct, Long> {

	public GraphProductRepo() {
		super(GraphProduct.class);
	}

	public void saveProductInCategoryRelationships(List<ProductInCategoryRelationship> newProductInCategoryRelationships) {
		newProductInCategoryRelationships.forEach(this::saveRelationship);
	}

	public void saveManufacturedRelationships(List<ManufacturedRelationship> newManufacturedRelationships) {
		newManufacturedRelationships.forEach(this::saveRelationship);
	}

	public void saveBrandRelationships(List<BrandRelationship> newAdvisorCategoryRelationships) {
		newAdvisorCategoryRelationships.forEach(this::saveRelationship);
	}

	public void saveAdvisorCategoryRelationships(List<AdvisorCategoryRelationship> newAdvisorCategoryRelationships) {
		newAdvisorCategoryRelationships.forEach(this::saveRelationship);
	}

	public List<Long> getSimilarProducts(long id) {

		Set<Long> productIds = Sets.newLinkedHashSet();

		List<GremlinPipeline> pipes = Lists.newArrayList();
		GremlinPipeline firstPipeline = new GremlinPipeline();
		firstPipeline.start(getVertex(id))
				.out("belong_to_category").in("belong_to_category")
				.as("products")
				.out("has_advisor_category").in("has_advisor_category").has("productId", id)
				.back("products")
				.out("is_product_type").in("is_product_type").has("productId", id)
				.back("products").property("productId");

		GremlinPipeline secondPipeline = new GremlinPipeline();
		secondPipeline.start(getVertex(id))
				.out("belong_to_category").in("belong_to_category")
				.as("products")
				.out("has_advisor_category").in("has_advisor_category").has("productId", id)
				.back("products").property("productId");

		GremlinPipeline thirdPipeline = new GremlinPipeline();
		thirdPipeline.start(getVertex(id))
				.out("belong_to_category").in("belong_to_category")
				.as("products")
				.out("is_product_type").in("is_product_type").has("productId", id)
				.back("products").property("productId");

		pipes.add(firstPipeline);
		pipes.add(secondPipeline);
		pipes.add(thirdPipeline);

		for (GremlinPipeline pipe : pipes) {
			if (productIds.size() < 10) {
				pipe.fill(productIds);
				System.out.println("PLNIM");
				for (Long pid : productIds) {
					System.out.println(pid);
				}
			}
		}

		return Lists.newArrayList(productIds);
	}

}

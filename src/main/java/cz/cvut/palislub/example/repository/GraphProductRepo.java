package cz.cvut.palislub.example.repository;

import com.tinkerpop.blueprints.Vertex;
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

	public List getSimilarProducts(long i) {

		GremlinPipeline pipeline = new GremlinPipeline();


		Map<Vertex, Integer> map = new HashMap();

		// gremlin> g.V.as('x').outE('knows').inV.has('age', T.gt, 30).back('x').age

		pipeline.start(getVertex(i))
				.out("belong_to_category").in("belong_to_category").as("products")
				.out("has_advisor_category").in("has_advisor_category").has("productId", i)
				.back("products")
				.out("brand_of").in("brand_of").has("productId", i)
				.back("products");

//		pipeline.start(getVertex(i)).as("actualProduct").out("belong_to_category").in("belong_to_category")
//				.back("actualProduct").out("has_advisor_category").in("has_advisor_category")
//				.back("actualProduct");
//				.groupCount(map);


//		System.out.println("XXXXXXXXXXXXXXX " + pipeline.count());
		for (Object o : pipeline) {
			System.out.println(o);
		}
		System.out.println("XXXXXXXXXXXXXXX " + pipeline.count());
		for (Object o : map.keySet()) {
			System.out.println(o + " " + map.get(o));
		}


//
//		GremlinPipeline pipeline = new GremlinPipeline();
//		pipeline.start(getVertex(i)).out("belong_to_category").in("belong_to_category").groupCount(map);
//
//		while(pipeline.hasNext()) {
//			pipeline.next();
//		}

//		for (Object s : map.keySet()) {
//			System.out.println(s + " " + map.get(s));
//		}

		return null;
	}

}

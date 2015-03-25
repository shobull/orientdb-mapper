package cz.cvut.palislub.example.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.transform.OrderPipe;
import com.tinkerpop.pipes.util.structures.Pair;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;
import cz.cvut.palislub.example.domain.relationships.AdvisorCategoryRelationship;
import cz.cvut.palislub.example.domain.relationships.BrandRelationship;
import cz.cvut.palislub.example.domain.relationships.ManufacturedRelationship;
import cz.cvut.palislub.example.domain.relationships.ProductInCategoryRelationship;
import cz.cvut.palislub.repository.GenericGraphRepo;

import java.util.*;

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


	public void getCollaborativeProducts(long i) {
		Set<Long> productIds = Sets.newLinkedHashSet();

		List<GremlinPipeline> pipes = Lists.newArrayList();
		GremlinPipeline firstPipeline = new GremlinPipeline();

		OrderPipe sortPipe = new OrderPipe(new PipeFunction<Pair, Integer>() {
			@Override
			public Integer compute(Pair argument) {
				OrientVertex a = (OrientVertex) argument.getA();
				OrientVertex b = (OrientVertex) argument.getB();
				Long priceA = Math.abs((Long) a.getProperty("priceWithVat") - 4590l);
				Long priceB = Math.abs((Long) b.getProperty("priceWithVat") - 4590l);
				if (priceA == priceB)
					return 0;
				else
					return priceA > priceB ? 1 : -1;
			}
		});

		firstPipeline.start(getVertex(i))
				.out("belong_to_category").in("belong_to_category")
				.as("products")
				.out("has_advisor_category").in("has_advisor_category").has("productId", i)
				.back("products")
				.out("is_product_type").in("is_product_type").has("productId", i)
				.back("products")
				.has("inSale", true)
				.add(sortPipe)
				.property("productId")
				.fill(productIds);

		GremlinPipeline secondPipeline = new GremlinPipeline();
		secondPipeline.start(getVertex(i))
				.out("belong_to_category").in("belong_to_category")
				.as("products")
				.out("has_advisor_category").in("has_advisor_category").has("productId", i)
				.back("products")
				.has("inSale", true)
				.add(sortPipe)
				.property("productId")
				.fill(productIds);

		GremlinPipeline fourthPipeline = new GremlinPipeline();
		fourthPipeline.start(getVertex(i))
				.out("belong_to_category").in("belong_to_category")
				.as("products")
				.out("is_product_type").in("is_product_type").has("productId", i)
				.back("products")
				.has("inSale", true)
				.add(sortPipe)
				.property("productId")
				.fill(productIds);

		GremlinPipeline thirdPipeline = new GremlinPipeline();
		thirdPipeline.start(getVertex(i))
				.out("belong_to_category")
				.in("belong_to_category")
				.has("inSale", true)
				.add(sortPipe)
				.property("productId")
				.fill(productIds);

		System.out.println(productIds);

//		return Lists.newArrayList(productIds);

	}


	private List<Long> sortByValues(HashMap<Long, Long> map) {
		List resultList = Lists.newArrayList(map.entrySet());
		Collections.sort(resultList, new Comparator<Map.Entry<Long, Long>>() {
			@Override
			public int compare(Map.Entry o1, Map.Entry o2) {
				return Long.compare((long) o2.getValue(), (long) o1.getValue());
			}
		});

		return transformToIdList(resultList);
	}

	private List<Long> transformToIdList(List<Map.Entry<Long, Long>> resultList) {
		List<Long> ids = Lists.newArrayList();

		for (Map.Entry e : resultList) {
			ids.add((long) e.getKey());
		}

		return ids;
	}

}

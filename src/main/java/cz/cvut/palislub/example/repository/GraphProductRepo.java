package cz.cvut.palislub.example.repository;

import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.filter.FilterFunctionPipe;
import com.tinkerpop.pipes.filter.FilterPipe;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;
import cz.cvut.palislub.example.domain.relationships.AdvisorCategoryRelationship;
import cz.cvut.palislub.example.domain.relationships.BrandRelationship;
import cz.cvut.palislub.example.domain.relationships.ManufacturedRelationship;
import cz.cvut.palislub.example.domain.relationships.ProductInCategoryRelationship;
import cz.cvut.palislub.repository.GenericGraphRepo;

import java.util.*;

/**
 * User: Lubos Palisek
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


	public void getFreeDeliveryRecommendationProducts(int price) {

		List<Long> prices = Lists.newArrayList();

		FilterPipe filterByPricePipe = filterByPricePipe(price);

		GremlinPipeline freeDeliveryPipeline = new GremlinPipeline(getVertices());
		freeDeliveryPipeline
				.add(filterByPricePipe)
				.property("priceWithVat")
				.fill(prices);

		for (Long aLong : prices) {
			System.out.println(aLong);
		}

//		System.out.println("XXXXX " + freeDeliveryPipeline.next());
	}


	public static FilterPipe filterByPricePipe(long priceToFreeDelivery) {
		return new FilterFunctionPipe<OrientVertex>(vertex -> ((Long) vertex.getProperty("priceWithVat")) > priceToFreeDelivery);
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

package cz.cvut.palislub.example.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.gremlin.groovy.Gremlin;
import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngineFactory;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.transform.GatherPipe;
import com.tinkerpop.pipes.transform.OrderPipe;
import com.tinkerpop.pipes.transform.TransformPipe;
import com.tinkerpop.pipes.util.structures.Pair;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;
import cz.cvut.palislub.example.domain.relationships.AdvisorCategoryRelationship;
import cz.cvut.palislub.example.domain.relationships.BrandRelationship;
import cz.cvut.palislub.example.domain.relationships.ManufacturedRelationship;
import cz.cvut.palislub.example.domain.relationships.ProductInCategoryRelationship;
import cz.cvut.palislub.repository.GenericGraphRepo;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
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


}

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
import java.util.HashMap;
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

}

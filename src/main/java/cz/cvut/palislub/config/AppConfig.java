package cz.cvut.palislub.config;

import com.orientechnologies.orient.core.command.script.ODatabaseScriptManager;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import cz.cvut.palislub.example.domain.nodes.GraphOrder;
import cz.cvut.palislub.example.repository.GraphOrderRepo;
import cz.cvut.palislub.example.repository.GraphProductRepo;
import cz.cvut.palislub.example.repository.GraphUserRepo;
import cz.cvut.palislub.example.repository.GraphWebPageRepo;
import cz.cvut.palislub.persist.*;
import cz.cvut.palislub.repository.GenericGraphRepo;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * User: L
 * Date: 17. 2. 2015
 */
@Configuration
@ComponentScan("cz.cvut.palislub.example.repository")
public class AppConfig {

	@Bean
	public OrientDbPersister persister() {
		OrientDbPersister persister = new OrientDbPersister();
		return persister;
	}

	@Bean
	public GraphProductRepo graphProductRepo() {
		return new GraphProductRepo();
	}

	@Bean
	public GraphUserRepo graphUserRepo() {
		return new GraphUserRepo();
	}

	@Bean
	public GraphWebPageRepo graphWebPageRepo() {
		return new GraphWebPageRepo();
	}

	@Bean
	public GraphOrderRepo graphOrderRepo() {
		return new GraphOrderRepo();
	}

	@Bean
	public AnnotationResolver annotationResolver() {
		return new AnnotationResolver();
	}

	@Bean
	public OrientDbSchemaManager orientDbSchemaManager() {
		return new OrientDbSchemaManager();
	}

	@Bean
	OrientGraphFactory orientGraphFactory() {
		OrientGraphFactory factory = new OrientGraphFactory("remote:localhost/nazubytest");
		factory.setupPool(1, 5);
		return factory;
	}

	@Bean
	public OrientDbSchemaChecker orientDbSchemaChecker() {
		OrientDbSchemaChecker orientDbSchemaChecker = new OrientDbSchemaChecker();
		orientDbSchemaChecker.setScannedPackage("cz.cvut.palislub.example.domain");
		return orientDbSchemaChecker;
	}

	@Bean
	public OrientDbManager graphManager() {
		OrientDbManager graphManager = new OrientDbManager();
		return graphManager;
	}

	@Bean
	public OrientDbConvertor orientDbConvertor() {
		return new OrientDbConvertor();
	}

}

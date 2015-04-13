package config;

/**
 * User: L
 * Date: 13. 4. 2015
 */

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import cz.cvut.palislub.persist.*;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import repo.GraphTestUserRepository;

/**
 * User: Lubos Palisek
 * Date: 17. 2. 2015
 */
@Configuration
@ComponentScan("repo")
public class TestConfig {

	@Bean
	public OrientDbPersister persister() {
		OrientDbPersister persister = new OrientDbPersister();
		return persister;
	}

	@Bean
	public GraphTestUserRepository graphTestUserRepository() {
		return new GraphTestUserRepository();
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
		OrientGraphFactory factory = new OrientGraphFactory("memory:testing-db");
		return factory;
	}

	@Bean
	public OrientDbSchemaChecker orientDbSchemaChecker() {
		OrientDbSchemaChecker orientDbSchemaChecker = new OrientDbSchemaChecker();
		orientDbSchemaChecker.setScannedPackage("domain");
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

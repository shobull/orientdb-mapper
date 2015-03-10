package cz.cvut.palislub.config;

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import cz.cvut.palislub.persist.OrientDbConvertor;
import cz.cvut.palislub.persist.OrientDbManager;
import cz.cvut.palislub.persist.OrientDbPersister;
import cz.cvut.palislub.persist.OrientDbSchemaChecker;
import cz.cvut.palislub.resolver.AnnotationResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * User: L
 * Date: 17. 2. 2015
 */
@Configuration
@ComponentScan("cz.cvut.palislub.example.repo")
public class AppConfig {

	@Bean
	public OrientDbPersister persister() {
		OrientDbPersister persister = new OrientDbPersister();
		return persister;
	}

	@Bean
	public AnnotationResolver annotationResolver() {
		return new AnnotationResolver();
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
		OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/Lubos/db");
		OrientGraphNoTx graph = factory.getNoTx();
		graphManager.setGraph(graph);
		return graphManager;
	}

	@Bean
	public OrientDbConvertor orientDbConvertor() {
		return new OrientDbConvertor();
	}

}

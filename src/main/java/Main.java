import cz.cvut.palislub.config.AppConfig;
import cz.cvut.palislub.example.domain.Buying;
import cz.cvut.palislub.example.domain.Product;
import cz.cvut.palislub.example.domain.User;
import cz.cvut.palislub.repository.GenericGraphRepo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * User: L
 * Date: 16. 2. 2015
 */
public class Main {

	public static void main(String[] args) {

		User user = new User();
		user.setName("Lubos");
		user.setUserId("1");

		User user2 = new User();
		user2.setUserId("2");
		user2.setName("Petr");

		Product product = new Product();
		product.setProductId(1);
		product.setName("Kartacek na zuby");

		Product product2 = new Product();
		product2.setProductId(2);
		product2.setName("Pasta na zuby");

		Buying buying = new Buying();
		buying.setUser(user);
		buying.setProduct(product);

		Buying buying3 = new Buying();
		buying3.setUser(user);
		buying3.setProduct(product2);

//		Buying buying2 = new Buying();
//		buying2.setUser(user2);
//		buying2.setProduct(product);

		ApplicationContext ctx =
				new AnnotationConfigApplicationContext(AppConfig.class);
		GenericGraphRepo genericGraphRepo = ctx.getBean(GenericGraphRepo.class);
//		genericGraphRepo.save(user);

//		genericGraphRepo.clearDatabase();

		List<User> users = new ArrayList();
		users.add(user);
		users.add(user2);
//
		genericGraphRepo.save(users);
		genericGraphRepo.save(product);
		genericGraphRepo.save(product2);
		genericGraphRepo.save(buying);
		genericGraphRepo.save(buying3);

		genericGraphRepo.loadTest();

//		System.out.println(genericGraphRepo.count(User.class));

		// AT THE BEGINNING

// EVERY TIME YOU NEED A GRAPH INSTANCE
//		OrientGraph graph = factory.getTx();
//		try {
//			Vertex luca = graph.addVertex(null);
//			luca.setProperty("name", "Luca");
//
//			Vertex marko = graph.addVertex(null);
//			marko.setProperty("name", "Marko");
//
//			Edge lucaKnowsMarko = graph.addEdge(null, luca, marko, "knows");
//			System.out.println("Created edge: " + lucaKnowsMarko.getId());
//

//			System.out.println(graph.countVertices("asd"));
//
//		} finally {
//			graph.shutdown();
//		}

	}

}

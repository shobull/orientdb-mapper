import com.google.common.collect.Lists;
import cz.cvut.palislub.config.AppConfig;
import cz.cvut.palislub.example.domain.nodes.GraphOrder;
import cz.cvut.palislub.example.domain.nodes.GraphUser;
import cz.cvut.palislub.example.domain.nodes.GraphWebPage;
import cz.cvut.palislub.example.domain.relationships.PageViewRelationship;
import cz.cvut.palislub.example.repository.GraphOrderRepo;
import cz.cvut.palislub.example.repository.GraphProductRepo;
import cz.cvut.palislub.example.repository.GraphUserRepo;
import cz.cvut.palislub.persist.OrientDbSchemaChecker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.List;

/**
 * User: L
 * Date: 16. 2. 2015
 */
public class Main {

	public static void main(String[] args) {

		ApplicationContext ctx =
				new AnnotationConfigApplicationContext(AppConfig.class);
		GraphProductRepo productRepo = ctx.getBean(GraphProductRepo.class);
		GraphUserRepo userRepo = ctx.getBean(GraphUserRepo.class);

		GraphOrderRepo graphOrderRepo = ctx.getBean(GraphOrderRepo.class);
		OrientDbSchemaChecker orientDbSchemaChecker = ctx.getBean(OrientDbSchemaChecker.class);

		orientDbSchemaChecker.checkDbSchema();
		List<GraphOrder> userList = Lists.newArrayList();
		for (int i = 0; i < 10000; i++) {
			GraphOrder go = new GraphOrder(i);
			go.setCreated(new Date());
			go.setTotalItemsPriceWithVat(500);
			userList.add(go);
		}

		long start, end;
		boolean batch = true;
		if (batch) {
			start = System.nanoTime();
			graphOrderRepo.save(userList);
			end = System.nanoTime();
			System.out.println(end - start);
			graphOrderRepo.removeAllNodes();

			start = System.nanoTime();
			graphOrderRepo.save(userList);
			end = System.nanoTime();
			System.out.println(end - start);
			graphOrderRepo.removeAllNodes();

			start = System.nanoTime();
			graphOrderRepo.save(userList);
			end = System.nanoTime();
			System.out.println(end - start);
			graphOrderRepo.removeAllNodes();
		} else {
			start = System.nanoTime();
			graphOrderRepo.saveIterative(userList);
			end = System.nanoTime();
			System.out.println(end - start);
			graphOrderRepo.removeAllNodes();

			start = System.nanoTime();
			graphOrderRepo.saveIterative(userList);
			end = System.nanoTime();
			System.out.println(end - start);
			graphOrderRepo.removeAllNodes();

			start = System.nanoTime();
			graphOrderRepo.saveIterative(userList);
			end = System.nanoTime();
			System.out.println(end - start);
			graphOrderRepo.removeAllNodes();
		}

	}

}

import cz.cvut.palislub.config.AppConfig;
import cz.cvut.palislub.example.domain.dao.MyDao;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;
import cz.cvut.palislub.example.domain.nodes.GraphUser;
import cz.cvut.palislub.example.domain.relationships.PageViewRelationship;
import cz.cvut.palislub.example.domain.relationships.ProductViewRelationship;
import cz.cvut.palislub.example.repository.GraphProductRepo;
import cz.cvut.palislub.example.repository.GraphUserRepo;
import groovy.util.slurpersupport.GPathResult;
import org.springframework.beans.factory.annotation.Autowired;
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


		ApplicationContext ctx =
				new AnnotationConfigApplicationContext(AppConfig.class);
		GraphProductRepo productRepo = ctx.getBean(GraphProductRepo.class);
		GraphUserRepo userRepo = ctx.getBean(GraphUserRepo.class);


		MyDao dao = ctx.getBean(MyDao.class);

		dao.getSimilarProducts(962, "4hV7WA4XcL.1426924417578");


//		GraphUser gu = new GraphUser("xxxx.1426536665.1426536665");
//
//		userRepo.save(gu);

//		ProductViewRelationship pw1 = new ProductViewRelationship(gu, new GraphProduct(237));
//		ProductViewRelationship pw2 = new ProductViewRelationship(gu, new GraphProduct(517));
//		ProductViewRelationship pw3 = new ProductViewRelationship(gu, new GraphProduct(223));
//		ProductViewRelationship pw4 = new ProductViewRelationship(gu, new GraphProduct(315));
//
//		userRepo.saveRelationship(pw1);
//		userRepo.saveRelationship(pw2);
//		userRepo.saveRelationship(pw3);
//		userRepo.saveRelationship(pw4);

//		for (Object o : productRepo.getSimilarProducts(2)) {
//			System.out.println(o);
//		}

//		System.out.println(productRepo.getSimilarProducts(2).size());

	}

}

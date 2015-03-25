package cz.cvut.palislub.example.domain.dao;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.transform.OrderPipe;
import com.tinkerpop.pipes.util.structures.Pair;
import cz.cvut.palislub.example.domain.nodes.GraphProduct;
import cz.cvut.palislub.example.domain.nodes.GraphUser;
import cz.cvut.palislub.example.domain.nodes.GraphWebPage;
import cz.cvut.palislub.example.domain.relationships.PageViewRelationship;
import cz.cvut.palislub.example.domain.relationships.ProductViewRelationship;
import cz.cvut.palislub.example.repository.GraphProductRepo;
import cz.cvut.palislub.example.repository.GraphUserRepo;
import cz.cvut.palislub.example.repository.GraphWebPageRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lubos on 17.3.2015.
 */
public class MyDao {

	@Autowired
	private GraphProductRepo graphProductRepo;

	@Autowired
	private GraphUserRepo graphUserRepo;

	@Autowired
	private GraphWebPageRepo graphWebPageRepo;

	public void test() {
		Vertex vertex = graphUserRepo.getVertex("d09c49d2228b4547a50c4ae1ce42e832.1427274908318");
//		Vertex vertex = graphUserRepo.getVertex("shobull");

		GremlinPipeline pipe = new GremlinPipeline();
		pipe.start(vertex)
				.outE("viewed_page")
				.add(new OrderPipe(new PipeFunction<Pair<OrientEdge, OrientEdge>, Integer>() {
					@Override
					public Integer compute(Pair<OrientEdge, OrientEdge> argument) {
						OrientEdge a = argument.getA();
						OrientEdge b = argument.getB();
						Date aDate = a.getProperty("visitedTime");
						Date bDate = b.getProperty("visitedTime");
						return bDate.compareTo(aDate);
					}
				}));

		List<OrientEdge> dates = pipe.toList();

		System.out.println(graphUserRepo.getProperty(dates.get(0), "visitedTime"));
		System.out.println(graphUserRepo.getProperty(dates.get(1), "visitedTime"));

		Date lastVisit = (Date) graphUserRepo.getProperty(dates.get(0), "visitedTime");
		long actualVisit = 1427294037000l;

		long diff = actualVisit - lastVisit.getTime();
		long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		long seconds = TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS);


		System.out.println(days);
		System.out.println(seconds);





//		graphUserRepo.setProperty(dates.get(0), "visitLength", 20);

	}

	private void trackVisitTime(String cookieValue) {
		Date lastPageVisit = new Date();

		long time = 1417270908318l;

		long diff = time - lastPageVisit.getTime();
		System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
	}


	public void getSimilarProducts(int i, String s, String page) {
		GraphUser graphUser = new GraphUser(s);
		GraphWebPage graphWebPage = new GraphWebPage(page);
		PageViewRelationship pageViewRelationship = new PageViewRelationship(graphUser, graphWebPage);
		graphUserRepo.save(graphUser);
		graphWebPageRepo.save(graphWebPage);
		graphUserRepo.saveRelationship(pageViewRelationship);
		trackPageViewByType(page, graphUser);
	}

	private void trackPageViewByType(String pageUrl, GraphUser graphUser) {
		GraphProduct gp = new GraphProduct(824);
		ProductViewRelationship productViewRelationship = new ProductViewRelationship(graphUser, gp);
		graphUserRepo.saveRelationship(productViewRelationship);
	}
}

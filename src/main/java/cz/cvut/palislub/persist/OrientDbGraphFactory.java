package cz.cvut.palislub.persist;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

/**
 * Created by lubos on 21.3.2015.
 */
public class OrientDbGraphFactory {

	private OrientGraphFactory factory;

	private OrientGraph graphTx = null;

	private OrientGraphNoTx graphNoTx = null;

	public void setFactory(OrientGraphFactory factory) {
		this.factory = factory;
	}

	public OrientGraph getGraphTx() {
		if (graphTx == null) {
			graphTx = factory.getTx();
		}
		return graphTx;
	}

	public OrientGraphNoTx getGraphNoTx() {
		if (graphNoTx == null) {
			graphNoTx = factory.getNoTx();
		}
		return graphNoTx;
	}
}

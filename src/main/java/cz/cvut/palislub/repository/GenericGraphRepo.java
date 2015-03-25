package cz.cvut.palislub.repository;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import cz.cvut.palislub.persist.OrientDbPersister;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * User: L
 * Date: 16. 2. 2015
 */
public abstract class GenericGraphRepo<T, ID> {

	@Autowired
	private OrientDbPersister persister;

	private Class<T> clazz;

	public GenericGraphRepo(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T save(T entity) {
		T t = (T) persister.save(entity);
		return t;
	}

	public Iterable<T> save(Iterable<T> entities) {
		for (T entity : entities) {
			save(entity);
		}
		return entities;
	}

	public void saveRelationship(Object entity) {
		persister.save(entity);
	}

	public OrientGraph getGraph() {
		return persister.getGraph();
	}

	public List<ID> listVertexIds() {
		return persister.listVertexIds(clazz);
	}

	public Iterable<Vertex> getVertices() {
		return persister.listVertices(clazz);
	}

	public List<ID> getIdsOfVertexByProperty(String propertyName, Object value) {
		return persister.getIdsOfVertexByProperty(clazz, propertyName, value);
	}

	public T get(ID id) {
		return (T) persister.get(clazz, id);
	}

	public Vertex getVertex(ID id) {
		return persister.getVertex(clazz, id);
	}

	public void delete(ID id) {
		persister.delete(clazz, id);
	}

	public void delete(Iterable<ID> ids) {
		for (ID o : ids) {
			delete(o);
		}
	}

	public long count() {
		return persister.count(clazz);
	}

	public void clearDatabase() {
		persister.clearDatabase();
	}

}

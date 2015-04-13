package cz.cvut.palislub.repository;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import cz.cvut.palislub.persist.OrientDbPersister;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * User: Lubos Palisek
 * Date: 16. 2. 2015
 */
public abstract class GenericGraphRepo<T, ID> {

	@Autowired
	private OrientDbPersister persister;

	private Class<T> clazz;

	public GenericGraphRepo(Class<T> clazz) {
		this.clazz = clazz;
	}

	public void setPersister(OrientDbPersister persister) {
		this.persister = persister;
	}

	public OrientGraph getTxGraph() {
		return persister.getTxGraph();
	}

	public T save(T entity) {
		T t = (T) persister.save(entity);
		return t;
	}

	public Iterable<T> save(Iterable<T> entities) {
		persister.saveAll((Iterable<Object>) entities);
		return entities;
	}

	public Object saveRelationship(Object entity) {
		return persister.save(entity);
	}

	public Iterable<Object> saveRelationships(Iterable<Object> entities) {
		persister.saveAll(entities);
		return entities;
	}

	public List<ID> listVertexIds() {
		return persister.listVertexIds(clazz);
	}

	public Vertex getVertex(ID id) {
		return persister.getVertex(clazz, id);
	}

	public Iterable<Vertex> getVertices() {
		return persister.listVertices(clazz);
	}

	public Iterable<Vertex> getVertices(String[] keys, Object[] values) {
		return persister.listVertices(clazz, keys, values);
	}

	public List<ID> getIdsOfVertexByProperty(String propertyName, Object value) {
		return persister.getIdsOfVertexByProperty(clazz, propertyName, value);
	}

	public T get(ID id) {
		return (T) persister.get(clazz, id);
	}

	public T getByRid(String rid) {
		return (T) persister.getByRid(clazz, rid);
	}

	public void setProperty(Element element, String propertyName, Object propertyValue) {
		persister.setProperty(element, propertyName, propertyValue);
	}

	public Object getProperty(Element element, String propertyName) {
		return persister.getProperty(element, propertyName);
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

	public void removeAllNodes() {
		persister.removeAllNodes(clazz);
	}

	public void clearDatabase() {
		persister.clearDatabase();
	}

}

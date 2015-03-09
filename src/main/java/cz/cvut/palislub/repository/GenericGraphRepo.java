package cz.cvut.palislub.repository;

import cz.cvut.palislub.persist.OrientDbPersister;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * User: L
 * Date: 16. 2. 2015
 */
public abstract class GenericGraphRepo<T> {

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

	public void saveRelationship(Object entity) {
		persister.saveRelationship(entity);
	}

	public Iterable<T> save(Iterable<T> entities) {
		for (T entity : entities) {
			save(entity);
		}
		return entities;
	}

	public List<Object> listVertexIds() {
		return persister.listVertices(clazz);
	}

	public void delete(List<Object> ids) {
		for (Object o : ids) {
			delete(o);
		}
	}

	public void delete(Object id) {
		persister.delete(clazz, id);
	}

	public long count() {
		return persister.count(clazz);
	}

	public void clearDatabase() {
		persister.clearDatabase();
	}
}

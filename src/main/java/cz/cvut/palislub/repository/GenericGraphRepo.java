package cz.cvut.palislub.repository;

import cz.cvut.palislub.persist.OrientDbPersister;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: L
 * Date: 16. 2. 2015
 */
public class GenericGraphRepo<T> {

	@Autowired
	private OrientDbPersister persister;

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

	public long count(Class clazz) {
		return persister.count(clazz);
	}

	public void clearDatabase() {
		persister.clearDatabase();
	}

	public void loadTest() {
		persister.loadTest();
	}
}

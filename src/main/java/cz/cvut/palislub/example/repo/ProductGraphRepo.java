package cz.cvut.palislub.example.repo;

import cz.cvut.palislub.example.domain.Product;
import cz.cvut.palislub.repository.GenericGraphRepo;
import org.springframework.stereotype.Repository;

/**
 * User: L
 * Date: 10. 3. 2015
 */
@Repository
public class ProductGraphRepo extends GenericGraphRepo<Product, Long> {

	public ProductGraphRepo() {
		super(Product.class);
	}
}

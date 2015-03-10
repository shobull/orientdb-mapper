package cz.cvut.palislub.example.repo;

import cz.cvut.palislub.example.domain.Buying;
import cz.cvut.palislub.example.domain.User;
import cz.cvut.palislub.repository.GenericGraphRepo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: L
 * Date: 18. 2. 2015
 */
@Repository
public class UserGraphRepo extends GenericGraphRepo<User, String> {

	public UserGraphRepo() {
		super(User.class);
	}

	public void saveBuyings(List<Buying> buyings) {
		for (Buying b : buyings) {
			saveRelationship(b);
		}
	}

}

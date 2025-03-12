package purihuaman.util;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import purihuaman.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserSpecification {
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String EMAIL = "email";
	public static final String USERNAME = "username";
	private static final Set<String> ALLOWED_KEY = Set.of(FIRST_NAME, LAST_NAME, EMAIL, USERNAME);

	public static Specification<UserEntity> filterUsers(Map<String, String> valuesToFilter) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			valuesToFilter.forEach((key, value) -> {
				if (!ALLOWED_KEY.contains(key)) return;
				if (value == null || value.trim().isEmpty()) return;

				switch (key) {
					case FIRST_NAME:
						String firstName = "%" + value.trim().toLowerCase() + "%";
						predicates.add(cb.like(cb.lower(root.get(FIRST_NAME)), firstName));
						break;
					case LAST_NAME:
						String lastName = "%" + value.trim().toLowerCase() + "%";
						predicates.add(cb.like(cb.lower(root.get(LAST_NAME)), lastName));
						break;
					case EMAIL:
						String email = "%" + value.trim().toLowerCase() + "%";
						predicates.add(cb.like(cb.lower(root.get(EMAIL)), email));
						break;
					case USERNAME:
						String username = "%" + value.trim().toLowerCase() + "%";
						predicates.add(cb.like(cb.lower(root.get(USERNAME)), username));
						break;
				}
			});

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}

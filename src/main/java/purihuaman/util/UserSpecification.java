package purihuaman.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import purihuaman.entity.UserEntity;

public class UserSpecification {
	private static final Set<String> ALLOWED_KEY = Set.of("first_name", "last_price", "email", "username");

	public static Specification<UserEntity> filterUsers(Map<String, String> valuesToFilter) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			valuesToFilter.forEach((key, value) -> {
				if (!ALLOWED_KEY.contains(key))
					return;
				if (value == null || value.trim().isEmpty())
					return;

				switch (key) {
				case "first_name":
					predicates.add(criteriaBuilder.like(root.get("first_name"), "%" + value + ""));
					break;
				}
			});

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}

package purihuaman.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import purihuaman.entity.ProductEntity;

public class ProductSpecification {
	public static final String PRODUCT_NAME = "product_name";
	public static final String MIN_PRICE = "min_price";
	public static final String MAX_PRICE = "max_price";
	public static final String NEW_PRODUCT = "new_product";
	public static final String CATEGORY_NAME = "category_name";

	private static final Set<String> ALLOWED_KEY = Set.of(PRODUCT_NAME, MIN_PRICE, MAX_PRICE, NEW_PRODUCT,
			CATEGORY_NAME);

	public static Specification<ProductEntity> filterProducts(Map<String, String> valuesToFilter) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			valuesToFilter.forEach((key, value) -> {
				if (!ALLOWED_KEY.contains(key))
					return;
				if (value == null || value.trim().isEmpty())
					return;

				switch (key) {
				case PRODUCT_NAME:
					String searchTerm = "%" + value.toLowerCase() + "%";
					predicates.add(cb.like(cb.lower(root.get(PRODUCT_NAME)), searchTerm));
					break;
				case MIN_PRICE:
					double minPrice = Double.parseDouble(value);
					predicates.add(cb.greaterThanOrEqualTo(root.get(MIN_PRICE), minPrice));
					break;
				case MAX_PRICE:
					double maxPrice = Double.parseDouble(value);
					predicates.add(cb.lessThanOrEqualTo(root.get(MAX_PRICE), maxPrice));
					break;
				case NEW_PRODUCT:
					int intValue = Integer.parseInt(value);
					predicates.add(cb.equal(root.get(NEW_PRODUCT), intValue));
					break;
				case CATEGORY_NAME:
					predicates.add(cb.equal(root.get(CATEGORY_NAME), value));
					break;
				}

			});
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}

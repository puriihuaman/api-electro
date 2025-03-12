package purihuaman.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum RoleType {
	ADMIN("Admin"), USER("User"), INVITED("Invited");

	private final String value;

	RoleType(String roleName) {
		this.value = roleName;
	}

	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static RoleType fromString(String value) {
		if (value == null || value.isBlank()) throw new IllegalArgumentException(
			"Role cannot be null or empty. Valid roles: " + getValidRoleNames());

		for (RoleType role : RoleType.values()) {
			if (role.getRoleName().equalsIgnoreCase(value)) return role;
		}
		throw new IllegalArgumentException("Invalid role: " + value);
	}

	@JsonValue
	public String getRoleName() {
		return value;
	}

	private static String getValidRoleNames() {
		return Arrays.stream(values()).map(RoleType::getRoleName).collect(Collectors.joining(", "));
	}
}

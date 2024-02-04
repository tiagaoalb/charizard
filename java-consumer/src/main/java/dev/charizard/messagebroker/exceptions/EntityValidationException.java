package dev.charizard.messagebroker.exceptions;

import java.util.Set;

public class EntityValidationException extends RuntimeException {
	private final Set<String> validationErrors;

	public EntityValidationException(Set<String> validationErrors) {
		super("Entity Validation Error:" + convertSetToString(validationErrors));
		this.validationErrors = validationErrors;
	}

	public static String convertSetToString(Set<String> set) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String str : set) {
			if (!stringBuilder.isEmpty()) {
				stringBuilder.append(", ");
			}
			stringBuilder.append(str);
		}
		return stringBuilder.toString();
	}

	//if necessary send to Observability tool
	public Set<String> getValidationErrors() {
		return validationErrors;
	}

}

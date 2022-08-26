package com.bloomreach.brcms.jcr.node;

import com.bloomreach.brcms.client.NodeForm;
import com.bloomreach.brcms.client.NodePropertyForm;
import com.bloomreach.brcms.client.PropertyType;
import com.github.f4b6a3.ulid.Ulid;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ResponseStatusException;

final class ValidationUtils {

  private ValidationUtils() {
    // Utility class constructor
  }

  static @NonNull NodeForm validateAndMapPropertyValues(final @NonNull NodeForm form) {

    Optional.ofNullable(form.getProperties())
        .orElse(List.of())
        .forEach(ValidationUtils::validateAndMapPropertyValue);

    return form;
  }

  static @NonNull Ulid validateUlid(final @NonNull String id) {

    try {
      return Ulid.from(id);
    } catch (final IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Id value: " + id);
    }
  }

  private static void validateAndMapPropertyValue(final @NonNull NodePropertyForm propertyForm) {

    final String propertyKey = propertyForm.getKey();
    final Object propertyValue = propertyForm.getValue();

    if (propertyValue == null) {
      return;
    }

    final PropertyType propertyType = propertyForm.getType();

    if (PropertyType.BOOLEAN == propertyType) {
      validateBoolean(propertyKey, propertyValue);
    } else if (PropertyType.NUMBER == propertyType) {
      validateNumber(propertyKey, propertyValue);
    } else if (PropertyType.TIMESTAMP == propertyType) {
      validateTimestamp(propertyKey, propertyValue);
    } else if (PropertyType.ARRAY == propertyType) {
      validateArray(propertyKey, propertyValue);
    }
  }

  private static void validateArray(
      final @NonNull String propertyKey, final @NonNull Object propertyValue) {

    if (!(propertyValue instanceof List)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, propertyKey + " is not a valid array");
    }
  }

  private static void validateBoolean(
      final @NonNull String propertyKey, final @NonNull Object propertyValue) {

    if (!("true".equalsIgnoreCase(propertyValue.toString())
        || "false".equalsIgnoreCase(propertyValue.toString()))) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, propertyKey + " is not a valid boolean");
    }
  }

  private static void validateNumber(
      final @NonNull String propertyKey, final @NonNull Object propertyValue) {
    try {
      Double.parseDouble(propertyValue.toString());
    } catch (final Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, propertyKey + " is not a valid number");
    }
  }

  private static void validateTimestamp(
      final @NonNull String propertyKey, final @NonNull Object propertyValue) {
    try {
      Instant.parse(propertyValue.toString());
    } catch (final Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, propertyKey + " is not a valid timestamp");
    }
  }
}

package com.bloomreach.brcms.jcr.node;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

  @ExceptionHandler(value = ConstraintViolationException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public String handleResourceNotFoundException(
      final @NonNull ConstraintViolationException exception) {

    final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
    final StringBuilder strBuilder = new StringBuilder();

    for (final ConstraintViolation<?> violation : violations) {
      strBuilder.append(violation.getMessage()).append("\n");
    }

    return strBuilder.toString();
  }
}

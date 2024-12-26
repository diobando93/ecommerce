package com.eCommerce.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final HttpStatus  status;

  /**
   * Constructs an ApiException with a custom message and HTTP status.
   *
   * @param message
   *          the error message
   * @param status
   *          the HTTP status code
   */
  public ApiException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  /**
   * Gets the HTTP status of the exception.
   *
   * @return the {@link HttpStatus}
   */
  public HttpStatus getStatus() {
    return status;
  }

}

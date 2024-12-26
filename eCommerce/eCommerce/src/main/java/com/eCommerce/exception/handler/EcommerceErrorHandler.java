package com.eCommerce.exception.handler;

import java.time.LocalDateTime;
import com.eCommerce.exception.ApiException;
import com.eCommerce.exception.DefaultErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class EcommerceErrorHandler {

  /**
   * Handles ApiException and returns a structured error response.
   * 
   * @param ex
   *          the ApiException containing the error details.
   * @param request
   *          the WebRequest object containing request details.
   * @return a {@link ResponseEntity} containing the structured error response.
   */
  @ExceptionHandler(ApiException.class)
  public ResponseEntity<DefaultErrorDto> handleApiException(ApiException ex, WebRequest request) {
    DefaultErrorDto errorDTO = new DefaultErrorDto();
    errorDTO.setTimestamp(LocalDateTime.now().toString());
    errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
    errorDTO.setError(ex.getStatus().getReasonPhrase());
    errorDTO.setMessage(ex.getMessage());
    errorDTO.setPath(request.getDescription(false));

    return new ResponseEntity<>(errorDTO, ex.getStatus());
  }
}

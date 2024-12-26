package com.eCommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultErrorDto {

  private String timestamp;
  private String status;
  private String error;
  private String message;
  private String path;

}

package com.straysafe.backend.advice;

import com.straysafe.backend.api.model.exception.AppException;
import com.straysafe.backend.api.model.exception.AuthenticationException;
import com.straysafe.backend.api.model.exception.BreedNotFoundException;
import com.straysafe.backend.api.model.exception.ImageSaveException;
import com.straysafe.backend.api.model.exception.PetCreateException;
import com.straysafe.backend.api.model.exception.PetDeleteException;
import com.straysafe.backend.api.model.exception.PetGetException;
import com.straysafe.backend.api.model.exception.PetTypeNotFoundException;
import com.straysafe.backend.api.model.exception.ReportCreationException;
import com.straysafe.backend.api.model.exception.ReportNotFoundException;
import com.straysafe.backend.api.model.exception.ReportStatusNotFoundException;
import com.straysafe.backend.api.model.exception.UserProfileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }
    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.put(error.getObjectName(), error.getDefaultMessage());
    }
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    logger.error(e.getMessage(), e);
    Throwable mostSpecificCause = e.getMostSpecificCause();

    String detailedMessage = mostSpecificCause.getMessage();
    String errorMessage = extractEnumErrorMessage(detailedMessage);

    return ResponseEntity.badRequest().body(errorMessage);
  }

  @ExceptionHandler(PetTypeNotFoundException.class)
  public ResponseEntity<String> handlePetTypeNotFound(PetTypeNotFoundException ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserProfileException.class)
  public ResponseEntity<String> handleUserProfileException(UserProfileException ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BreedNotFoundException.class)
  public ResponseEntity<String> handleBreedNotFound(BreedNotFoundException ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ReportStatusNotFoundException.class)
  public ResponseEntity<String> handleReportStatusNotFound(ReportStatusNotFoundException ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ReportCreationException.class)
  public ResponseEntity<String> handleReportCreation(ReportCreationException ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(AppException.class)
  public ResponseEntity<String> handleException(AppException ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.IM_USED);
  }

  @ExceptionHandler(SQLException.class)
  public ResponseEntity<String> handleSQLException(SQLException ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ReportNotFoundException.class)
  public ResponseEntity<String> handleException(ReportNotFoundException ex){
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<String> handleException(AuthenticationException ex){
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
  }
  @ExceptionHandler(PetDeleteException.class)
  public ResponseEntity<String> handleException(PetDeleteException ex){
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  @ExceptionHandler(PetCreateException.class)
  public ResponseEntity<String> handleException(PetCreateException ex){
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  @ExceptionHandler(ImageSaveException.class)
  public ResponseEntity<String> handleException(ImageSaveException ex){
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  @ExceptionHandler(PetGetException.class)
  public ResponseEntity<String> handleException(PetGetException ex){
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private String extractEnumErrorMessage(String detailedMessage) {
    if (detailedMessage.contains("Cannot deserialize value of type")) {

      int start = detailedMessage.indexOf("from String");
      int end = detailedMessage.indexOf(" at [Source");
      if (start != -1 && end != -1 && start < end) {
        return detailedMessage.substring(start, end).trim();
      }
    }
    return "Invalid request format or incompatible data type. Please review your JSON request.";
  }

}

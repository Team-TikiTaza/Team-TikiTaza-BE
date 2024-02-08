package com.pgms.apigame.exception;

import static com.pgms.coredomain.domain.common.GameErrorCode.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pgms.coredomain.domain.common.GameErrorCode;
import com.pgms.coredomain.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GameGlobalExceptionHandler {

	@ExceptionHandler(GameException.class)
	protected ResponseEntity<ErrorResponse> handleGameCustomException(GameException ex) {
		log.warn(">>>>> Game Custom Exception : ", ex);
		GameErrorCode errorCode = ex.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode.getCode(), errorCode.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		log.warn(">>>>> Validation Failed : ", ex);
		BindingResult bindingResult = ex.getBindingResult();
		String errorMessage = VALIDATION_FAILED.getMessage();

		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		ErrorResponse errorResponse = ErrorResponse.of(VALIDATION_FAILED.getCode(), errorMessage);
		fieldErrors.forEach(error -> errorResponse.addValidation(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
		log.error(">>>>> Internal Server Error : ", ex);
		ErrorResponse errorResponse = ErrorResponse.of("game-500/01", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}

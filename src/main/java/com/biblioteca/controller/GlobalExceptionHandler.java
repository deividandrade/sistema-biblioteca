package com.biblioteca.controller;

import com.biblioteca.dto.BibliotecaDTO.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Tratamento global de exceções. Captura erros lançados em qualquer Controller
 * e retorna resposta formatada.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	// captura erros de validação do Bean Validation (@NotBlank, @NotNull, etc.)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleValidationErrors(MethodArgumentNotValidException ex) {
		// junta todas as mensagens de erro em uma só string
		String mensagem = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.joining(", "));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ApiResponseDTO.erro("Erro de validação: " + mensagem));
	}

	// Captura exceções genéricas de negócio (RuntimeException)
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleRuntimeException(RuntimeException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.erro(ex.getMessage()));
	}

	// Captura qualquer outra exceção não tratada
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleGenericException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ApiResponseDTO.erro("Erro interno do servidor. Tente novamente mais tarde."));
	}
}

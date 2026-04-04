package com.biblioteca.controller;

import com.biblioteca.dto.BibliotecaDTO.ApiResponseDTO;
import com.biblioteca.dto.BibliotecaDTO.EmprestimoRequestDTO;
import com.biblioteca.dto.BibliotecaDTO.EmprestimoResponseDTO;
import com.biblioteca.service.EmprestimoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de Empréstimos.
 */
@RestController
@RequestMapping("/api/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

	private final EmprestimoService emprestimoService;

	// GET /api/emprestimos
	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<EmprestimoResponseDTO>>> listarTodos() {
		return ResponseEntity.ok(ApiResponseDTO.sucesso("Empréstimos encontrados", emprestimoService.listarTodos()));
	}

	// GET /api/emprestimos/{id}
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<EmprestimoResponseDTO>> buscarPorId(@PathVariable Long id) {
		try {
			return ResponseEntity
					.ok(ApiResponseDTO.sucesso("Empréstimo encontrado", emprestimoService.buscarPorId(id)));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDTO.erro(e.getMessage()));
		}
	}

	// GET /api/emprestimos/ativos
	@GetMapping("/ativos")
	public ResponseEntity<ApiResponseDTO<List<EmprestimoResponseDTO>>> listarAtivos() {
		return ResponseEntity.ok(ApiResponseDTO.sucesso("Empréstimos ativos", emprestimoService.listarAtivos()));
	}

	// GET /api/emprestimos/atrasados
	@GetMapping("/atrasados")
	public ResponseEntity<ApiResponseDTO<List<EmprestimoResponseDTO>>> listarAtrasados() {
		return ResponseEntity.ok(ApiResponseDTO.sucesso("Empréstimos atrasados", emprestimoService.listarAtrasados()));
	}

	// POST /api/emprestimos - Realiza um novo empréstimo
	@PostMapping
	public ResponseEntity<ApiResponseDTO<EmprestimoResponseDTO>> realizarEmprestimo(
			@Valid @RequestBody EmprestimoRequestDTO dto) {
		try {
			EmprestimoResponseDTO emprestimo = emprestimoService.realizarEmprestimo(dto);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(ApiResponseDTO.sucesso("Empréstimo realizado com sucesso", emprestimo));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.erro(e.getMessage()));
		}
	}

	// PATCH /api/emprestimos/{id}/devolver - Realiza a devolução
	// Vou usar PATCH porque é uma atualização parcial do recurso
	@PatchMapping("/{id}/devolver")
	public ResponseEntity<ApiResponseDTO<EmprestimoResponseDTO>> realizarDevolucao(@PathVariable Long id) {
		try {
			EmprestimoResponseDTO emprestimo = emprestimoService.realizarDevolucao(id);
			return ResponseEntity.ok(ApiResponseDTO.sucesso("Devolução realizada com sucesso", emprestimo));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.erro(e.getMessage()));
		}
	}
}
package com.biblioteca.controller;

import com.biblioteca.dto.BibliotecaDTO.ApiResponseDTO;
import com.biblioteca.dto.BibliotecaDTO.LivroRequestDTO;
import com.biblioteca.dto.BibliotecaDTO.LivroResponseDTO;
import com.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de Livros.
 */
@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
public class LivroController {

	private final LivroService livroService;

	// GET /api/livros
	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<LivroResponseDTO>>> listarTodos() {
		return ResponseEntity.ok(ApiResponseDTO.sucesso("Livros encontrados", livroService.listarTodos()));
	}

	// GET /api/livros/{id}
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<LivroResponseDTO>> buscarPorId(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(ApiResponseDTO.sucesso("Livro encontrado", livroService.buscarPorId(id)));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDTO.erro(e.getMessage()));
		}
	}

	// GET /api/livros/disponiveis
	@GetMapping("/disponiveis")
	public ResponseEntity<ApiResponseDTO<List<LivroResponseDTO>>> listarDisponiveis() {
		return ResponseEntity.ok(ApiResponseDTO.sucesso("Livros disponíveis", livroService.listarDisponiveis()));
	}

	// GET /api/livros/buscar?titulo=xxx
	@GetMapping("/buscar")
	public ResponseEntity<ApiResponseDTO<List<LivroResponseDTO>>> buscarPorTitulo(@RequestParam String titulo) {
		return ResponseEntity.ok(ApiResponseDTO.sucesso("Resultado da busca", livroService.buscarPorTitulo(titulo)));
	}

	// POST /api/livros
	@PostMapping
	public ResponseEntity<ApiResponseDTO<LivroResponseDTO>> criar(@Valid @RequestBody LivroRequestDTO dto) {
		try {
			LivroResponseDTO livro = livroService.criar(dto);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(ApiResponseDTO.sucesso("Livro criado com sucesso", livro));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.erro(e.getMessage()));
		}
	}

	// PUT /api/livros/{id}
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<LivroResponseDTO>> atualizar(@PathVariable Long id,
			@Valid @RequestBody LivroRequestDTO dto) {
		try {
			return ResponseEntity.ok(ApiResponseDTO.sucesso("Livro atualizado", livroService.atualizar(id, dto)));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.erro(e.getMessage()));
		}
	}

	// DELETE /api/livros/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<Void>> deletar(@PathVariable Long id) {
		try {
			livroService.deletar(id);
			return ResponseEntity.ok(ApiResponseDTO.sucesso("Livro excluído com sucesso", null));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.erro(e.getMessage()));
		}
	}
}
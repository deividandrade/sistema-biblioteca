package com.biblioteca.controller;

import com.biblioteca.dto.BibliotecaDTO.ApiResponseDTO;
import com.biblioteca.dto.BibliotecaDTO.AutorRequestDTO;
import com.biblioteca.dto.BibliotecaDTO.AutorResponseDTO;
import com.biblioteca.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de Autores. Endpoints da API - retornam
 * JSON.
 */
@RestController
@RequestMapping("/api/autores")
@RequiredArgsConstructor
public class AutorController {

	private final AutorService autorService;

	// GET /api/autores - Lista todos os autores
	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<AutorResponseDTO>>> listarTodos() {
		List<AutorResponseDTO> autores = autorService.listarTodos();
		return ResponseEntity.ok(ApiResponseDTO.sucesso("Autores encontrados", autores));
	}

	// GET /api/autores/{id} - Busca autor por ID
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<AutorResponseDTO>> buscarPorId(@PathVariable Long id) {
		try {
			AutorResponseDTO autor = autorService.buscarPorId(id);
			return ResponseEntity.ok(ApiResponseDTO.sucesso("Autor encontrado", autor));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDTO.erro(e.getMessage()));
		}
	}

	// GET /api/autores/buscar?nome=xxx - Busca por nome
	@GetMapping("/buscar")
	public ResponseEntity<ApiResponseDTO<List<AutorResponseDTO>>> buscarPorNome(@RequestParam String nome) {
		List<AutorResponseDTO> autores = autorService.buscarPorNome(nome);
		return ResponseEntity.ok(ApiResponseDTO.sucesso("Resultado da busca", autores));
	}

	// POST /api/autores - Cria novo autor
	@PostMapping
	public ResponseEntity<ApiResponseDTO<AutorResponseDTO>> criar(@Valid @RequestBody AutorRequestDTO dto) {
		try {
			AutorResponseDTO autorCriado = autorService.criar(dto);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(ApiResponseDTO.sucesso("Autor criado com sucesso", autorCriado));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.erro(e.getMessage()));
		}
	}

	// PUT /api/autores/{id} - Atualiza autor
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<AutorResponseDTO>> atualizar(@PathVariable Long id,
			@Valid @RequestBody AutorRequestDTO dto) {
		try {
			AutorResponseDTO autorAtualizado = autorService.atualizar(id, dto);
			return ResponseEntity.ok(ApiResponseDTO.sucesso("Autor atualizado com sucesso", autorAtualizado));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.erro(e.getMessage()));
		}
	}

	// DELETE /api/autores/{id} - Deleta autor
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<Void>> deletar(@PathVariable Long id) {
		try {
			autorService.deletar(id);
			return ResponseEntity.ok(ApiResponseDTO.sucesso("Autor excluído com sucesso", null));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.erro(e.getMessage()));
		}
	}
}

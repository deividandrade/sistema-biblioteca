package com.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class BibliotecaDTO {

	// DTOs de AUTOR

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AutorRequestDTO {
		@NotBlank(message = "O nome é obrigatório")
		private String nome;
		private String biografia;
		private String nacionalidade;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AutorResponseDTO {
		private Long id;
		private String nome;
		private String biografia;
		private String nacionalidade;
		private int totalLivros;
	}

	// DTOs de LIVRO

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LivroRequestDTO {
		@NotBlank(message = "O título é obrigatório")
		private String titulo;
		private String isbn;
		private Integer anoPublicacao;
		private String editora;
		@NotNull(message = "O autor é obrigatório")
		private Long autorId;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LivroResponseDTO {
		private Long id;
		private String titulo;
		private String isbn;
		private Integer anoPublicacao;
		private String editora;
		private Boolean disponivel;
		private String nomeAutor;
		private Long autorId;
	}

	// DTOs de EMPRESTIMO

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EmprestimoRequestDTO {
		@NotBlank(message = "O nome do leitor é obrigatório")
		private String nomeLeitor;
		private String emailLeitor;
		@NotNull(message = "O livro é obrigatório")
		private Long livroId;
		private LocalDate dataDevolucaoPrevista;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EmprestimoResponseDTO {
		private Long id;
		private String nomeLeitor;
		private String emailLeitor;
		private LocalDate dataEmprestimo;
		private LocalDate dataDevolucaoPrevista;
		private LocalDate dataDevolucaoReal;
		private String status;
		private String tituloLivro;
		private Long livroId;
	}

	// DTO de resposta genérica da API

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ApiResponseDTO<T> {
		private boolean sucesso;
		private String mensagem;
		private T dados;

		public static <T> ApiResponseDTO<T> sucesso(String mensagem, T dados) {
			return new ApiResponseDTO<>(true, mensagem, dados);
		}

		public static <T> ApiResponseDTO<T> erro(String mensagem) {
			return new ApiResponseDTO<>(false, mensagem, null);
		}
	}
}

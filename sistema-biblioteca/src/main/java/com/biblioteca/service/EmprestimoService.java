package com.biblioteca.service;

import com.biblioteca.dto.BibliotecaDTO.EmprestimoRequestDTO;
import com.biblioteca.dto.BibliotecaDTO.EmprestimoResponseDTO;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Emprestimo.StatusEmprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

	private final EmprestimoRepository emprestimoRepository;
	private final LivroRepository livroRepository;

	// Lista todos os empréstimos
	public List<EmprestimoResponseDTO> listarTodos() {
		return emprestimoRepository.findAll().stream().map(this::converterParaDTO).collect(Collectors.toList());
	}

	// Busca por ID
	public EmprestimoResponseDTO buscarPorId(Long id) {
		Emprestimo emprestimo = emprestimoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Empréstimo não encontrado com ID: " + id));
		return converterParaDTO(emprestimo);
	}

	// Realiza um novo empréstimo
	@Transactional
	public EmprestimoResponseDTO realizarEmprestimo(EmprestimoRequestDTO dto) {
		// Busca o livro
		Livro livro = livroRepository.findById(dto.getLivroId())
				.orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + dto.getLivroId()));

		// Verifica se o livro está disponível
		if (!livro.getDisponivel()) {
			throw new RuntimeException("O livro '" + livro.getTitulo() + "' não está disponível para empréstimo");
		}

		// Cria o empréstimo
		Emprestimo emprestimo = new Emprestimo();
		emprestimo.setNomeLeitor(dto.getNomeLeitor());
		emprestimo.setEmailLeitor(dto.getEmailLeitor());
		emprestimo.setLivro(livro);
		emprestimo.setDataEmprestimo(LocalDate.now());
		emprestimo.setStatus(StatusEmprestimo.ATIVO);

		// Define a data de devolução prevista (padrão: 14 dias)
		if (dto.getDataDevolucaoPrevista() != null) {
			emprestimo.setDataDevolucaoPrevista(dto.getDataDevolucaoPrevista());
		} else {
			emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(14));
		}

		// Marca o livro como indisponível
		livro.setDisponivel(false);
		livroRepository.save(livro);

		Emprestimo emprestimoCriado = emprestimoRepository.save(emprestimo);
		return converterParaDTO(emprestimoCriado);
	}

	// Realiza a devolução de um livro
	@Transactional
	public EmprestimoResponseDTO realizarDevolucao(Long id) {
		Emprestimo emprestimo = emprestimoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Empréstimo não encontrado com ID: " + id));

		if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
			throw new RuntimeException("Este livro já foi devolvido");
		}

		// Atualiza o empréstimo
		emprestimo.setDataDevolucaoReal(LocalDate.now());
		emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

		// Marca o livro como disponível novamente
		Livro livro = emprestimo.getLivro();
		livro.setDisponivel(true);
		livroRepository.save(livro);

		return converterParaDTO(emprestimoRepository.save(emprestimo));
	}

	// Busca empréstimos ativos
	public List<EmprestimoResponseDTO> listarAtivos() {
		return emprestimoRepository.findByStatus(StatusEmprestimo.ATIVO).stream().map(this::converterParaDTO)
				.collect(Collectors.toList());
	}

	// Busca empréstimos atrasados e atualiza o status deles
	@Transactional
	public List<EmprestimoResponseDTO> listarAtrasados() {
		List<Emprestimo> atrasados = emprestimoRepository.findEmprestimosAtrasados(LocalDate.now());

		// Atualiza o status para ATRASADO
		atrasados.forEach(e -> {
			e.setStatus(StatusEmprestimo.ATRASADO);
			emprestimoRepository.save(e);
		});

		return atrasados.stream().map(this::converterParaDTO).collect(Collectors.toList());
	}

	// Converte entidade para DTO
	private EmprestimoResponseDTO converterParaDTO(Emprestimo emprestimo) {
		EmprestimoResponseDTO dto = new EmprestimoResponseDTO();
		dto.setId(emprestimo.getId());
		dto.setNomeLeitor(emprestimo.getNomeLeitor());
		dto.setEmailLeitor(emprestimo.getEmailLeitor());
		dto.setDataEmprestimo(emprestimo.getDataEmprestimo());
		dto.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista());
		dto.setDataDevolucaoReal(emprestimo.getDataDevolucaoReal());
		dto.setStatus(emprestimo.getStatus().name());
		dto.setTituloLivro(emprestimo.getLivro().getTitulo());
		dto.setLivroId(emprestimo.getLivro().getId());
		return dto;
	}
}

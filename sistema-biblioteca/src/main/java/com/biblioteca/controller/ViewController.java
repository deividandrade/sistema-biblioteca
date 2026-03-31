package com.biblioteca.controller;

import com.biblioteca.service.AutorService;
import com.biblioteca.service.EmprestimoService;
import com.biblioteca.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller para as páginas web (Thymeleaf). Diferente dos RestControllers,
 * este retorna nomes de templates HTML.
 */
@Controller
@RequiredArgsConstructor
public class ViewController {

	private final LivroService livroService;
	private final AutorService autorService;
	private final EmprestimoService emprestimoService;

	// Página inicial com dashboard
	@GetMapping("/")
	public String index(Model model) {
		// Adiciona dados ao modelo para exibir no template
		model.addAttribute("totalLivros", livroService.listarTodos().size());
		model.addAttribute("totalAutores", autorService.listarTodos().size());
		model.addAttribute("livrosDisponiveis", livroService.listarDisponiveis().size());
		model.addAttribute("emprestimosAtivos", emprestimoService.listarAtivos().size());
		return "index"; // Aponta para templates/index.html
	}

	// Página de listagem de livros
	@GetMapping("/livros")
	public String livros(Model model) {
		model.addAttribute("livros", livroService.listarTodos());
		model.addAttribute("autores", autorService.listarTodos());
		return "livros/lista";
	}

	// Página de listagem de autores
	@GetMapping("/autores")
	public String autores(Model model) {
		model.addAttribute("autores", autorService.listarTodos());
		return "autores/lista";
	}

	// Página de listagem de empréstimos
	@GetMapping("/emprestimos")
	public String emprestimos(Model model) {
		model.addAttribute("emprestimos", emprestimoService.listarTodos());
		model.addAttribute("livrosDisponiveis", livroService.listarDisponiveis());
		return "emprestimos/lista";
	}
}
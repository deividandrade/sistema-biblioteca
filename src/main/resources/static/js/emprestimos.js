let idParaDevolucao = null;

async function salvarEmprestimo() {
    const nomeLeitor = document.getElementById('empNomeLeitor').value.trim();
    const livroId = document.getElementById('empLivroId').value;

    if (!nomeLeitor) {
        mostrarAlerta('O nome do leitor é obrigatório!', 'warning');
        return;
    }
    if (!livroId) {
        mostrarAlerta('Selecione um livro!', 'warning');
        return;
    }

    const dados = {
        nomeLeitor: nomeLeitor,
        emailLeitor: document.getElementById('empEmailLeitor').value.trim() || null,
        livroId: parseInt(livroId),
        dataDevolucaoPrevista: document.getElementById('empDataDevolucao').value || null
    };

    try {
        const resposta = await fetch('/api/emprestimos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dados)
        });

        const resultado = await resposta.json();

        bootstrap.Modal.getInstance(document.getElementById('modalEmprestimo')).hide();

        if (resultado.sucesso) {
            mostrarAlerta(resultado.mensagem, 'success');
            recarregarPagina();
        } else {
            mostrarAlerta(resultado.mensagem, 'danger');
        }
    } catch (erro) {
        console.error('Erro ao registrar empréstimo:', erro);
        mostrarAlerta('Erro ao conectar com o servidor.', 'danger');
    }
}

function confirmarDevolucaoDaLinha(btn) {
    const tr = btn.closest('tr');
    idParaDevolucao = tr.dataset.id;
    document.getElementById('nomeLivroDevolucao').textContent = tr.dataset.livro;
    new bootstrap.Modal(document.getElementById('modalDevolucao')).show();
}

async function executarDevolucao() {
    if (!idParaDevolucao) return;

    try {
        const resposta = await fetch(`/api/emprestimos/${idParaDevolucao}/devolver`, {
            method: 'PATCH'
        });

        const resultado = await resposta.json();

        bootstrap.Modal.getInstance(document.getElementById('modalDevolucao')).hide();

        if (resultado.sucesso) {
            mostrarAlerta(resultado.mensagem, 'success');
            recarregarPagina();
        } else {
            mostrarAlerta(resultado.mensagem, 'danger');
        }
    } catch (erro) {
        mostrarAlerta('Erro ao conectar com o servidor.', 'danger');
    }
}

function filtrarTabela() {
    const texto = document.getElementById('campoBusca').value.toLowerCase();
    const filtroStatus = document.getElementById('filtroStatus').value;
    const linhas = document.querySelectorAll('#tabelaEmprestimos tbody tr[data-leitor]');

    linhas.forEach(linha => {
        const leitor = (linha.dataset.leitor || '').toLowerCase();
        const livro = (linha.dataset.livro || '').toLowerCase();
        const status = linha.dataset.status || '';

        const combinaTexto = leitor.includes(texto) || livro.includes(texto);
        const combinaStatus = filtroStatus === '' || status === filtroStatus;

        linha.style.display = (combinaTexto && combinaStatus) ? '' : 'none';
    });
}

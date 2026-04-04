/**
 * Exibe um alerta na tela
 * @param {string} mensagem - Texto do alerta
 * @param {string} tipo - 'success', 'danger', 'warning', 'info'
 */
function mostrarAlerta(mensagem, tipo = 'success') {
    const alertBox = document.getElementById('alertBox');
    const alertMessage = document.getElementById('alertMessage');

    if (!alertBox || !alertMessage) return;

    // Remove classes anteriores de tipo
    alertBox.classList.remove('alert-success', 'alert-danger', 'alert-warning', 'alert-info');
    alertBox.classList.add('alert-' + tipo);
    alertBox.classList.remove('d-none');

    alertMessage.innerHTML = mensagem;

    // Fecha o alerta automaticamente após 4 segundos
    setTimeout(() => {
        alertBox.classList.add('d-none');
    }, 4000);

    // Rola a tela para o topo para mostrar o alerta
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

/**
 * Recarrega a página atual
 */
function recarregarPagina() {
    setTimeout(() => window.location.reload(), 800);
}

/**
 * Formata uma data ISO para o padrão brasileiro
 * @param {string} dataISO - Data no formato YYYY-MM-DD
 * @returns {string} Data no formato DD/MM/YYYY
 */
function formatarData(dataISO) {
    if (!dataISO) return '-';
    const [ano, mes, dia] = dataISO.split('-');
    return `${dia}/${mes}/${ano}`;
}

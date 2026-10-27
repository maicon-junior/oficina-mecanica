// Tela de Ordem de Serviço: adicionar/remover serviços e calcular o total na tela.
// O valor gravado é sempre calculado de novo no servidor (OrdemServicoService).
(function () {
    const tabela = document.getElementById("itens");
    const modelo = document.getElementById("modelo-item");
    const total = document.getElementById("total");
    if (!tabela || !modelo) {
        return;
    }
    const moeda = new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" });

    // Itens que já estavam na OS mantêm o preço cobrado na época
    function precoDaLinha(linha) {
        const select = linha.querySelector("select");
        if (select.value && select.value === linha.dataset.servicoOriginal && linha.dataset.valorOriginal) {
            return Number(linha.dataset.valorOriginal);
        }
        const opcao = select.selectedOptions[0];
        return opcao && opcao.dataset.valor ? Number(opcao.dataset.valor) : 0;
    }

    function atualizar() {
        let soma = 0;
        tabela.querySelectorAll("tr.item").forEach(function (linha, indice) {
            const preco = precoDaLinha(linha);
            const quantidade = Number(linha.querySelector("input").value) || 0;
            const subtotal = preco * quantidade;
            soma += subtotal;
            linha.querySelector(".preco").textContent = preco ? moeda.format(preco) : "—";
            linha.querySelector(".subtotal").textContent = preco ? moeda.format(subtotal) : "—";
            // Reindexa os nomes para o Spring montar a lista itens[0], itens[1]...
            linha.querySelector("select").name = "itens[" + indice + "].servico.id";
            linha.querySelector("input").name = "itens[" + indice + "].quantidade";
        });
        total.textContent = moeda.format(soma);
    }

    document.getElementById("adicionar-item").addEventListener("click", function () {
        const linha = modelo.content.firstElementChild.cloneNode(true);
        tabela.appendChild(linha);
        linha.querySelector("select").focus();
        atualizar();
    });

    tabela.addEventListener("click", function (evento) {
        const botao = evento.target.closest(".remover");
        if (botao) {
            botao.closest("tr").remove();
            atualizar();
        }
    });

    tabela.addEventListener("input", atualizar);
    tabela.addEventListener("change", atualizar);

    if (!tabela.querySelector("tr.item")) {
        tabela.appendChild(modelo.content.firstElementChild.cloneNode(true));
    }
    atualizar();
})();

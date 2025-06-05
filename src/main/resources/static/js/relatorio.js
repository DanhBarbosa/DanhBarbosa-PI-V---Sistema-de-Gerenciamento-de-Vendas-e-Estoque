let produto = "";

document.getElementById("product").addEventListener("change", function () {
    const selectedOption = this.options[this.selectedIndex];

    const nome = selectedOption.textContent;
    produto = nome;
    console.log("Produto selecionado:", nome);

});

async function gerarRelatorio() {
    const token = localStorage.getItem("token");

    const dataInicio = document.getElementById("startDate").value;
    const dataFinal = document.getElementById("endDate").value;

    const resposta = await fetch(`/api/vendas/exportar?inicio=${dataInicio}&fim=${dataFinal}&produto=${produto}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    const blob = await resposta.blob();
    const url = window.URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.download = "vendas.xlsx";
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url);
}


async function carregarFuncionarios() {
    const resposta = await fetch('/api/usuarios/funcionarios', {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`
        }
    });

    if (resposta.ok) {
        const funcionarios = await resposta.json();
        const select = document.getElementById("employee");
        select.innerHTML = '<option value="">Todos</option>'; // opção padrão

        funcionarios.forEach(func => {
            const option = document.createElement("option");
            option.value = func.id;
            option.textContent = func.nome;
            select.appendChild(option);
        });
    } else {
        alert('Erro ao carregar funcionários');
    }
}


async function carregarProdutos() {
    const resposta = await fetch('/api/produtos/disponiveis', {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`
        }
    });

    if (resposta.ok) {
        const produtos = await resposta.json();
        const select = document.getElementById("product");
        select.innerHTML = '<option value="">Todos</option>'; // opção padrão

        produtos.forEach(prod => {
            const option = document.createElement("option");
            option.value = prod.id;
            option.textContent = prod.nome;
            select.appendChild(option);
        });
    } else {
        alert('Erro ao carregar produtos');
    }
}


async function carregarVendas() {

    const token = localStorage.getItem("token");

    const resposta = await fetch('/api/vendas', {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    });

    if (!resposta.ok) {
        throw new Error('Erro ao carregar os dados da API.');
        return;
    }
    const dados = await resposta.json();


    const tabelaCorpo = document.getElementById('controleVendasLoja');

    tabelaCorpo.innerHTML = '';

    if (dados && dados.length > 0) {
        dados.forEach(item => {
            const linha = document.createElement('tr');


            const colunas = [
                item.idVenda,
                item.nomeFuncionario,
                new Date(item.dataVenda).toLocaleString(),
                item.nomeProduto || '-',
                item.quantidade,
                item.valorVenda.toFixed(2),
                item.metodoPagamento || '-'

            ];

            colunas.forEach(dado => {
                const celula = document.createElement('td');
                celula.textContent = dado;
                linha.appendChild(celula);
            });

            tabelaCorpo.appendChild(linha);
        });
    } else {
        tabelaCorpo.innerHTML = '<tr><td colspan="10">Nenhum item encontrado.</td></tr>';
    }

}


window.onload = function () {
    carregarFuncionarios();
    carregarProdutos();
    carregarVendas();

};


// Elementos do DOM
const contaSelect = document.getElementById("contaSelect");
const saldoDisplay = document.getElementById("saldoDisplay");
const renderBtn = document.getElementById("renderBtn");

let contasMap = {}; // armazenar saldo e tipo para cada conta

// Carregar todas as contas
async function carregarContas() {
    try {
        const res = await fetch("/aluno");
        const clientes = await res.json();

        contaSelect.innerHTML = "";
        contasMap = {};

        clientes.forEach(c => {
            c.contas.forEach(ct => {
                const key = `${c.id}|${ct.numero}`;
                contasMap[key] = { saldo: ct.saldo, tipo: ct.tipo };
                const option = document.createElement("option");
                option.value = key;
                option.text = `${c.nome} - ${ct.numero} (${ct.tipo})`;
                contaSelect.add(option);
            });
        });

        atualizarSaldo();
        atualizarRenderButton();
    } catch (err) {
        console.error("Erro ao carregar contas:", err);
    }
}

// Atualizar saldo exibido
function atualizarSaldo() {
    const key = contaSelect.value;
    if (!key) return;
    saldoDisplay.textContent = contasMap[key].saldo.toFixed(2);
}

// Atualizar visibilidade do botão render
function atualizarRenderButton() {
    const key = contaSelect.value;
    renderBtn.style.display = contasMap[key]?.tipo === "ContaPoupanca" ? "inline-block" : "none";
}

// Operações bancárias
async function depositar() {
    const key = contaSelect.value;
    const valor = parseFloat(prompt("Valor do depósito:"));
    if (isNaN(valor) || valor <= 0) return alert("Valor inválido");

    const [clienteId, numero] = key.split("|");

    await fetch(`/conta/depositar?clienteId=${clienteId}&numero=${numero}&valor=${valor}`, { method: "POST" });
    contasMap[key].saldo += valor;
    atualizarSaldo();
}

async function sacar() {
    const key = contaSelect.value;
    const valor = parseFloat(prompt("Valor do saque:"));
    if (isNaN(valor) || valor <= 0) return alert("Valor inválido");

    const [clienteId, numero] = key.split("|");

    await fetch(`/conta/sacar?clienteId=${clienteId}&numero=${numero}&valor=${valor}`, { method: "POST" });
    contasMap[key].saldo -= valor;
    atualizarSaldo();
}

async function transferir() {
    const key = contaSelect.value;
    const destino = prompt("Número da conta destino (clienteId|numero):");
    const valor = parseFloat(prompt("Valor da transferência:"));
    if (!destino || isNaN(valor) || valor <= 0) return alert("Dados inválidos");

    const [clienteId, numero] = key.split("|");

    await fetch(`/conta/transferir?origemClienteId=${clienteId}&origemNumero=${numero}&destinoNumero=${destino}&valor=${valor}`, { method: "POST" });

    contasMap[key].saldo -= valor;
    if (contasMap[destino]) contasMap[destino].saldo += valor;
    atualizarSaldo();
}

async function render() {
    const key = contaSelect.value;
    const [clienteId, numero] = key.split("|");

    await fetch(`/conta/render?clienteId=${clienteId}&numero=${numero}`, { method: "POST" });

    // Apenas simulação: aumentar 1% para mostrar visualmente
    contasMap[key].saldo *= 1.01;
    atualizarSaldo();
}

// Atualizar saldo e render quando mudar de conta
contaSelect.addEventListener("change", () => {
    atualizarSaldo();
    atualizarRenderButton();
});

// Carregar contas ao abrir a página
carregarContas();

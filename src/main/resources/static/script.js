const form = document.getElementById("clienteForm");
const tabela = document.getElementById("clientesTable").querySelector("tbody");
const tipoConta = document.getElementById("tipoConta");
const limiteChequeDiv = document.getElementById("limiteChequeDiv");
const taxaRendimentoDiv = document.getElementById("taxaRendimentoDiv");

// Mostrar/ocultar campos de acordo com o tipo de conta
tipoConta.addEventListener("change", () => {
    if (tipoConta.value === "ContaCorrente") {
        limiteChequeDiv.classList.remove("hidden");
        taxaRendimentoDiv.classList.add("hidden");
    } else {
        limiteChequeDiv.classList.add("hidden");
        taxaRendimentoDiv.classList.remove("hidden");
    }
});

form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const cliente = {
        nome: document.getElementById("nome").value,
        cpf: document.getElementById("cpf").value,
        contas: [
            {
                tipo: tipoConta.value,
                numero: document.getElementById("numeroConta").value,
                saldo: parseFloat(document.getElementById("saldo").value),
                limiteChequeEspecial: parseFloat(document.getElementById("limiteCheque").value),
                taxaRendimento: parseFloat(document.getElementById("taxaRendimento").value)
            }
        ]
    };

    try {
        await fetch("/Cliente", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(cliente)
        });
        form.reset();
        listarClientes();
    } catch (err) {
        console.error(err);
    }
});

async function listarClientes() {
    try {
        const res = await fetch("/Cliente");
        const clientes = await res.json();

        tabela.innerHTML = "";
        clientes.forEach(c => {
            const contasStr = c.contas.map(ct => {
                if (ct.tipo === "ContaCorrente") {
                    return `${ct.tipo} (${ct.numero}) - Saldo: ${ct.saldo}, Limite: ${ct.limiteChequeEspecial}`;
                } else {
                    return `${ct.tipo} (${ct.numero}) - Saldo: ${ct.saldo}, Rendimento: ${ct.taxaRendimento}`;
                }
            }).join("<br>");
            tabela.innerHTML += `<tr>
                <td>${c.id}</td>
                <td>${c.nome}</td>
                <td>${c.cpf}</td>
                <td>${contasStr}</td>
            </tr>`;
        });
    } catch (err) {
        console.error(err);
    }
}

// Listar clientes ao carregar a página
listarClientes();

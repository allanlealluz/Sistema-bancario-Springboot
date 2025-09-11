  const clienteId = 1; // troque pelo cliente logado

  async function carregarContas() {
      try {
          const response = await fetch(`http://localhost:8080/Cliente/${clienteId}`);
          if (!response.ok) throw new Error('Erro ao carregar contas');

          const cliente = await response.json();
          const select = document.getElementById('contas');
          select.innerHTML = '';
          cliente.contas.forEach(conta => {
              const option = document.createElement('option');
              option.value = conta.numero;
              option.text = `${conta.tipo} - ${conta.numero} - Saldo: ${conta.saldo}`;
              select.appendChild(option);
          });
      } catch (err) {
          document.getElementById('erro').innerText = err.message;
      }
  }

  async function depositar() {
      const numero = document.getElementById('contas').value;
      const valor = parseFloat(document.getElementById('valor').value);
      document.getElementById('mensagem').innerText = '';
      document.getElementById('erro').innerText = '';

      try {
          await fetch('http://localhost:8080/conta/depositar', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({ clienteId, numero, valor })
          });
          document.getElementById('mensagem').innerText = 'Depósito realizado com sucesso!';
          carregarContas(); // atualizar saldo
      } catch (err) {
          document.getElementById('erro').innerText = 'Erro ao depositar.';
      }
  }

  async function sacar() {
      const numero = document.getElementById('contas').value;
      const valor = parseFloat(document.getElementById('valor').value);
      document.getElementById('mensagem').innerText = '';
      document.getElementById('erro').innerText = '';

      try {
          await fetch('http://localhost:8080/conta/sacar', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({ clienteId, numero, valor })
          });
          document.getElementById('mensagem').innerText = 'Saque realizado com sucesso!';
          carregarContas(); // atualizar saldo
      } catch (err) {
          document.getElementById('erro').innerText = 'Erro ao sacar.';
      }
  }

  async function render() {
      const numero = document.getElementById('contas').value;
      document.getElementById('mensagem').innerText = '';
      document.getElementById('erro').innerText = '';

      try {
          await fetch('http://localhost:8080/conta/render', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({ clienteId, numero })
          });
          document.getElementById('mensagem').innerText = 'Conta poupanca rendida!';
          carregarContas(); // atualizar saldo
      } catch (err) {
          document.getElementById('erro').innerText = 'Erro ao render.';
      }
  }

  async function transferir() {
      const origemNumero = document.getElementById('contas').value;
      const destinoNumero = document.getElementById('destinoNumero').value;
      const valor = parseFloat(document.getElementById('valorTransferencia').value);
      document.getElementById('mensagem').innerText = '';
      document.getElementById('erro').innerText = '';

      try {
          await fetch('http://localhost:8080/conta/transferir', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({
                  origemClienteId: clienteId,
                  origemNumero,
                  destinoClienteId: 2, // trocar conforme destino real
                  destinoNumero,
                  valor
              })
          });
          document.getElementById('mensagem').innerText = 'Transferência realizada!';
          carregarContas(); // atualizar saldo
      } catch (err) {
          document.getElementById('erro').innerText = 'Erro ao transferir.';
      }
  }

  window.onload = () => carregarContas();
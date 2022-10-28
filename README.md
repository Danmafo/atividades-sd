# atividades-sd
Atividades p/ 1° Avaliação do Curso de Sistemas Distribuídos

# Integrantes:
<table>
    <tr>
        <td>Matrícula<td>
        <td>Discente<td>
    </tr>
    <tr>
        <td>20200795040<td>
        <td>Daniel Marques Fonseca<td>
    </tr>    
    <tr>
        <td>20200795602<td>
        <td>Taylor Henrique Moraes de Souza<td>
    </tr>    
</table>

<br>

## Atividade 1

```java
package br.com.sistemasdistribuidos.atividade.a1
```

### Geral

<p>Este pacote apresenta uma calculadora baseada na arquitetura cliente/servidor, onde um processo recebe dados do usuário e outro que processa e devolve a resposta.</p>

### Classe ServerUDP

<p>Esta classe é responsável pelo processamento dos dados e envio da resposta para o cliente.</p>

<p>O método main prepara o processo para o recebimento de requisições do usuário.</p>

<p>Este trecho inicia o socket na porta 6789 e cria um buffer para o armazenamento dos dados.</p>

```java
    public static void main(String[] args) throws IOException {
        String valores[];
        String resultado = null;
        String erro = "Erro";
        DatagramSocket aSocket = null;
        aSocket = new DatagramSocket(6789);
        System.out.println("Servidor UDP iniciado");
        byte[] buffer = new byte[1000];
 ```
 
 <p>O servidor deve aguardar por requisições enquanto estiver ligado. As mensagens devem estar no formato <code>&lt;numero&gt;&lt;sinal&gt;&lt;numero&gt;</code>. Para cada mensagem recebida é separado os valores e o sinal para realizar a operação.</p>
 
 <p>Uma resposta é enviada de volta ao cliente no formato <code>O resultado da operação &lt;numero&gt; &lt;sinal&gt; &lt;numero&gt; = &lt;resultado&gt;</code>.</p>
 
 ```java
        while (true) {
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            // bloqueia até receber a mensagem
            aSocket.receive(request);
            String mensagem = new String (request.getData(), 0, request.getLength());
            System.out.println( "RECEIVE=" + mensagem);

            String msgFinal;
            Operacoes operacao;
            String sinal = mensagem.replaceAll("[^\\+\\-\\*\\/]", ""); // remove numeros e espaços em branco, deixando apenas o sinal da operacao
            valores = mensagem.split("[\\+\\-\\*\\/]");
            
            switch (sinal) {
                case "+":
                    operacao = new Operacoes(valores[0], valores[1]);
                    resultado = String.valueOf(operacao.soma());
                break;

                case "-":
                    operacao = new Operacoes(valores[0], valores[1]);
                    resultado = String.valueOf(operacao.subtracao());
                break;

                case "*":
                    operacao = new Operacoes(valores[0], valores[1]);
                    resultado = String.valueOf(operacao.multiplicacao());
                break;

                case "/":
                    operacao = new Operacoes(valores[0], valores[1]);
                    resultado = String.valueOf(operacao.divisao());
                break;

                default:
                    System.out.println(erro);
            }
            
            msgFinal = "O resultado da operação " + mensagem + " = " + resultado;
            System.out.println(msgFinal);
            byte[] msg = msgFinal.getBytes();

            // instancia o objeto para enviar a mensagem
            DatagramPacket reply = new DatagramPacket(msg, 
                    msg.length, request.getAddress(),
                    request.getPort());
            // envia a resposta ao cliente
            aSocket.send(reply);
        }
```

### Classe ClienteUDP

<p>Atributos e construtor da classe</p>

```java
    private final String ENDERECO_SERVIDOR = "localhost";
    private final int PORTA = 6789;
    private DatagramSocket socket;
    
    /**
     * Creates new form Cliente
     */
    public ClienteUDP() {
        initComponents();
        
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(null, "Um erro impediu a inicialização da aplicação.");
            System.exit(1);
        }
    }
```

<p>Este método é responsável por extrair os dados das caixas de texto, enviá-los ao servidor e exibir a resposta na interface gráfica, quando o usuário clicar no botão <code>Resolver</code>.</p>

```java
    private void btnResolverActionPerformed(java.awt.event.ActionEvent evt) {                                            
        String num1 = txtNum1.getText();
        String num2 = txtNum2.getText();
        String operacao = labelSinalOperacao.getText();
        String resposta;
        
        enviarDados(num1 + " " + operacao + " " + num2);
        resposta = aguardarResposta();
        
        labelResultado.setText(resposta);
    }
```

<p>Este método altera a operação que será repassada ao servidor ao selecionar um item da lista de operações.</p>

```java
    private void comboOperacaoItemStateChanged(java.awt.event.ItemEvent evt) {                                               
        int operacao = comboOperacao.getSelectedIndex();
        
        switch (operacao) {
            case 0: labelSinalOperacao.setText("+"); break;
            case 1: labelSinalOperacao.setText("-"); break;
            case 2: labelSinalOperacao.setText("*"); break;
            case 3: labelSinalOperacao.setText("/"); break;
        }
    }
```

<p>Aqui os dados são empacotados e enviados ao servidor.</p>

```java
    private void enviarDados(String dadosString) {
        try {
            byte[] dados = dadosString.getBytes();
            DatagramPacket pacoteEnviado = new DatagramPacket(dados, dados.length, InetAddress.getByName(ENDERECO_SERVIDOR), PORTA);
            socket.send(pacoteEnviado);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage());
        }
    }
```

<p>Método que faz o cliente esperar por uma resposta do servidor. Caso o servidor não responda dentro de um segundo, um erro é disparado e o método retorna a mensagem "Servidor indisponível.". Isso previne que a aplicação pare aguardando uma resposta quando o servidor estiver desligado, por exemplo.</p>

```java
    private String aguardarResposta() {
        String resposta;
        DatagramPacket pacoteRecebido;
        
        try {
            byte[] dados = new byte[1000];
            pacoteRecebido = new DatagramPacket(dados, dados.length);
            
            // Servidor deve responder em até 1s
            socket.setSoTimeout(1000);

            socket.receive(pacoteRecebido);

            resposta = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());
        } catch (SocketTimeoutException timeout) {
            resposta = "Servidor indisponível.";
        } catch (IOException e) {
            resposta = "Ocorreu um erro: " + e.getMessage();
        }
        
        return resposta;
    }
```

<p>Método gerado automaticamente pelo gerador de interface gráfica da IDE Apache NetBeans.</p>

```java
    private void initComponents() { ... }
```

### Classe Operacoes

<p>Esta classe é responsável pela conversão de tipo dos dados e efetuação do cálculo matemático.</p>

<p>Atributos e construtor da classe</p>

```java
    public float x;
    public float y;

    public Operacoes(String valor1, String valor2) {
        this.x = Float.parseFloat(valor1);
        this.y = Float.parseFloat(valor2);
    }
```

<p>Soma os valores da operação.</p>

```java
    public float soma() {
        return x + y;
    }
```

<p>Subtrai os valores da operação.</p>

```java
    public float subtracao() {
        return x - y;
    }
```

<p>Multiplica os valores da operação.</p>

```java
    public float multiplicacao() {
        return x * y;
    }
```

<p>Divide os valores da operação.</p>

```java
    public float divisao() {
        return x / y;
    }
```

### Arquivo ClienteUDP.form

<p>Este arquivo é gerado automaticamente pelo gerador de interface gráfica da IDE Apache NetBeans. Usado para gerenciar os componentes gráficos dentro da IDE.</p>

## Atividade 2

```java
package br.com.sistemasdistribuidos.atividade.a2
```

### Classe ServerRMI

<p>Esta classe é responsável por deixar os métodos disponíveis para acesso remoto.</p>

<p>O método main instancia um objeto remoto, cria um registro para os objetos recebidos e libera o acesso para outras máquinas.</p>

```java
    public static void main(String[] args) {
        try {
            VeiculoInterface classificados = new Classificados();
            String acesso = "rmi://localhost/classificados";
    
            System.out.println("Registrando objeto no RMIRegistry...");
            LocateRegistry.createRegistry(1099);
            Naming.rebind(acesso, classificados);
    
            System.out.println("Aguardando clientes...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

### Classe Veiculo

<p>Atributos e construtor da classe</p>

```java
    String nomeCliente;
    String marcaVeiculo;
    double valorVenda;
    int ano;
    
    public Veiculo() { }

    public Veiculo(String nomeCliente, String marcaVeiculo, double valorVenda, int ano) {
        this.nomeCliente = nomeCliente;
        this.marcaVeiculo = marcaVeiculo;
        this.valorVenda = valorVenda;
        this.ano = ano;
    }
```

<p>Esta é a classe cujas instâncias serão transmitidas pela rede. A interface <code>Serializable</code> permite que os objetos sejam materializados e enviados.</p>

```java
public class Veiculo implements Serializable {
```

<p>Sobrescreve o método toString para exibir os dados na tela.</p>

```java
    @Override
    public String toString() {
        return "Nome do Cliente: " + this.nomeCliente + "\n"
                + "Marca do Veículo: " + this.marcaVeiculo + "\n"
                + "Valor de Venda: " + this.valorVenda + "\n"
                + "Ano: " + this.ano;
    }
```

### Classe VeiculoInterface

<p>Permite o acesso aos métodos do objeto instanciado no servidor extendendo a classe <code>Remote</code>.</p>

```java
public interface VeiculoInterface extends Remote {
```

<p>Assinatura dos métodos.</p>

```java
    public List<Veiculo> search2Ano (int anoVeiculo) throws RemoteException;
    public void add (Veiculo v) throws RemoteException;
```

### Classe Classificados

<p>Atributos e construtor da classe</p>

```java
    List<Veiculo> veiculos = new ArrayList<>();

    protected Classificados() throws RemoteException {
        super();
    }
```

<p>Retorna uma lista com todos os veículos armazenados.</p>

```java
    @Override
    public List<Veiculo> search2Ano(int anoVeiculo) throws RemoteException {
        List<Veiculo> procuradosPorAno = new ArrayList<>();
        for (Veiculo veiculo : veiculos) {
            if (veiculo.getAno() == anoVeiculo) {
                procuradosPorAno.add(veiculo);
            }
        }
        return procuradosPorAno;
    }
```

<p>Adiciona um veículo à lista.</p>

```java
    @Override
    public void add(Veiculo v) throws RemoteException {
        veiculos.add(v);
    }
```

### Classe ClienteRMI

<p>Nesta classe é feito o acesso ao método remoto e a transferências de alguns objetos para o servidor.</p>

<p>Neste trecho é realizado a busca pelo objeto remoto no servidor.</p>

```java
        String acesso = "rmi://localhost/classificados";
        VeiculoInterface classificados = (VeiculoInterface) Naming.lookup(acesso);
```

<p>Aqui os objetos são criado e repassados ao servidor pela chamada ao método remoto <code>add()</code>.</p>

```java
        Veiculo veiculo1 = new Veiculo("Daniel Fonseca", "BMW", 120000.0, 2022);
        Veiculo veiculo2 = new Veiculo("Taylor Henrique", "Jaguar", 190000.0, 2022);
        Veiculo veiculo3 = new Veiculo("Marty McFly", "DMC", 100000.0, 1975);
        Veiculo veiculo4 = new Veiculo("Vovô", "Chevrolet Chevette", 90000.0, 1975);

        classificados.add(veiculo1);
        classificados.add(veiculo2);
        classificados.add(veiculo3);
        classificados.add(veiculo4);
```

<p>Uma lista de veículos é obtida por meio da chamada do método <code>search2Ano()</code> e exibida na tela.</p>

```java
        List<Veiculo> veiculos2022 = classificados.search2Ano(2022);
        List<Veiculo> veiculos1975 = classificados.search2Ano(1975);

        System.out.println("== 2022 ==");
        for (int i = 0; i < veiculos2022.size(); i++) {
            System.out.println("\nVeículo " + (i + 1) + "\n" + veiculos2022.get(i) + "\n");
        }

        System.out.println("== 1975 ==");
        for (int i = 0; i < veiculos1975.size(); i++) {
            System.out.println("\nVeículo " + (i + 1) + "\n" + veiculos1975.get(i) + "\n");
        }
```

## Atividade 3

```java
package br.com.sistemasdistribuidos.atividade.a3
```

### Geral
<p>Todas as classes nesse pacote implementam a interface <strong>Runnable</strong>, porque habilita que a classe seja passada por uma Thread ou Thread Pool podendo ser executada simultaneamente com outras classes, que é o que faremos aqui.</p>

<p>O método run é obrigatório para qualquer classe que implementa Runnable, ele sempre será chamado quando iniciarmos a classe.</p>

<br>

### Classe ServerTCP

<p>Atributos e construtor da classe</p>

```java
    int porta = 8081;

    private List<Conexao> conexoes;
    private ServerSocket server;
    private boolean funcionando;
    private ExecutorService threads;
    private List<String> logados;

    public ServerTCP() {
        logados = new ArrayList<>();
        conexoes = new ArrayList<>();
        funcionando = false;
    }
```

<p>Aqui o servidor é iniciado na porta 8081 quando instanciamos um ServerSocket, e fica rodando enquanto a variável <strong>funcionando</strong> é igual a false.</p>

<p>Enquanto esta condição for verdadeira, o servidor está aberto para receber novos Sockets (Clientes). Sempre que um novo cliente se conecta, é criado um objeto Conexao, que é adicionado à uma lista de conexões e executado simultaneamente a outros clientes.</p>

<p>Caso ocorra uma execeção, chamamos o método quit().</p>

```java
@Override
    public void run() {
        try {
            server = new ServerSocket(porta);
            threads = Executors.newCachedThreadPool();
            System.out.println("* SERVIDOR CONECTADO *");
            while (!funcionando) {
                Socket cliente = server.accept();
                Conexao conexao = new Conexao(cliente, this);
                conexoes.add(conexao);
                threads.execute(conexao);
            }
        } catch (Exception e) {
            quit();
        }
    }
```

<p>Pega todas as conexões da lista de conexões, e caso a conexão não seja nula, acessa o método listarLogados() dentro do objeto Conexao que lista todos os nicks conectados para o usuário, exceto o seu próprio.</p>

```java
public void listarLogados() {
        for (Conexao c : conexoes) {
            if (c != null) {
                c.listarConectados();
            }
        }
    } 
```

<p>Método que envia uma mensagem passada como parâmetro para todos os usuários logados.</p>

```java
public void broadcast(String msg) {
        for (Conexao c : conexoes) {
            if (c != null) {
                c.enviarMensagem(msg);
            }
        }
    }
```

<p>Método que desliga o servidor e todas as conexões feitas nele.</p>

```java
public void quit() {
        funcionando = true;
        try {
            if (!server.isClosed()) {
                server.close();
            }
            for (Conexao c : conexoes) {
                c.desligarConexao();
            }
        } catch (IOException e) { }
    }
```

<p>Roda o Servidor</p>

```java
public static void main(String[] args) {
        ServerTCP server = new ServerTCP();
        server.run();
    }
```

<br>

### Classe Conexao

<p>Atributos e construtor da classe</p>

<p>Essa classe serve como um handler para a conexão do cliente com o servidor, por isso no construtor recebe o socket e o server</p>

```java
    private Socket cliente;
    private ServerTCP server;
    private BufferedReader entrada;
    private PrintWriter saida;
    private String nickname;

    public Conexao(Socket cliente, ServerTCP server) {
        this.cliente = cliente;
        this.server = server;
    }
```

<p>Ao iniciar um conexão, é criada um objeto de entrada e outro de saída de dados, e o cliente define um nickname. Assim que o nickname é criado, o servidor emite uma mensagem no seu próprio console dizendo cliente se conectou, e nos consoles das conexões dizendo que o cliente entrou no chat.</p>

<p>Enquanto o cliente puder digitar e mandar uma mensagem sua conexão se mantém e toda mensagem que ele escrever ecoa em todas as outras conexões. Caso ele digite "<strong>#USERS</strong>", lhe é mostrado uma lista de quem está conectado, com exceção dele mesmo. Caso ele digite "<strong>#QUIT</strong>", todas as conexões recebem uma mensagem dizendo que o cliente se desconectou, o console do server emite a mesma informação para si, a lista de usuários logados remove este cliente e a função desligarConexao() é chamada.</p>

<p>Caso ocorra uma exceção, a função desligarConexao() é chamada.</p>

<p></p>

```java
@Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            saida = new PrintWriter(cliente.getOutputStream(), true);
            saida.println("Digite o seu nickname: ");
            this.nickname = entrada.readLine();
            server.getLogados().add(nickname);
            System.out.println(nickname + " conectado!");
            server.broadcast(nickname + " entrou no chat!");
            String msg;
            while ((msg = entrada.readLine()) != null) {
                if (msg.equals("#USERS")) {
                    server.listarLogados();
                } else if (msg.equals("#QUIT")) {
                    server.broadcast(nickname + " saiu do chat.");
                    System.out.println(nickname + " desconectado!");
                    server.getLogados().remove(nickname);
                    desligarConexao();
                } else {
                    server.broadcast(msg);
                }
            }
        } catch (IOException e) {
            try {
                desligarConexao();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
```

<p>Lista os nicknames dos usuários conectados, com exceção do seu.</p>

```java
public void listarConectados() {
        List<String> logados = server.getLogados();
        for (String nick : logados) {
            if (!nick.equals(this.nickname)) {
                saida.println(nick);
            }
        }   
    }
```

Envia mensagem passada como parâmetro

```java
public void enviarMensagem(String msg) {
        saida.println(msg);   
    }
```

Encerra os atributos de entrada, saída e o cliente

```java
public void desligarConexao() throws IOException {
        entrada.close();
        saida.close();
        if (!cliente.isClosed()) {
            cliente.close();
        }
    }
```

<br>

### Classe ClienteTCP

<p>Atributos e construtor da classe</p>

```java
    int porta = 8081;

    private Socket cliente;
    private BufferedReader entrada;
    private PrintWriter saida;
    private boolean funcionando = false;
```

<p>Inicia um socket no ip e na porta específicada, também cria os objetos de entrada e saída de dados. Instancia um objeto EntradaCliente que servirá como um handler para as entradas do cliente, alocando este em uma Thread.</p>

<p>Enquanto o cliente puder digitar ele se mantém, e caso ocorra uma exceção é chamada a funçaõ quit().</p>

```java
    @Override
    public void run() {
        try {
            cliente = new Socket("localhost", porta);
            saida = new PrintWriter(cliente.getOutputStream(), true);
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            EntradaCliente entradaCliente = new EntradaCliente(this);
            Thread thread = new Thread(entradaCliente);
            thread.start();

            String msgEntrada;
            while ((msgEntrada = entrada.readLine()) != null) {
                System.out.println(msgEntrada);
            }
        } catch (IOException e) {
            quit();
        }
    }
```

<p>Fecha os objetos de entrada, saída e também o cliente. Além de mudar o status de <strong>funcionando</strong> para true.</p>

```java
    public void quit() {
        this.funcionando = true;
        try {
            entrada.close();
            saida.close();
            if (!cliente.isClosed()) {
                cliente.close();
            }
        } catch (IOException e) { }
    }
```

<br>

### Classe EntradaCliente

<p><p>Atributos e construtor da classe</p></p>

```java
    private ClienteTCP cliente;

    public EntradaCliente(ClienteTCP cliente) {
        this.cliente = cliente;
    }
```

<p>Cria um BufferedReader que vai receber as entradas do cliente. Enquanto o status da variável <strong>funcionando</strong> do cliente for false continua rodando.</p>

<p>Caso a entrada do cliente <strong>#QUIT</strong>, fecha o leitor e chama a função quit() do cliente, se não, devolve a mensagem que o cliente passar.</p>

<p>Se houver exceção, chama o quit() do cliente.</p>

```java
    @Override
    public void run() {
        try {
            BufferedReader leitorDeEntrada = new BufferedReader(new InputStreamReader(System.in));
            while (!cliente.isFuncionando()) {
                String msg = leitorDeEntrada.readLine();
                if (msg == "#QUIT") {
                    leitorDeEntrada.close();
                    cliente.quit();
                } else {
                    cliente.getSaida().println(msg);
                }
            }
        } catch (IOException e) {
            cliente.quit();
        }
    }
```

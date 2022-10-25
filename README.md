# atividades-sd
Atividades p/ 1° Avaliação do Curso de Sistemas Distribuídos

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
            System.out.println("NICK INSIDE CONN = " + nickname);
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

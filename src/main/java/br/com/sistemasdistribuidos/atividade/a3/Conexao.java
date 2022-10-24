package br.com.sistemasdistribuidos.atividade.a3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Conexao implements Runnable {

    private Socket cliente;
    private ServerTCP server;
    private BufferedReader entrada;
    private PrintWriter saida;
    private String nickname;

    public Conexao(Socket cliente, ServerTCP server) {
        this.cliente = cliente;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            saida = new PrintWriter(cliente.getOutputStream(), true);
            saida.println("Digite o seu nickname: ");
            nickname = entrada.readLine();
            System.out.println(nickname + " conectado!");
            server.broadcast(nickname + " entrou no chat!");
            String msg;
            while ((msg = entrada.readLine()) != null) {
                if (msg.startsWith("/nickname")) {
                    String[] msgSplit = msg.split(" ", 2);
                    if (msgSplit.length == 2) {
                        server.broadcast(nickname + " mudou seu nickname para " + msgSplit[1]);
                        nickname = msgSplit[1];
                        saida.println("Nickname trocado com sucesso!");
                    } else {
                        saida.println("Nenhum nickname fornecido!");
                    }
                } else if (msg.equals("#QUIT")) {
                    server.broadcast(nickname + " saiu do chat.");
                    System.out.println(nickname + " desconectado!");
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

    public void enviarMensagem(String msg) {
        saida.println(msg);   
    }

    public void desligarConexao() throws IOException {
        entrada.close();
        saida.close();
        if (!cliente.isClosed()) {
            cliente.close();
        }
    }

    public Socket getCliente() {
        return cliente;
    }
    
}

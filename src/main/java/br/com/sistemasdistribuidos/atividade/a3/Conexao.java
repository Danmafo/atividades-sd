package br.com.sistemasdistribuidos.atividade.a3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

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

    public void listarConectados() {
        List<String> logados = server.getLogados();
        for (String nick : logados) {
            if (!nick.equals(this.nickname)) {
                saida.println(nick);
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
    
    public PrintWriter getSaida() {
        return saida;
    }

    public String getNickname() {
        return this.nickname;
    }
    
}

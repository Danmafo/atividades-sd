package br.com.sistemasdistribuidos.atividade.a3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP implements Runnable {

    int porta = 8081;

    private List<Conexao> conexoes = new ArrayList<>();
    private ServerSocket server;
    private boolean funcionando = true;
    private ExecutorService threads;

    @Override
    public void run() {
        try {
            server = new ServerSocket(porta);
            threads = Executors.newCachedThreadPool();
            System.out.println("* SERVIDOR CONECTADO *");
            while (funcionando) {
                Socket cliente = server.accept();
                Conexao conexao = new Conexao(cliente, this);
                conexoes.add(conexao);
                threads.execute(conexao);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String msg) {
        for (Conexao c : conexoes) {
            if (c != null) {
                c.enviarMensagem(msg);
            }
        }
    }

    public void quit() throws IOException {
        funcionando = false;
        if (!server.isClosed()) {
            server.close();
        }
        for (Conexao c : conexoes) {
            c.quit();
        }
    }

    public static void main(String[] args) {
        ServerTCP server = new ServerTCP();
        server.run();
    }
    
}

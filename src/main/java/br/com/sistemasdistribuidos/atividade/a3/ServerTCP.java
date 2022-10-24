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

    private List<Conexao> conexoes;
    private ServerSocket server;
    private boolean funcionando;
    private ExecutorService threads;

    public ServerTCP() {
        conexoes = new ArrayList<>();
        funcionando = false;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
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

    public void broadcast(String msg) {
        for (Conexao c : conexoes) {
            if (c != null) {
                c.enviarMensagem(msg);
            }
        }
    }

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
    
    public static void main(String[] args) {
        ServerTCP server = new ServerTCP();
        server.run();
    }

}
package br.com.sistemasdistribuidos.atividade.a3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteTCP implements Runnable {

    int porta = 8081;

    private Socket cliente;
    private BufferedReader entrada;
    private PrintWriter saida;
    private boolean funcionando = false;

    @Override
    public void run() {
        try {
            cliente = new Socket("localhost", 9999);
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
    
    public boolean isFuncionando() {
        return funcionando;
    }

    public PrintWriter getSaida() {
        return saida;
    }

    public void setSaida(PrintWriter saida) {
        this.saida = saida;
    }

    public void setFuncionando(boolean funcionando) {
        this.funcionando = funcionando;
    }

    public static void main(String[] args) {
        ClienteTCP cliente = new ClienteTCP();
        cliente.run();
    }

}

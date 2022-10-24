package br.com.sistemasdistribuidos.atividade.a3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EntradaCliente implements Runnable {

    private ClienteTCP cliente;

    public EntradaCliente(ClienteTCP cliente) {
        this.cliente = cliente;
    }

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
    
}

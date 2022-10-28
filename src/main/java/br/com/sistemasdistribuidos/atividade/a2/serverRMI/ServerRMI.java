package br.com.sistemasdistribuidos.atividade.a2.serverRMI;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import br.com.sistemasdistribuidos.atividade.a2.ClassificadosVeiculos;

public class ServerRMI {
    
    public static void main(String[] args) {
        try {
            ClassificadosVeiculos classificados = new Classificados();
            String acesso = "rmi://localhost/classificados";
    
            System.out.println("Registrando objeto no RMIRegistry...");
            LocateRegistry.createRegistry(1099);
            Naming.rebind(acesso, classificados);
    
            System.out.println("Aguardando clientes...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

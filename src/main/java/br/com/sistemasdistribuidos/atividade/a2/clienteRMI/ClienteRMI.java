package br.com.sistemasdistribuidos.atividade.a2.clienteRMI;

import java.rmi.Naming;
import java.util.List;

import br.com.sistemasdistribuidos.atividade.a2.interfaces.VeiculoInterface;
import br.com.sistemasdistribuidos.atividade.a2.serverRMI.Veiculo;

public class ClienteRMI {

    public static void main(String[] args) throws Exception {
        String acesso = "rmi://localhost/classificados";
        VeiculoInterface classificados = (VeiculoInterface) Naming.lookup(acesso);
        
        Veiculo veiculo1 = new Veiculo("Daniel Fonseca", "BMW", 120000.0, 2022);
        Veiculo veiculo2 = new Veiculo("Taylor Henrique", "Jaguar", 190000.0, 2022);
        Veiculo veiculo3 = new Veiculo("Marty McFly", "DMC", 100000.0, 1975);
        Veiculo veiculo4 = new Veiculo("Vovô", "Chevrolet Chevette", 90000.0, 1975);

        classificados.add(veiculo1);
        classificados.add(veiculo2);
        classificados.add(veiculo3);
        classificados.add(veiculo4);

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
    }
    
}

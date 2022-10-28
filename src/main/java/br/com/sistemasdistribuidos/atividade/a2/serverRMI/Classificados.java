package br.com.sistemasdistribuidos.atividade.a2.serverRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemasdistribuidos.atividade.a2.Veiculo;
import br.com.sistemasdistribuidos.atividade.a2.VeiculoInterface;

public class Classificados extends UnicastRemoteObject implements VeiculoInterface {

    List<Veiculo> veiculos = new ArrayList<>();

    protected Classificados() throws RemoteException {
        super();
    }

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

    @Override
    public void add(Veiculo v) throws RemoteException {
        veiculos.add(v);
    }
    
}

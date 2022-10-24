package br.com.sistemasdistribuidos.atividade.a2.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import br.com.sistemasdistribuidos.atividade.a2.serverRMI.Veiculo;

public interface VeiculoInterface extends Remote {

    public List<Veiculo> search2Ano (int anoVeiculo) throws RemoteException;
    public void add (Veiculo v) throws RemoteException;
    
}
    

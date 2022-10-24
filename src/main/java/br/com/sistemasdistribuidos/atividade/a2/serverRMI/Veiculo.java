package br.com.sistemasdistribuidos.atividade.a2.serverRMI;

import java.io.Serializable;

public class Veiculo implements Serializable {

    String nomeCliente;
    String marcaVeiculo;
    double valorVenda;
    int ano;
    
    public Veiculo() { }

    public Veiculo(String nomeCliente, String marcaVeiculo, double valorVenda, int ano) {
        this.nomeCliente = nomeCliente;
        this.marcaVeiculo = marcaVeiculo;
        this.valorVenda = valorVenda;
        this.ano = ano;
    }
    
    @Override
    public String toString() {
        return "Nome do Cliente: " + this.nomeCliente + "\n"
                + "Marca do Veículo: " + this.marcaVeiculo + "\n"
                + "Valor de Venda: " + this.valorVenda + "\n"
                + "Ano: " + this.ano;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getMarcaVeiculo() {
        return marcaVeiculo;
    }

    public void setMarcaVeiculo(String marcaVeiculo) {
        this.marcaVeiculo = marcaVeiculo;
    }

    public double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }
    
}

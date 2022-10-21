package br.com.sistemasdistribuidos.atividade.a1;

public class Operacoes {

    public float x;
    public float y;

    public Operacoes(String valor1, String valor2) {
        this.x = Float.parseFloat(valor1);
        this.y = Float.parseFloat(valor2);
    }

    public float soma() {
        return x + y;
    }

    public float subtracao() {
        return x - y;
    }

    public float multiplicacao() {
        return x * y;
    }

    public float divisao() {
        return x / y;
    }
    
}

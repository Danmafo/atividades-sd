package br.com.sistemasdistribuidos.atividade.a1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerUDP {

    public static void main(String[] args) throws IOException {
        String valores[];
        String resultado = null;
        String erro = "Erro";
        DatagramSocket aSocket = null;
        aSocket = new DatagramSocket(6789);
        System.out.println("Servidor UDP iniciado");
        byte[] buffer = new byte[1000];
        while (true) {
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            // bloqueia até receber a mensagem
            aSocket.receive(request);
            String mensagem = new String (request.getData(), 0, request.getLength());
            System.out.println( "RECEIVE=" + mensagem);

            String msgFinal;
            Operacoes operacao;
            String sinal = mensagem.replaceAll("[^\\+\\-\\*\\/]", ""); // remove numeros e espaços em branco, deixando apenas o sinal da operacao
            valores = mensagem.split("[\\+\\-\\*\\/]");
            
            switch (sinal) {
                case "+":
                    operacao = new Operacoes(valores[0], valores[1]);
                    resultado = String.valueOf(operacao.soma());
                break;

                case "-":
                    operacao = new Operacoes(valores[0], valores[1]);
                    resultado = String.valueOf(operacao.subtracao());
                break;

                case "*":
                    operacao = new Operacoes(valores[0], valores[1]);
                    resultado = String.valueOf(operacao.multiplicacao());
                break;

                case "/":
                    operacao = new Operacoes(valores[0], valores[1]);
                    resultado = String.valueOf(operacao.divisao());
                break;

                default:
                    System.out.println(erro);
            }
            
            msgFinal = "O resultado da operação " + mensagem + " = " + resultado;
            System.out.println(msgFinal);
            byte[] msg = msgFinal.getBytes();

            // instancia o objeto para enviar a mensagem
            DatagramPacket reply = new DatagramPacket(msg, 
                    msg.length, request.getAddress(),
                    request.getPort());
            // envia a resposta ao cliente
            aSocket.send(reply);
        } // fim do while

    }

}

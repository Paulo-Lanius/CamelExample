package com.example.model;

public class Payment {
    private double valor;
    private int mesa;
    private String tipoDePagamento;

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
    public int getMesa() {
        return mesa;
    }
    public void setMesa(int mesa) {
        this.mesa = mesa;
    }
    public String getTipoDePagamento() {
        return tipoDePagamento;
    }
    public void setTipoDePagamento(String tipoDePagamento) {
        this.tipoDePagamento = tipoDePagamento;
    }    
}

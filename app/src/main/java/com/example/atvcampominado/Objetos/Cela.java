package com.example.atvcampominado.Objetos;

import java.util.ArrayList;

public class Cela {

    private int valor;
    private ArrayList<Cela> celas_vizinhas;
    private Boolean estado;

    public Cela(){

    }

    public Cela(int valor, ArrayList<Cela> celas) {
        this.valor = valor;
        this.celas_vizinhas = celas;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public ArrayList<Cela> getSelas_vizinhas() {
        return celas_vizinhas;
    }

    public void setSelas_vizinhas(ArrayList<Cela> celas_vizinhas) {
        this.celas_vizinhas = celas_vizinhas;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}

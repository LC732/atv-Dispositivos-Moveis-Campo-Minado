package com.example.atvcampominado.Objetos;

public class Vitoria {

    private String data;
    private String hora;
    private String tempoDeJogo;
    private int clicks;
    private int bandeiras;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vitoria() {

    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHora(String hora) { this.hora = hora;}

    public void setTempoDeJogo(String tempoDeJogo) {
        this.tempoDeJogo = tempoDeJogo;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public void setBandeiras(int bandeiras) {
        this.bandeiras = bandeiras;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public String getTempoDeJogo() {
        return tempoDeJogo;
    }

    public int getClicks() {
        return clicks;
    }

    public int getBandeiras() {
        return bandeiras;
    }


}

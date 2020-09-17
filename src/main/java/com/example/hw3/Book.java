package com.example.hw3;

public class Book {
    private String tytuł;
    private String autor;
    private String rokWydania;
    private String typ;

    public Book(String tytuł, String autor, String rokWydania, String typ) {
        this.tytuł = tytuł;
        this.autor = autor;
        this.rokWydania = rokWydania;
        this.typ = typ;
    }

    public String getTytuł() {
        return tytuł;
    }

    public void setTytuł(String tytuł) {
        this.tytuł = tytuł;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getRokWydania() {
        return rokWydania;
    }

    public void setRokWydania(String rokWydania) {
        this.rokWydania = rokWydania;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}

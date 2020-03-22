package com.adp.tokobukubuka;

public class Category {

    private int id_penulis;
    private String nama_penulis;

    public Category(){}

    public Category(int id_penulis, String nama_penulis){
        this.id_penulis = id_penulis;
        this.nama_penulis = nama_penulis;
    }

    public int getId_penulis() {
        return id_penulis;
    }

    public void setId_penulis(int id_penulis) {
        this.id_penulis = id_penulis;
    }

    public String getNama_penulis() {
        return nama_penulis;
    }

    public void setNama_penulis(String nama_penulis) {
        this.nama_penulis = nama_penulis;
    }
}

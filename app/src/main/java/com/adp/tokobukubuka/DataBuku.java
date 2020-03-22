package com.adp.tokobukubuka;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tb_buku")
public class DataBuku {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_buku")
    private int id_buku;

    @ColumnInfo(name = "nama_buku")
    private String nama_buku;

    @ColumnInfo(name = "genre")
    private String genre;

    @ColumnInfo(name = "sinopsis")
    private String sinopsis;

    @ColumnInfo(name = "stock")
    private int stock;

    @ColumnInfo(name = "price")
    private int price;

    @ColumnInfo(name = "gambar")
    private String gambar;

    @ColumnInfo(name = "id_penulis")
    private int id_penulis;

    public DataBuku(int id_buku, String nama_buku, String genre, String sinopsis, int stock, int price, String gambar, int id_penulis) {
        this.id_buku = id_buku;
        this.nama_buku = nama_buku;
        this.genre = genre;
        this.sinopsis = sinopsis;
        this.stock = stock;
        this.price = price;
        this.gambar= gambar;
        this.id_penulis= id_penulis;
    }

    @Ignore
    public DataBuku(String nama_buku, String genre, String sinopsis, int stock, int price, int id_penulis) {
        this.nama_buku = nama_buku;
        this.genre = genre;
        this.sinopsis = sinopsis;
        this.stock = stock;
        this.price = price;
        this.gambar= gambar;
        this.id_penulis= id_penulis;
    }

    public int getId_buku() {
        return id_buku;
    }

    public void setId_buku(int id_buku) {
        this.id_buku = id_buku;
    }

    public String getNama_buku() {
        return nama_buku;
    }

    public void setNama_buku(String nama_buku) {
        this.nama_buku = nama_buku;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public int getId_penulis() {
        return id_penulis;
    }

    public void setId_penulis(int id_penulis) {
        this.id_penulis = id_penulis;
    }
}

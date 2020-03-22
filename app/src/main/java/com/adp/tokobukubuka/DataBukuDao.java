package com.adp.tokobukubuka;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DataBukuDao {
    @Query("SELECT * FROM tb_buku ORDER BY id_buku")
    List<DataBuku> loadAllBuku();

    @Insert
    void insertBuku(DataBuku dataBuku);
}

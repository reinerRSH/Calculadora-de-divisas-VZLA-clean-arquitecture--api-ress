package com.example.dolarcotizacion.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DolarDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDolar(dolar: DolarEntity)



    @Query("SELECT * FROM Precio_dolar ORDER BY id DESC LIMIT 1")
    suspend fun obtenerUltimoDolar(): DolarEntity?

}


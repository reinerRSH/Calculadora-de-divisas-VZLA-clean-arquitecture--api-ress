package com.example.dolarcotizacion.model.Daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dolarcotizacion.model.data.Entitys.PagoMovilEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PagoMovilDao {
    @Query("SELECT * FROM pago_movil")
   fun getAllPagoMovil(): Flow<List<PagoMovilEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPago(pago: PagoMovilEntity)

    @Delete
    suspend fun deletePago(pago: PagoMovilEntity)



}
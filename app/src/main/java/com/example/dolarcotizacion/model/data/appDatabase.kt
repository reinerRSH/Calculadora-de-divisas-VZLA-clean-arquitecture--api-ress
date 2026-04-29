package com.example.dolarcotizacion.model.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dolarcotizacion.model.Daos.DolarDao
import com.example.dolarcotizacion.model.Daos.PagoMovilDao

import com.example.dolarcotizacion.model.data.Entitys.PagoMovilEntity


@Database(entities = [DolarEntity::class, PagoMovilEntity::class], version = 2)
abstract class appDatabase: RoomDatabase() {


    abstract fun dolarDao(): DolarDao
    abstract fun pagoMovilDao(): PagoMovilDao
}




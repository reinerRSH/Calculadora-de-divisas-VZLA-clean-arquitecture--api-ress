package com.example.dolarcotizacion.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.jvm.java

@Database(entities = [DolarEntity::class], version = 1)
abstract class appDatabase: RoomDatabase() {


    abstract fun dolarDao(): DolarDao

}
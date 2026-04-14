package com.example.dolarcotizacion.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.jvm.java

@Database(entities = [DolarEntity::class], version = 1)
abstract class appDatabase: RoomDatabase() {


    abstract fun dolarDao(): DolarDao

    companion object{
        @Volatile
        private var INSTANCE: appDatabase? = null

        fun getDatabase(context: Context): appDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    appDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }



    }
}
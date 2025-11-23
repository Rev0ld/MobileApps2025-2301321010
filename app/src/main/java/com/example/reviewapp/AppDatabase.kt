package com.example.reviewapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Review::class], version = 1, exportSchema = false)

abstract class AppDatabase: RoomDatabase() {
    abstract fun reviewDao(): ReviewDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reviews.db"
                ).fallbackToDestructiveMigration(true)
                    .build().also { INSTANCE = it }
            }
    }
}
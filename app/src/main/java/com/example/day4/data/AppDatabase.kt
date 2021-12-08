package com.example.day4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Author::class,
        Book::class
               ],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authorDao(): AuthorDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appDB"
                ).build().also {
                    INSTANCE = it
                }

            }
        }
    }
}
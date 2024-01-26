package com.prplmnstr.mynotesapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prplmnstr.mynotesapp.model.Note
import com.prplmnstr.mynotesapp.utils.Constants


@Database(
    entities = [Note::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val notesDao: NotesDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        Constants.DATABASE_NAME
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
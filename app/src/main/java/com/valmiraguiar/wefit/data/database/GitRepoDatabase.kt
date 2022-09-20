package com.valmiraguiar.wefit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.valmiraguiar.wefit.data.entity.dao.GitRepoDAO
import com.valmiraguiar.wefit.domain.model.GitRepoModel

@Database(
    entities = [GitRepoModel::class],
    version = 1,
    exportSchema = false
)
abstract class GitRepoDatabase : RoomDatabase() {
    abstract val gitRepoDAO: GitRepoDAO

    companion object {
        @Volatile
        private var INSTANCE: GitRepoDatabase? = null

        fun getInstance(context: Context): GitRepoDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GitRepoDatabase::class.java,
                        "gitrepos-db"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}
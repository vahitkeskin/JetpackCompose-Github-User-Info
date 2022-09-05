package com.vahitkeskin.jetpackcomposegithubuserinfo.api

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vahitkeskin.jetpackcomposegithubuserinfo.dao.GithubLastSearchDao
import com.vahitkeskin.jetpackcomposegithubuserinfo.dao.LastSearchRoom

@Database(entities = [LastSearchRoom::class], version = 1, exportSchema = false)
abstract class LastSearchDatabase : RoomDatabase() {
    abstract fun githubDao(): GithubLastSearchDao
}
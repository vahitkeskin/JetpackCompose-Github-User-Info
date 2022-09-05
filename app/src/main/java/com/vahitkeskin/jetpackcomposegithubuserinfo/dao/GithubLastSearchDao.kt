package com.vahitkeskin.jetpackcomposegithubuserinfo.dao

import androidx.room.*
import com.vahitkeskin.jetpackcomposegithubuserinfo.utils.Constants.Companion.TABLE_LAST_SEARCH
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubLastSearchDao {
    @Query("SELECT * FROM $TABLE_LAST_SEARCH ORDER BY id DESC")
    fun getGithub(): Flow<List<LastSearchRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLastSearch(lastSearchRoom: LastSearchRoom)

    @Delete
    fun deleteGithub(lastSearchRoom: LastSearchRoom)

    @Query("DELETE FROM $TABLE_LAST_SEARCH")
    fun deleteAllGithub()

}
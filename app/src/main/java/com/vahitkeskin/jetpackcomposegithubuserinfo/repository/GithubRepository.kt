package com.vahitkeskin.jetpackcomposegithubuserinfo.repository

import com.vahitkeskin.jetpackcomposegithubuserinfo.dao.LastSearchRoom
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun addBookToRoom(lastSearchRoom: LastSearchRoom)
    fun getGithubFromRoom(): Flow<List<LastSearchRoom>>
    fun deleteGithubFromRoom(lastSearchRoom: LastSearchRoom)
    fun deleteAllGithubFromRoom()
}
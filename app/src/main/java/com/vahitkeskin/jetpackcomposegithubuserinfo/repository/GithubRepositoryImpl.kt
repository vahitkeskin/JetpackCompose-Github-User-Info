package com.vahitkeskin.jetpackcomposegithubuserinfo.repository

import com.vahitkeskin.jetpackcomposegithubuserinfo.dao.GithubLastSearchDao
import com.vahitkeskin.jetpackcomposegithubuserinfo.dao.LastSearchRoom
import kotlinx.coroutines.flow.Flow

class GithubRepositoryImpl(
    private val githubLastSearchDao: GithubLastSearchDao
) : GithubRepository{
    override fun addBookToRoom(lastSearchRoom: LastSearchRoom) = githubLastSearchDao.addLastSearch(lastSearchRoom)
    override fun getGithubFromRoom() = githubLastSearchDao.getGithub()
    override fun deleteGithubFromRoom(lastSearchRoom: LastSearchRoom) = githubLastSearchDao.deleteGithub(lastSearchRoom)
    override fun deleteAllGithubFromRoom() = githubLastSearchDao.deleteAllGithub()
}
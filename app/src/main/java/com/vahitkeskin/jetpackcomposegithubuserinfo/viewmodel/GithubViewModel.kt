package com.vahitkeskin.jetpackcomposegithubuserinfo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vahitkeskin.jetpackcomposegithubuserinfo.dao.LastSearchRoom
import com.vahitkeskin.jetpackcomposegithubuserinfo.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubViewModel @Inject constructor(
    private val repo: GithubRepository
) : ViewModel() {

    var github by mutableStateOf(emptyList<LastSearchRoom>())

    fun addLastSearch(lastSearchRoom: LastSearchRoom) = viewModelScope.launch(Dispatchers.IO) {
        repo.addBookToRoom(lastSearchRoom)
    }

    fun getGithub() = viewModelScope.launch {
        repo.getGithubFromRoom().collect { dbGithub ->
            github = dbGithub
        }
    }

    fun deleteGithub(lastSearchRoom: LastSearchRoom) = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteGithubFromRoom(lastSearchRoom)
    }

    fun deleteAllGithub() = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteAllGithubFromRoom()
    }

}
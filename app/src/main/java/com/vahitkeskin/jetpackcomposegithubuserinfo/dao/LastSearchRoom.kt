package com.vahitkeskin.jetpackcomposegithubuserinfo.dao

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vahitkeskin.jetpackcomposegithubuserinfo.utils.Constants.Companion.TABLE_LAST_SEARCH

@Entity(tableName = TABLE_LAST_SEARCH, indices = [Index(value = ["userName", "userImage"], unique = true)])
data class LastSearchRoom(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var userName: String,
    var userImage: String,
)
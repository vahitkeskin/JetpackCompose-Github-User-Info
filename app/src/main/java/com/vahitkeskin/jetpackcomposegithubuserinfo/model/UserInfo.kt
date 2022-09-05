package com.vahitkeskin.jetpackcomposegithubuserinfo.model

data class UserInfo(
    var userImage: String ?= null,
    var userName: String ?= null,
    var userLogin: String ?= null,
    var userBio: String ?= null,
    var userPublicRepos: String ?= null,
    var userFollowers: String ?= null,
    var userFollowing: String ?= null,
    var dataSuccess: Boolean ?= null,
    var searchNewUser: Boolean ?= null,
)

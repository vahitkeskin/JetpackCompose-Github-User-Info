package com.vahitkeskin.jetpackcomposegithubuserinfo.model

data class RepositoriesModel(
    var name: String ?= null,
    var language: String ?= null,
    var html_url: String ?= null,
    var pushed_at: String ?= null,
    var visibility: String ?= null
)

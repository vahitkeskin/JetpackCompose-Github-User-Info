package com.vahitkeskin.jetpackcomposegithubuserinfo.utils

fun githubTabTitle(userPublicRepos: String): List<String> {
    return listOf(
        "Overview",
        "Repositories $userPublicRepos",
        "Projects",
        "Packages",
        "Stars"
    )
}
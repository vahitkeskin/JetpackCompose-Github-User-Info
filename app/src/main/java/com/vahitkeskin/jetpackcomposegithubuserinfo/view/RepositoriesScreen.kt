package com.vahitkeskin.jetpackcomposegithubuserinfo.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vahitkeskin.jetpackcomposegithubuserinfo.api.Service
import com.vahitkeskin.jetpackcomposegithubuserinfo.model.ReposItem
import com.vahitkeskin.jetpackcomposegithubuserinfo.model.RepositoriesModel
import com.vahitkeskin.jetpackcomposegithubuserinfo.utils.goToLink
import com.vahitkeskin.jetpackcomposegithubuserinfo.utils.updatedOn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

@Composable
fun RepositoriesScreen(name: String) {
    val userReposList by remember { mutableStateOf(ArrayList<RepositoriesModel>()) }
    var reposState by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (name.isNotEmpty()) {
            val call = Service().getUsers().userRepos(user = name)
            call.enqueue(object : Callback<List<ReposItem>> {
                override fun onResponse(
                    call: Call<List<ReposItem>>,
                    response: Response<List<ReposItem>>
                ) {
                    if (response.isSuccessful) {
                        userReposList.clear()
                        response.body()?.forEach {
                            userReposList.add(
                                RepositoriesModel(
                                    it.name,
                                    it.language,
                                    it.html_url,
                                    it.pushed_at,
                                    it.visibility
                                )
                            )
                            reposState = true
                        }
                    }
                }

                override fun onFailure(call: Call<List<ReposItem>>, t: Throwable) {
                    Timber.d("onFailure error ${t.message}")
                }
            })
        }

        if (reposState) {
            LazyColumn {
                items(
                    items = userReposList,
                    itemContent = {
                        ReposItem(repositoriesModel = it) { html_url ->
                            html_url.goToLink(context)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ReposItem(repositoriesModel: RepositoriesModel, clickItem: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                clickItem.invoke(repositoriesModel.html_url.orEmpty())
            }
            .fillMaxSize()
    ) {
        Text(
            text = repositoriesModel.name.orEmpty(),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Row {
            Text(text = repositoriesModel.language.orEmpty(), color = Color.LightGray)
            Text(
                color = Color.Gray,
                modifier = Modifier.padding(start = 10.dp),
                text = "Update on ${repositoriesModel.pushed_at.orEmpty().updatedOn()}"
            )
        }
    }
}
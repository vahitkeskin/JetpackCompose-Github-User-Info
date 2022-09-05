package com.vahitkeskin.jetpackcomposegithubuserinfo.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.palette.graphics.Palette
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.palette.BitmapPalette
import com.vahitkeskin.jetpackcomposegithubuserinfo.R
import com.vahitkeskin.jetpackcomposegithubuserinfo.api.Service
import com.vahitkeskin.jetpackcomposegithubuserinfo.dao.LastSearchRoom
import com.vahitkeskin.jetpackcomposegithubuserinfo.model.Users
import com.vahitkeskin.jetpackcomposegithubuserinfo.ui.theme.GithubColorCode
import com.vahitkeskin.jetpackcomposegithubuserinfo.utils.githubTabIcon
import com.vahitkeskin.jetpackcomposegithubuserinfo.utils.githubTabTitle
import com.vahitkeskin.jetpackcomposegithubuserinfo.utils.goToLink
import com.vahitkeskin.jetpackcomposegithubuserinfo.viewmodel.GithubViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(){
    val context = LocalContext.current
    val viewModel: GithubViewModel = hiltViewModel()

    var userImage by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var userLogin by remember { mutableStateOf("") }
    var userBio by remember { mutableStateOf("") }
    var userUrl by remember { mutableStateOf("") }

    var userPublicRepos by remember { mutableStateOf("") }
    var userFollowers by remember { mutableStateOf("") }
    var userFollowing by remember { mutableStateOf("") }
    var dataSuccess by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchNewUser by remember { mutableStateOf(false) }
    var inputUserName by remember { mutableStateOf(TextFieldValue(text = "")) }
    var lastSearch by remember { mutableStateOf("") }
    var lastSearchState by remember { mutableStateOf(false) }

    var menuItemSelectedSearch by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    viewModel.getGithub()

    val pagerState = rememberPagerState(
        pageCount = githubTabTitle(userPublicRepos).size,
        initialOffscreenLimit = 2,
        infiniteLoop = true,
        initialPage = 0
    )

    Column(
        modifier = Modifier
            .background(GithubColorCode)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }) {
                Column {
                    OutlinedTextField(
                        keyboardActions = KeyboardActions(
                            onDone = {
                                menuItemSelectedSearch = true
                                searchNewUser = true
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = inputUserName,
                        onValueChange = { newText ->
                            inputUserName = newText
                        },
                        label = {
                            Text(
                                text = "User Github Name...",
                                color = Color.LightGray
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.LightGray
                            )
                        },
                        trailingIcon = {
                            if (inputUserName.text.isNotEmpty()) {
                                expanded = false
                                Icon(
                                    modifier = Modifier.clickable {
                                        lastSearchState = false
                                        inputUserName = TextFieldValue(
                                            text = "",
                                            selection = TextRange(0)
                                        )
                                    },
                                    painter = painterResource(id = R.drawable.ic_baseline_cancel_24),
                                    contentDescription = null,
                                    tint = Color.LightGray,
                                )
                            } else {
                                IconButton(
                                    onClick = { }, modifier = Modifier.clearAndSetSemantics { }) {
                                    Icon(
                                        Icons.Filled.ArrowDropDown,
                                        "Trailing icon for exposed dropdown menu",
                                        Modifier
                                            .rotate(if (expanded && viewModel.github.isNotEmpty()) 180f else 360f)
                                            .border(
                                                1.dp, Color.LightGray,
                                                CircleShape
                                            ),
                                        tint = Color.LightGray
                                    )
                                }
                            }
                        }
                    )
                    if (lastSearchState) {
                        Text(
                            modifier = Modifier.padding(start = 53.dp),
                            text = "Sending request with same username",
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                        )
                    }
                }
                ExposedDropdownMenu(
                    expanded = expanded && viewModel.github.isNotEmpty(),
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    viewModel.github.forEachIndexed { index, selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                val value = inputUserName.text.plus(selectionOption.userName)
                                inputUserName = TextFieldValue(
                                    text = value,
                                    selection = TextRange(value.length)
                                )
                                menuItemSelectedSearch = true
                                searchNewUser = true
                                expanded = false
                            }
                        ) {
                            Column {
                                if (index == 0) {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 10.dp, bottom = 10.dp)
                                            .fillMaxWidth(),
                                        contentAlignment = Alignment.TopEnd
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .clickable {
                                                    viewModel.deleteAllGithub()
                                                    Toast.makeText(context, "Clear All", Toast.LENGTH_SHORT).show()
                                                },
                                            text = "Clear All",
                                            textAlign = TextAlign.End,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                                //UserImage
                                Row(
                                    modifier = Modifier.padding(bottom = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    GlideImage(
                                        imageModel = selectionOption.userImage,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, Color.DarkGray, CircleShape)
                                    )
                                    Text(
                                        modifier = Modifier
                                            .padding(start = 10.dp)
                                            .weight(1f),
                                        text = selectionOption.userName
                                    )
                                    Icon(
                                        modifier = Modifier.clickable {
                                            viewModel.deleteGithub(selectionOption)
                                            Toast.makeText(context, "Click $selectionOption", Toast.LENGTH_SHORT).show()
                                        },
                                        painter = painterResource(id = R.drawable.ic_baseline_cancel_24),
                                        contentDescription = null,
                                        tint = Color.LightGray,
                                    )
                                }
                            }
                        }
                    }
                }
            }
            FloatingActionButton(
                backgroundColor = GithubColorCode,
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .border(1.dp, Color.LightGray, CircleShape),
                onClick = {
                    val newInputUserName =
                        if (inputUserName.text.isNullOrEmpty()) "vahitkeskin" else inputUserName.text

                    if (lastSearch == newInputUserName) {
                        lastSearchState = true
                    } else {
                        lastSearchState = false
                        inputUserName = TextFieldValue(
                            text = newInputUserName,
                            selection = TextRange(newInputUserName.length)
                        )
                        searchNewUser = true

                        //Menu item selected
                        menuItemSelectedSearch = true

                        lastSearch = newInputUserName
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_github_icon),
                    contentDescription = null,
                    tint = Color.LightGray
                )
            }
        }

        if (menuItemSelectedSearch && searchNewUser) {
            keyboardController?.hide()
            val call = Service().getUsers()
                .userInfo(user = inputUserName.text)
            call.enqueue(object : Callback<Users> {
                override fun onResponse(call: Call<Users>, response: Response<Users>) {
                    if (response.isSuccessful) {
                        userImage = response.body()?.avatar_url.orEmpty()
                        userName = response.body()?.name.orEmpty()
                        userLogin = response.body()?.login.orEmpty()
                        userBio = response.body()?.bio.orEmpty()
                        userUrl = response.body()?.html_url.orEmpty()
                        userPublicRepos = response.body()?.public_repos.toString()
                        userFollowers = response.body()?.followers.toString()
                        userFollowing = response.body()?.following.toString()
                        dataSuccess = true
                        searchNewUser = false

                        //Data Success -> ROOM
                        val lastSearchRoom = LastSearchRoom(0,inputUserName.text, userImage)
                        viewModel.addLastSearch(lastSearchRoom)
                    } else {
                        searchNewUser = false
                        Toast.makeText(context, "The username ${inputUserName.text} could not be found!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Users>, t: Throwable) {
                    Timber.d("onFailure error: ${t.message}")
                }

            })
            menuItemSelectedSearch = false
        }

        var palette by remember { mutableStateOf<Palette?>(null) }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (dataSuccess) {
                Column(
                    modifier = Modifier.background(GithubColorCode)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .widthIn(min = 32.dp)
                            .padding(10.dp)
                    ) {
                        GlideImage(
                            contentScale = ContentScale.Crop,
                            imageModel = userImage,
                            bitmapPalette = BitmapPalette {
                                palette = it
                                palette?.darkVibrantSwatch?.let {
                                    Timber.d("RGB Color code for $userName code: ${it.rgb}")
                                }
                            },
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(
                                    2.dp,
                                    palette?.darkVibrantSwatch?.let { Color(it.rgb) } ?: Color.Gray,
                                    CircleShape)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                modifier = Modifier
                                    .background(GithubColorCode)
                                    .padding(start = 10.dp, end = 10.dp)
                                    .defaultMinSize(
                                        minWidth = ButtonDefaults.MinWidth,
                                        minHeight = 10.dp
                                    ),
                                onClick = {
                                    userUrl.goToLink(context)
                                },
                                contentPadding = PaddingValues(10.dp),
                                border = BorderStroke(1.dp, Color.LightGray),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = GithubColorCode,
                                    contentColor = Color.LightGray
                                )
                            ) {
                                Text(text = "Profile")
                            }
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = userFollowers, color = Color.LightGray)
                                Text(
                                    text = "followers",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color.LightGray
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = userFollowing, color = Color.LightGray)
                                Text(
                                    text = "following",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = userName,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.LightGray
                        )
                        Text(
                            text = userLogin,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White
                        )
                        Text(text = userBio, modifier = Modifier.fillMaxWidth(), color = Color.Gray)
                    }
                    //PagerState
                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = {
                            TabRowDefaults.Indicator(
                                Modifier.pagerTabIndicatorOffset(pagerState, it)
                            )
                        }) {
                        githubTabTitle(userPublicRepos).forEachIndexed { index, s ->
                            Tab(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            painterResource(id = githubTabIcon[index]),
                                            modifier = Modifier.padding(end = 10.dp),
                                            contentDescription = null
                                        )
                                        Text(s)
                                    }
                                },
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            )
                        }
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                    ) { index ->
                        if (index == 1) {
                            RepositoriesScreen(inputUserName.text)
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = githubTabTitle(userPublicRepos)[index] + " under development...",
                                    color = Color.White
                                )
                            }
                        }

                    }
                }
            } else {
                if (searchNewUser) CircularProgressIndicator()
            }
        }
    }
}
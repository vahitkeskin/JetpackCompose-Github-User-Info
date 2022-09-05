package com.vahitkeskin.jetpackcomposegithubuserinfo.di

import android.content.Context
import androidx.room.Room
import com.vahitkeskin.jetpackcomposegithubuserinfo.utils.Constants.Companion.TABLE_LAST_SEARCH
import com.vahitkeskin.jetpackcomposegithubuserinfo.api.LastSearchDatabase
import com.vahitkeskin.jetpackcomposegithubuserinfo.dao.GithubLastSearchDao
import com.vahitkeskin.jetpackcomposegithubuserinfo.repository.GithubRepository
import com.vahitkeskin.jetpackcomposegithubuserinfo.repository.GithubRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideGithubDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        LastSearchDatabase::class.java,
        TABLE_LAST_SEARCH
    ).build()

    @Provides
    fun provideGithubDao(
        lastSearchDatabase: LastSearchDatabase
    ) = lastSearchDatabase.githubDao()

    @Provides
    fun provideGithubRepository(
        githubLastSearchDao: GithubLastSearchDao
    ) : GithubRepository = GithubRepositoryImpl(
        githubLastSearchDao = githubLastSearchDao
    )
}
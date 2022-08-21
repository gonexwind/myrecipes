package com.gonexwind.myrecipes.core.data

import com.gonexwind.myrecipes.core.data.local.LocalDataSource
import com.gonexwind.myrecipes.core.data.remote.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {

    val remote = remoteDataSource
    val local = localDataSource
}
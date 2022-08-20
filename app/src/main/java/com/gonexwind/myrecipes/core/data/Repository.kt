package com.gonexwind.myrecipes.core.data

import com.gonexwind.myrecipes.core.data.remote.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remote: RemoteDataSource
) {
}
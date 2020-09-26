package com.dimi.moviedatabase.presentation.images

import android.content.pm.ActivityInfo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FullScreenOrientation @Inject constructor() {
    var orientation: Int = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}
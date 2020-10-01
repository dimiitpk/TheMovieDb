package com.dimi.moviedatabase.presentation.images

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.presentation.common.*
import com.dimi.moviedatabase.util.Constants
import kotlinx.coroutines.*

@FlowPreview
@ExperimentalCoroutinesApi
class FullScreenImageActivity : BaseActivity(), Permissions {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster_images)

        setupNavigationGraphArguments()
    }

    private fun setupNavigationGraphArguments() {
        supportFragmentManager.findFragmentById(R.id.images_fragment_container)?.findNavController()
            ?.setGraph(R.navigation.images_nav, intent.extras)

        intent.extras?.let { bundle ->
            requestedOrientation = bundle.getInt("orientation", 1)
        }
    }

    override fun isStoragePermissionGranted(): Boolean {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                displayPermissionsRequiredDialog(
                    "Permission required to save and send photos from the Web.",
                    positiveButtonFun = {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                    })
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }
            return false
        } else {
            return true
        }
    }
}
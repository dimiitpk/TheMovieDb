package com.dimi.moviedatabase.presentation.images

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.dimi.moviedatabase.presentation.images.fragments.ImageFullScreenFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class ImagesFragmentFactory @Inject constructor(
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            ImageFullScreenFragment::class.java.name -> {
                ImageFullScreenFragment()
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}
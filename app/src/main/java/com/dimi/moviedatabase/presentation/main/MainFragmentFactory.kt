package com.dimi.moviedatabase.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.dimi.moviedatabase.presentation.main.home.HomeFragment
import com.dimi.moviedatabase.presentation.main.search.SearchFragment
import com.dimi.moviedatabase.presentation.main.view.ViewMediaFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class MainFragmentFactory @Inject constructor(
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            HomeFragment::class.java.name -> {
                HomeFragment()
            }
            SearchFragment::class.java.name -> {
                SearchFragment()
            }
            ViewMediaFragment::class.java.name -> {
                ViewMediaFragment()
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}
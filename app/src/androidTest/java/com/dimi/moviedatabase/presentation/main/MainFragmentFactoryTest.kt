package com.dimi.moviedatabase.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.dimi.moviedatabase.presentation.common.UIController
import com.dimi.moviedatabase.presentation.main.home.HomeFragment
import com.dimi.moviedatabase.presentation.main.search.SearchFragment
import com.dimi.moviedatabase.presentation.main.view.ViewMediaFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
class MainFragmentFactoryTest @Inject constructor(
) : FragmentFactory() {

    lateinit var uiController: UIController

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            HomeFragment::class.java.name -> {
                val fragment = HomeFragment()
                if(::uiController.isInitialized){
                    fragment.uiController = uiController
                }
                fragment
            }
            SearchFragment::class.java.name -> {
                val fragment = SearchFragment()
                if(::uiController.isInitialized){
                    fragment.uiController = uiController
                }
                fragment
            }
            ViewMediaFragment::class.java.name -> {
                val fragment = ViewMediaFragment()
                if(::uiController.isInitialized){
                    fragment.uiController = uiController
                }
                fragment
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}
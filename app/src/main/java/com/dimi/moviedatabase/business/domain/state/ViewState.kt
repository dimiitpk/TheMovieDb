package com.dimi.moviedatabase.business.domain.state

import com.dimi.moviedatabase.business.domain.model.Media

interface ViewState {

    /**
     *  Use this function to set new incoming data in useCases
     *  Every ViewState need to override this method and write his own logic
     *  Must use pairs and keys of possible same values with different logic
     */
    fun setData(vararg hashMap: HashMap<String, Any>)
}
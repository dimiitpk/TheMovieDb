package com.dimi.moviedatabase.business.domain.state


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dimi.moviedatabase.business.domain.state.MessageType
import com.dimi.moviedatabase.business.domain.state.Response
import com.dimi.moviedatabase.business.domain.state.StateMessage
import com.dimi.moviedatabase.business.domain.state.UIComponentType
import kotlinx.android.parcel.IgnoredOnParcel
import java.lang.IndexOutOfBoundsException

class MessageStack: ArrayList<StateMessage>() {

    @IgnoredOnParcel
    private val _stateMessage: MutableLiveData<StateMessage?> = MutableLiveData()

    @IgnoredOnParcel
    val stateMessage: LiveData<StateMessage?>
        get() = _stateMessage

    fun isStackEmpty(): Boolean{
        return size == 0
    }

    override fun addAll(elements: Collection<StateMessage>): Boolean {
        for(element in elements){
            add(element)
        }
        return true
    }

    override fun add(element: StateMessage): Boolean {
        if(this.contains(element)){
            return false
        }
        val transaction = super.add(element)
        if(this.size == 1){
            setStateMessage(stateMessage = element)
        }
        return transaction
    }

    override fun removeAt(index: Int): StateMessage {
        try{
            val transaction = super.removeAt(index)
            if(this.size > 0){
                setStateMessage(stateMessage = this[0])
            }
            else{
                Log.d("MessageStack", "stack is empty: ")
                setStateMessage(null)
            }
            return transaction
        }catch (e: IndexOutOfBoundsException){
            setStateMessage(null)
            e.printStackTrace()
        }
        return StateMessage(
            Response(
                message = "does nothing",
                uiComponentType = UIComponentType.None,
                messageType = MessageType.None
            )
        )
    }

    private fun setStateMessage(stateMessage: StateMessage?){
        _stateMessage.value = stateMessage
    }
}



























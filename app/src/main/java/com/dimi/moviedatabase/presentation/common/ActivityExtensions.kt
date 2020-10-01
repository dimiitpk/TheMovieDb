package com.dimi.moviedatabase.presentation.common

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.dimi.moviedatabase.R
import com.dimi.moviedatabase.business.domain.state.StateMessageCallback

fun AppCompatActivity.displayToast(
    message:String,
    stateMessageCallback: StateMessageCallback? = null
){
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    stateMessageCallback?.removeMessageFromStack()
}

fun AppCompatActivity.displayPermissionsRequiredDialog(
    rationaleMessage: String,
    positiveButtonFun: () -> Unit
) : MaterialDialog {
    return MaterialDialog(this)
        .show{
            title(R.string.permission_required)
            message(text = rationaleMessage)
            positiveButton(R.string.allow){
                positiveButtonFun()
                dismiss()
            }
            negativeButton(R.string.deny) {
                dismiss()
            }
            cancelable(false)
        }
}

fun AppCompatActivity.displaySuccessDialog(
    message: String?,
    stateMessageCallback: StateMessageCallback? = null
): MaterialDialog {
    return MaterialDialog(this)
        .show{
            title(R.string.text_success)
            message(text = message)
            positiveButton(R.string.text_ok){
                stateMessageCallback?.removeMessageFromStack()
                dismiss()
            }
            cancelable(false)
        }
}

fun AppCompatActivity.displayErrorDialog(
    message: String?,
    stateMessageCallback: StateMessageCallback? = null
): MaterialDialog {
    return MaterialDialog(this)
        .show{
            title(R.string.text_error)
            message(text = message)
            positiveButton(R.string.text_ok){
                stateMessageCallback?.removeMessageFromStack()
                dismiss()
            }
            cancelable(false)
        }
}

fun AppCompatActivity.displayInfoDialog(
    message: String?,
    stateMessageCallback: StateMessageCallback? = null
): MaterialDialog {
    return MaterialDialog(this)
        .show{
            title(R.string.text_info)
            message(text = message)
            positiveButton(R.string.text_ok){
                stateMessageCallback?.removeMessageFromStack()
                dismiss()
            }
            cancelable(false)
        }
}
package com.angler.anglernavimultilang

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

object ExitAlertUtils {
    fun showExitAlert(activity: Activity, onExitConfirmed: () -> Unit) {
        val builder = AlertDialog.Builder(activity)

        builder.apply {
            setTitle("Exit App")
            setMessage("Are you sure you want to exit?")
            setPositiveButton("Yes") { dialog, _ ->
                onExitConfirmed.invoke()
                dialog.dismiss()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
        }

        val exitAlert = builder.create()
        exitAlert.show()
    }
}

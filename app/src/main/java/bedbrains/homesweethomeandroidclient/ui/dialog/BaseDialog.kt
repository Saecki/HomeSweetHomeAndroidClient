package bedbrains.homesweethomeandroidclient.ui.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import bedbrains.homesweethomeandroidclient.Res

abstract class BaseDialog(protected val context: Context) {

    var title = ""

    protected val dialogBuilder = AlertDialog.Builder(context)
        .setNegativeButton(android.R.string.cancel) { _, _ -> }

    protected lateinit var dialog: AlertDialog
    protected var positveButtonEnabled: Boolean
        get() = dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled
        set(value) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = value
        }

    fun create(): AlertDialog {
        dialogBuilder.setTitle(title)

        dialog = dialogBuilder.create()
        dialog.setOnShowListener {
            onShow()
        }

        onCreate()

        return dialog
    }

    fun show() {
        create().show()
    }

    protected abstract fun onCreate()

    protected open fun onShow() {}
}

fun <T : BaseDialog> T.title(title: Int): T {
    this.title = Res.resources.getString(title)
    return this
}

fun <T : BaseDialog> T.title(title: String): T {
    this.title = title
    return this
}


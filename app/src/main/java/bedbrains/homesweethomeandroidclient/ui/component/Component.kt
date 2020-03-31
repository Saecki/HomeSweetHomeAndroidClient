package bedbrains.homesweethomeandroidclient.ui.component

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.rest.Resp

fun SwipeRefreshLayout.refresh(lifecycleOwner: LifecycleOwner, context: Context?) {
    this.isRefreshing = true

    val resp = DataRepository.fetchUpdates()

    resp.observe(lifecycleOwner, Observer {
        when (it) {
            Resp.AWAITING -> Unit
            Resp.SUCCESS -> {
                this.isRefreshing = false
                resp.removeObservers(lifecycleOwner)
            }
            Resp.FAILURE -> {
                Toast.makeText(context, R.string.resp_update_error, Toast.LENGTH_LONG).show()
                this.isRefreshing = false
                resp.removeObservers(lifecycleOwner)
            }
        }
    })
}
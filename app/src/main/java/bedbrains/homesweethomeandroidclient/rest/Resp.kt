package bedbrains.homesweethomeandroidclient.rest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import bedbrains.homesweethomeandroidclient.MainActivity

enum class Resp {
    AWAITING,
    SUCCESS,
    FAILURE,
}

fun resultOf(vararg responses: LiveData<Resp>): LiveData<Resp> {
    val responded = MutableLiveData(Resp.AWAITING)

    for (r in responses) {
        r.observe(MainActivity.activity, Observer {
            when (it) {
                Resp.AWAITING -> Unit
                Resp.FAILURE -> {
                    responded.value = Resp.FAILURE
                    responses.forEach { resp -> resp.removeObservers(MainActivity.activity) }
                }
                Resp.SUCCESS -> {
                    if (responses.indexOfFirst { resp -> resp.value != Resp.SUCCESS } == -1) {
                        responded.value = Resp.SUCCESS
                    }
                    r.removeObservers(MainActivity.activity)
                }
            }
        })
    }

    return responded
}
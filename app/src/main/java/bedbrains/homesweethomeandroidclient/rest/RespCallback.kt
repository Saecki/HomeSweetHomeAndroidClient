package bedbrains.homesweethomeandroidclient.rest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RespCallback<T> : Callback<T> {

    private val responded = MutableLiveData(Resp.AWAITING)
    private var resp: T? = null
    private lateinit var onResponse: (T?) -> Unit

    override fun onFailure(call: Call<T>, t: Throwable) {
        resp = null
        onResponse?.invoke(resp)
        responded.value = Resp.FAILURE
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        resp = response.body()
        onResponse?.let { it(resp) }
        responded.value = when (resp) {
            null -> {
                Resp.FAILURE
            }
            else -> {
                Resp.SUCCESS
            }
        }
    }

    fun enqueue(call: Call<T>?, onResponse: (T?) -> Unit = {}): LiveData<Resp> {
        this.onResponse = onResponse
        if (call == null)
            responded.value = Resp.FAILURE
        else
            call.enqueue(this)

        return responded
    }
}
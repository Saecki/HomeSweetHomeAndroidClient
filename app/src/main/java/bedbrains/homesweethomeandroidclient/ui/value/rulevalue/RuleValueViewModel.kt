package bedbrains.homesweethomeandroidclient.ui.value.rulevalue

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.shared.datatypes.rules.RuleValue

class RuleValueViewModel : ViewModel() {
    val value: MutableLiveData<RuleValue?> = MutableLiveData(null)
    var initialCreation = true

    fun observe(lifecycleOwner: LifecycleOwner, uid: String) {
        DataRepository.values.observe(lifecycleOwner, Observer {
            val matchingValue = it.find { value -> value.uid == uid }

            value.value = when (matchingValue) {
                is RuleValue -> matchingValue
                else -> null
            }
        })
    }
}
package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.shared.datatypes.rules.WeeklyRule
import bedbrains.shared.datatypes.time.WeeklyTimeSpan

class WeeklyRuleViewModel : ViewModel() {
    val rule: MutableLiveData<WeeklyRule?> = MutableLiveData(null)
    var previewTimeSpan: WeeklyTimeSpan? = null
    var initialCreation = true

    fun observe(lifecycleOwner: LifecycleOwner, uid: String) {
        DataRepository.rules.observe(lifecycleOwner, Observer {
            val matchingRule = it.find { rule -> rule.uid == uid }

            rule.value = when (matchingRule) {
                is WeeklyRule -> matchingRule
                else -> null
            }
        })
    }
}
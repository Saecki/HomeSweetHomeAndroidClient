package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.shared.datatypes.rules.WeeklyRule
import bedbrains.shared.datatypes.rules.WeeklyTimeSpan

class WeeklyTimeSpanViewModel : ViewModel() {
    val timeSpan: MutableLiveData<WeeklyTimeSpan?> = MutableLiveData(null)
    var initialCreation = true

    fun observe(lifecycleOwner: LifecycleOwner, ruleUid: String, timeSpanUid: String) {
        DataRepository.rules.observe(lifecycleOwner, Observer {
            val matchingRule = it.find { rule -> rule.uid == ruleUid }
            val matchingTimeSpan = when (matchingRule) {
                is WeeklyRule -> matchingRule.timeSpans.find { it.uid == timeSpanUid }
                else -> null
            }

            timeSpan.value = matchingTimeSpan
        })
    }
}
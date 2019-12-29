package bedbrains.homesweethomeandroidclient.ui.value

import androidx.lifecycle.ViewModel
import bedbrains.shared.datatypes.Temperature
import bedbrains.shared.datatypes.rules.RuleValue

class ValuesViewModel : ViewModel() {
    val values = listOf(
        RuleValue.UNSPECIFIED,
        RuleValue("lkdjlgjd", "nice", Temperature(24.0, Temperature.Unit.CELSIUS), true)
    )
}
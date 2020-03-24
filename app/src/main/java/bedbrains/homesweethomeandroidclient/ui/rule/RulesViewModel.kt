package bedbrains.homesweethomeandroidclient.ui.rule

import androidx.lifecycle.ViewModel
import bedbrains.homesweethomeandroidclient.DataRepository

class RulesViewModel : ViewModel() {
    val rules = DataRepository.rules
}
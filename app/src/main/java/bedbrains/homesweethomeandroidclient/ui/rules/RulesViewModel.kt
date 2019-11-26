package bedbrains.homesweethomeandroidclient.ui.rules

import androidx.lifecycle.ViewModel
import bedbrains.shared.datatypes.rules.Rule
import bedbrains.shared.datatypes.rules.WeeklyRule

class RulesViewModel : ViewModel() {
    val rules = listOf<Rule>(
        WeeklyRule("klfjsojf", "normal"),
        WeeklyRule("asdf8urifojs", "vacation")
    )
}
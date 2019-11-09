package saecki.homesweethomeandroidclient.ui.rules

import androidx.lifecycle.ViewModel
import saecki.homesweethomeandroidclient.datatypes.rules.Rule
import saecki.homesweethomeandroidclient.datatypes.rules.WeeklyRule

class RulesViewModel : ViewModel() {
    val rules = listOf<Rule>(
        WeeklyRule("klfjsojf", "normal"),
        WeeklyRule("asdf8urifojs", "vacation")
    )
}
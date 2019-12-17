package bedbrains.homesweethomeandroidclient.ui.rule.rulevalue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import bedbrains.homesweethomeandroidclient.R

class RuleValueFragment() : Fragment() {
    private lateinit var ruleValueViewModel: RuleValueViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ruleValueViewModel = ViewModelProviders.of(this).get(RuleValueViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_rule_value, container, false)
        val heating = root.findViewById<TextView>(R.id.heating)
        val light = root.findViewById<TextView>(R.id.light)

        return root
    }
}
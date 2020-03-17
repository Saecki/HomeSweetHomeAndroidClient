package bedbrains.homesweethomeandroidclient.ui.rule.rulevalue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bedbrains.homesweethomeandroidclient.databinding.FragmentRuleValueBinding

class RuleValueFragment() : Fragment() {
    private val ruleValueViewModel: RuleValueViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentRuleValueBinding.inflate(inflater)
        val heating = binding.heating
        val light = binding.light

        heating.setOnClickListener {
            //TODO
        }

        light.setOnClickListener {
            //TODO
        }

        return binding.root
    }
}
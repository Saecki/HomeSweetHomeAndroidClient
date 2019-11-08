package saecki.homesweethomeandroidclient.ui.rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import saecki.homesweethomeandroidclient.R

class RulesFragment : Fragment() {

    lateinit var rulesViewModel: RulesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModelProviders.of(this).get(RulesViewModel::class.java)
        val linearLayoutManager = LinearLayoutManager(context)
        val root = inflater.inflate(R.layout.fragment_rules, container, false)

        return root
    }

}
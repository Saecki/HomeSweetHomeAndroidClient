package bedbrains.homesweethomeandroidclient.ui.value

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R

class ValuesFragment : Fragment() {

    lateinit var valuesViewModel: ValuesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        valuesViewModel = ViewModelProviders.of(this).get(ValuesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_values, container, false)
        val values = root.findViewById<RecyclerView>(R.id.values)
        val linearLayoutManager = LinearLayoutManager(context)
        val ruleValueListAdapter = RuleValueListAdapter(valuesViewModel.values)

        values.layoutManager = linearLayoutManager
        values.adapter = ruleValueListAdapter

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.values, menu)
    }

}
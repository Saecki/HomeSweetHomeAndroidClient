package bedbrains.homesweethomeandroidclient.ui.value.rulevalue

import android.os.Bundle
import android.view.*
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentRuleValueBinding
import bedbrains.homesweethomeandroidclient.ui.component.refresh
import bedbrains.homesweethomeandroidclient.ui.dialog.BaseInputDialog
import bedbrains.homesweethomeandroidclient.ui.dialog.onFinished
import bedbrains.homesweethomeandroidclient.ui.dialog.text
import bedbrains.homesweethomeandroidclient.ui.dialog.title

class RuleValueFragment() : Fragment() {
    private val ruleValueViewModel: RuleValueViewModel by viewModels()

    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var heating: TextView
    lateinit var light: Switch
    lateinit var ruleValueView: RuleValueView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        if (ruleValueViewModel.initialCreation) {
            val uid = arguments?.getString(resources.getString(R.string.uid))

            if (uid == null) {
                findNavController().popBackStack()
                Toast.makeText(context, R.string.resp_item_no_longer_exists, Toast.LENGTH_LONG).show()
            } else {
                ruleValueViewModel.observe(viewLifecycleOwner, uid)
            }
        }

        val binding = FragmentRuleValueBinding.inflate(inflater)
        swipeRefreshLayout = binding.swipeRefreshLayout
        ruleValueView = RuleValueView(binding.ruleValue, context)

        ruleValueViewModel.value.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                findNavController().popBackStack()
                Toast.makeText(context, R.string.resp_item_no_longer_exists, Toast.LENGTH_LONG).show()
            } else {
                MainActivity.toolbar.title = it.name

                ruleValueView.bind(it) { value ->
                    DataRepository.updateValue(value)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.refresh(viewLifecycleOwner, context)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rule_value, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> swipeRefreshLayout.refresh(viewLifecycleOwner, context)
            R.id.action_rename -> showRenameDialog(ruleValueViewModel.value.value!!.name)
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }

    private fun showRenameDialog(text: String) {
        BaseInputDialog(context!!)
            .title(R.string.action_rename)
            .text(text)
            .onFinished {
                DataRepository.updateValue(ruleValueViewModel.value.value!!.apply {
                    name = it
                })
            }
            .show()
    }
}
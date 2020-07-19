package bedbrains.homesweethomeandroidclient.ui.device.light

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.FragmentLightBinding
import bedbrains.homesweethomeandroidclient.ui.component.refresh
import bedbrains.homesweethomeandroidclient.ui.dialog.*
import bedbrains.platform.DataProvider
import bedbrains.shared.datatypes.rules.Rule
import com.google.android.material.chip.Chip

class LightFragment : Fragment() {

    private val lightViewModel: LightViewModel by viewModels()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var binding: FragmentLightBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        if (lightViewModel.initialCreation) {
            val uid = arguments?.getString(resources.getString(R.string.uid))

            if (uid == null) {
                findNavController().popBackStack()
                Toast.makeText(context, R.string.resp_item_no_longer_exists, Toast.LENGTH_LONG)
                    .show()
            } else {
                lightViewModel.observe(viewLifecycleOwner, uid)
            }
        }

        binding = FragmentLightBinding.inflate(inflater)
        swipeRefreshLayout = binding.swipeRefreshLayout

        lightViewModel.light.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                findNavController().popBackStack()
                Toast.makeText(context, R.string.resp_item_no_longer_exists, Toast.LENGTH_LONG)
                    .show()
            } else {
                MainActivity.toolbar.title = it.name
                displayTags(it.tags)
                binding.room.text = it.room
                binding.light.isChecked = it.isOn
                binding.rule.text =
                    it.rule?.name ?: Res.resources.getString(R.string.action_select_a_rule)
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.refresh(viewLifecycleOwner, context)
        }

        binding.addChip.setOnClickListener {
            showAddTagDialog()
        }

        binding.room.setOnClickListener {
            showRoomDialog(lightViewModel.light.value!!.room)
        }

        binding.light.setOnCheckedChangeListener { _, isChecked ->
            DataRepository.updateDevice(lightViewModel.light.value!!.apply {
                isOn = isChecked
            })
        }

        binding.rule.setOnClickListener {
            showSelectRuleDialog(lightViewModel.light.value!!.rule)
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.heating, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> swipeRefreshLayout.refresh(viewLifecycleOwner, context)
            R.id.action_rename -> showRenameDialog(lightViewModel.light.value!!.name)
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }

    private fun displayTags(tags: Set<String>) {
        val chips = binding.tags.children.map { it as Chip }.toList()
        val removed = chips.filter { chip -> tags.none { tag -> chip.text == tag } }
        val added = tags.filter { tag -> chips.none { chip -> chip.text == tag } }

        removed.forEach {
            if (it.id != R.id.add_chip)
                binding.tags.removeView(it)
        }

        added.forEach { tag ->
            val chip = Chip(context)

            chip.isCloseIconVisible = true
            chip.text = tag
            chip.setOnCloseIconClickListener {
                showRemoveTagDialog(tag)
            }
            binding.tags.addView(chip, binding.tags.childCount - 1)
        }
    }

    private fun showRenameDialog(text: String) {
        InputDialog(requireContext())
            .title(R.string.action_rename)
            .text(text)
            .onFinished {
                DataRepository.updateDevice(lightViewModel.light.value!!.apply {
                    name = it
                })
            }
            .show()
    }

    private fun showAddTagDialog() {
        val suggestions = DataRepository.devices.value!!
            .flatMap { it.tags }
            .distinct()
            .sorted()

        SuggestionInputDialog(requireContext())
            .title(R.string.action_tag_new)
            .suggestions(suggestions)
            .validator { !lightViewModel.light.value!!.tags.contains(it) }
            .onFinished {
                DataRepository.updateDevice(lightViewModel.light.value!!.apply {
                    tags.add(it)
                })
            }
            .show()
    }

    private fun showRemoveTagDialog(tag: String) {
        val title = Res.resources.getString(R.string.confirmation_tag_delete).replace("$1", tag)

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                DataRepository.updateDevice(lightViewModel.light.value!!.apply {
                    tags.remove(tag)
                })
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun showRoomDialog(text: String) {
        val suggestions = DataRepository.devices.value!!
            .map { it.room }
            .distinct()
            .sorted()

        SuggestionInputDialog(requireContext())
            .title(R.string.room)
            .text(text)
            .suggestions(suggestions)
            .onFinished {
                DataRepository.updateDevice(lightViewModel.light.value!!.apply {
                    room = it
                })
            }
            .show()
    }

    private fun showSelectRuleDialog(rule: Rule?) {
        val options = DataProvider.rules.map { Pair(it.name, it.uid) }

        SelectionInputDialog<String>(requireContext())
            .title(R.string.rule)
            .options(DataProvider.rules.map { Pair(it.name, it.uid) })
            .selection(options.indexOfFirst { it.second == rule?.uid })
            .onFinishedSelection { _, uid ->
                DataRepository.updateDevice(lightViewModel.light.value!!.apply {
                    ruleUID = uid
                })
            }
            .show()
    }
}
package bedbrains.homesweethomeandroidclient.ui.device.heating

import android.os.Bundle
import android.text.InputType
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
import bedbrains.homesweethomeandroidclient.databinding.FragmentHeatingBinding
import bedbrains.homesweethomeandroidclient.ui.component.refresh
import bedbrains.homesweethomeandroidclient.ui.dialog.InputDialog
import com.google.android.material.chip.Chip

class HeatingFragment : Fragment() {

    private val heatingViewModel: HeatingViewModel by viewModels()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var binding: FragmentHeatingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        if (heatingViewModel.initialCreation) {
            val uid = arguments?.getString(resources.getString(R.string.uid))

            if (uid == null) {
                findNavController().popBackStack()
                Toast.makeText(context, R.string.resp_item_no_longer_exists, Toast.LENGTH_LONG).show()
            } else {
                heatingViewModel.observe(viewLifecycleOwner, uid)
            }
        }

        binding = FragmentHeatingBinding.inflate(inflater)
        swipeRefreshLayout = binding.swipeRefreshLayout

        heatingViewModel.heating.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                findNavController().popBackStack()
                Toast.makeText(context, R.string.resp_item_no_longer_exists, Toast.LENGTH_LONG).show()
            } else {
                MainActivity.toolbar.title = it.name
                displayTags(it.tags)
                binding.room.text = it.room
                binding.actualTemp.text = it.actualTemp.formatGlobal(true)
                binding.targetTemp.text = it.targetTemp.formatGlobal(true)
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.refresh(viewLifecycleOwner, context)
        }

        binding.addChip.setOnClickListener {
            showAddTagDialog()
        }

        binding.room.setOnClickListener {
            showRoomDialog(heatingViewModel.heating.value!!.room)
        }

        binding.targetTemp.setOnClickListener {
            showTargetTempDialog(heatingViewModel.heating.value!!.targetTemp.formatGlobal(false))
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.heating, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> swipeRefreshLayout.refresh(viewLifecycleOwner, context)
            R.id.action_rename -> showRenameDialog(heatingViewModel.heating.value!!.name)
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
        InputDialog.show(context!!, R.string.action_rename, text, invalidOptions = setOf(text)) {
            DataRepository.updateDevice(heatingViewModel.heating.value!!.apply {
                name = it
            })
        }
    }

    private fun showAddTagDialog() {
        val options = DataRepository.devices.value!!.flatMap {
            it.tags
        }.toSet()

        InputDialog.show(
            context!!,
            R.string.action_tag_new,
            options = options,
            invalidOptions = heatingViewModel.heating.value!!.tags
        ) {
            DataRepository.updateDevice(heatingViewModel.heating.value!!.apply {
                tags.add(it)
            })
        }
    }

    private fun showRemoveTagDialog(tag: String) {
        val title = Res.resources.getString(R.string.confirmation_tag_delete).replace("$1", tag)

        AlertDialog.Builder(context!!)
            .setTitle(title)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                DataRepository.updateDevice(heatingViewModel.heating.value!!.apply {
                    tags.remove(tag)
                })
            }
            .show()
    }

    private fun showRoomDialog(text: String) {
        InputDialog.show(context!!, R.string.room, text, invalidOptions = setOf(text)) {
            DataRepository.updateDevice(heatingViewModel.heating.value!!.apply {
                room = it
            })
        }
    }

    private fun showTargetTempDialog(text: String) {
        InputDialog.show(
            context!!,
            R.string.temperature,
            text = text,
            inputType = InputType.TYPE_CLASS_PHONE,
            validator = { it.replace(',', '.').toDoubleOrNull() != null }
        ) {
            DataRepository.updateDevice(heatingViewModel.heating.value!!.apply {
                targetTemp.global = it.replace(',', '.').toDouble()
            })
        }
    }
}
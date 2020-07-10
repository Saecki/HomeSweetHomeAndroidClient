package bedbrains.homesweethomeandroidclient.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.RadioGroup
import androidx.core.view.get
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.SortingBinding
import com.google.android.material.radiobutton.MaterialRadioButton

open class SortingDialog(context: Context) : BaseDialog(context) {

    var sortingCriteria: List<String> = listOf()
    var sortingCriterion = 0
    var ascending = true
    var onFinished: (Int, Boolean) -> Unit = { _, _ -> }
    private val binding = SortingBinding.inflate(LayoutInflater.from(context))

    init {
        dialogBuilder
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                onFinished()
            }

        binding.sortingOrderAscending.id = 0
        binding.sortingOrderDescending.id = 1
    }


    override fun onCreate() {
        sortingCriteria.forEachIndexed { index, it ->
            val radioButton = MaterialRadioButton(context)
            binding.sortingCriteria.addView(radioButton)

            radioButton.text = it
            radioButton.id = index
            radioButton.layoutParams.width = RadioGroup.LayoutParams.MATCH_PARENT
        }

        val checkedButton = binding.sortingCriteria.get(sortingCriterion) as MaterialRadioButton
        checkedButton.isChecked = true

        if (ascending) binding.sortingOrderAscending.isChecked = true
        else binding.sortingOrderDescending.isChecked = true

        binding.sortingCriteria.setOnCheckedChangeListener { _, checkedId ->
            sortingCriterion = checkedId
        }
        binding.sortingOrder.setOnCheckedChangeListener { _, checkedId ->
            ascending = checkedId == 0
        }
    }

    protected open fun onFinished() {
        onFinished(sortingCriterion, ascending)
    }
}

fun <T : SortingDialog> T.sortingCriteria(criteria: List<String>, selected: Int): T {
    this.sortingCriteria = criteria
    this.sortingCriterion = selected
    return this
}

fun <T : SortingDialog> T.sortingCriteriaRes(criteria: List<Int>, selected: Int): T {
    this.sortingCriteria = criteria.map { Res.resources.getString(it) }
    this.sortingCriterion = selected
    return this
}

fun <T : SortingDialog> T.ascending(ascending: Boolean): T {
    this.ascending = ascending
    return this
}

fun <T : SortingDialog> T.onFinished(onFinished: (criterion: Int, ascending: Boolean) -> Unit): T {
    this.onFinished = onFinished
    return this
}

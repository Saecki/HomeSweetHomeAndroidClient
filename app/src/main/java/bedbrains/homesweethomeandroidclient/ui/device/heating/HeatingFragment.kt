package bedbrains.homesweethomeandroidclient.ui.device.heating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.databinding.FragmentHeatingBinding

class HeatingFragment : Fragment() {

    private val heatingViewModel: HeatingViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        MainActivity.toolbar.title = heatingViewModel.heating.name

        val binding = FragmentHeatingBinding.inflate(inflater)

        return binding.root
    }

}
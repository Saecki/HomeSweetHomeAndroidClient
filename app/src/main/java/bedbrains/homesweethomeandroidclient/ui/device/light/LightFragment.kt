package bedbrains.homesweethomeandroidclient.ui.device.light

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.databinding.FragmentLightBinding

class LightFragment : Fragment() {

    private val lightViewModel: LightViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        MainActivity.toolbar.title = lightViewModel.light.name

        val binding = FragmentLightBinding.inflate(inflater)

        return binding.root
    }

}
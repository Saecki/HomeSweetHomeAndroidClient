package bedbrains.homesweethomeandroidclient.ui.device.light

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R

class LightFragment : Fragment() {

    lateinit var lightViewModel: LightViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lightViewModel = ViewModelProviders.of(this).get(LightViewModel::class.java)
        MainActivity.appBarLayout.findViewById<Toolbar>(R.id.toolbar).title = lightViewModel.light.name

        val root = inflater.inflate(R.layout.fragment_light, container, false)

        return root
    }

}
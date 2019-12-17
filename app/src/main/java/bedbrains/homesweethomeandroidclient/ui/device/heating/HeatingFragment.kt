package bedbrains.homesweethomeandroidclient.ui.device.heating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R

class HeatingFragment : Fragment() {

    lateinit var heatingViewModel: HeatingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        heatingViewModel = ViewModelProviders.of(this).get(HeatingViewModel::class.java)
        MainActivity.appBarLayout.findViewById<Toolbar>(R.id.toolbar).title = heatingViewModel.heating.name

        val root = inflater.inflate(R.layout.fragment_heating, container, false)

        return root
    }

}
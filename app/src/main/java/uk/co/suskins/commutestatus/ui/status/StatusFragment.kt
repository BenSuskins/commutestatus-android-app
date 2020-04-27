package uk.co.suskins.commutestatus.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import uk.co.suskins.commutestatus.R


class StatusFragment : Fragment() {

    companion object {
        fun newInstance() =
            StatusFragment()
    }

    private lateinit var viewModel: StatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.status_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StatusViewModel::class.java)
        viewModel.getStatuses().observe(viewLifecycleOwner, Observer { statuses ->
            //Todo update view according to view model
//            toWorkPlatform.text = ""
        })
    }

}

package uk.co.suskins.commutestatus.ui.status

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import uk.co.suskins.commutestatus.R

class StatusActivity : AppCompatActivity() {
    private lateinit var viewModel: StatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        initialiseView()
    }

    override fun initialiseView() {
        viewModel = ViewModelProviders.of(this).get(StatusViewModel::class.java)
        viewModel.getStatuses().observe(this, Observer { statuses ->
            //Todo update view according to view model
//            toWorkPlatform.text = ""
        })
    }
}

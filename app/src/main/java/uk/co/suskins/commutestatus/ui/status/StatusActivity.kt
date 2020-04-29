package uk.co.suskins.commutestatus.ui.status

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uk.co.suskins.commutestatus.R
import uk.co.suskins.commutestatus.ui.welcome.EXTRA_ID_TOKEN

class StatusActivity : AppCompatActivity() {
    private lateinit var viewModel: StatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        initialiseView()
    }

    private fun initialiseView() {
        //Obtain the token from the Intent's extras
        val accessToken = intent.getStringExtra(EXTRA_ID_TOKEN)

//        viewModel = ViewModelProviders.of(this).get(StatusViewModel::class.java)
//        viewModel.getStatuses().observe(this, Observer { statuses ->
//            //Todo update view according to view model
//           toWorkPlatform.text = ""
//        })
    }
}

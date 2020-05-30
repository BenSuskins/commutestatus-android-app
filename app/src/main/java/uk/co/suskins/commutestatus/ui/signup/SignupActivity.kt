package uk.co.suskins.commutestatus.ui.signup

import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_settings.emailInput
import kotlinx.android.synthetic.main.activity_settings.emailText
import kotlinx.android.synthetic.main.activity_settings.errorMessage
import kotlinx.android.synthetic.main.activity_settings.homeStationInput
import kotlinx.android.synthetic.main.activity_settings.homeStationText
import kotlinx.android.synthetic.main.activity_settings.workStationInput
import kotlinx.android.synthetic.main.activity_settings.workStationText
import kotlinx.android.synthetic.main.activity_signup.*
import uk.co.suskins.commutestatus.R
import uk.co.suskins.commutestatus.data.UserRequest
import uk.co.suskins.commutestatus.ui.status.ERRORED
import uk.co.suskins.commutestatus.ui.status.LOADING

class SignupActivity : AppCompatActivity() {
    private val viewModel: SignupViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        initialiseUi()
    }

    private fun initialiseUi() {
        viewModel.getStations()

        //Set button listeners
        findViewById<Button>(R.id.resetBtn).setOnClickListener { reset() }
        findViewById<Button>(R.id.createBtn).setOnClickListener { createUser() }

        val userStatusObserver = Observer<String> { status ->
            if (status.equals(ERRORED)) {
                //Show error message
                errorMessage.isVisible = true
            }

        }
        viewModel.userStatus.observe(this, userStatusObserver)

        val stationStatusObserver = Observer<String> { status ->
            if (status.equals(LOADING)) {
                //Do nothing if loading
            } else if (status.equals(ERRORED)) {
                //Show error message
                errorMessage.isVisible = true
                emailInput.isVisible = false
                emailText.isVisible = false
                passwordInput.isVisible = false
                passwordText.isVisible = false
                homeStationInput.isVisible = false
                homeStationText.isVisible = false
                workStationInput.isVisible = false
                workStationText.isVisible = false
            } else {
                //Set AutoCompleteText Views
                val stations: ArrayAdapter<String> =
                    ArrayAdapter<String>(
                        this,
                        R.layout.list_item,
                        R.id.itemText,
                        viewModel.stations.value!!.stations.map { station -> station.name })

                homeStationInput.threshold = 2
                homeStationInput.setAdapter(stations)

                workStationInput.threshold = 2
                workStationInput.setAdapter(stations)
            }
        }
        viewModel.stationStatus.observe(this, stationStatusObserver)
    }

    private fun createUser() {
        //Create UserRequest
        val userRequest = UserRequest(
            emailInput.text.toString(),
            passwordInput.text.toString(),
            getStationId(homeStationInput.text.toString()),
            getStationId(workStationInput.text.toString())
        )

        //PutRequest to Backend
        viewModel.postUser(userRequest)
    }

    /**
     * Finds station ID from Station Name
     */
    private fun getStationId(stationToFind: String): Int {
        return viewModel.stations.value!!.stations.filter { station ->
            station.name.equals(
                stationToFind
            )
        }[0].id
    }

    private fun reset() {
        //Set form to default values
        emailInput.text = Editable.Factory.getInstance().newEditable("")
        passwordInput.text = Editable.Factory.getInstance().newEditable("")
        homeStationInput.text =
            Editable.Factory.getInstance().newEditable("")

        workStationInput.text =
            Editable.Factory.getInstance().newEditable("")

    }
}

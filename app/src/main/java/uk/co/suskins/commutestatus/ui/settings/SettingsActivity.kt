package uk.co.suskins.commutestatus.ui.settings

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_settings.*
import uk.co.suskins.commutestatus.R
import uk.co.suskins.commutestatus.data.UserRequest
import uk.co.suskins.commutestatus.ui.status.ERRORED
import uk.co.suskins.commutestatus.ui.status.LOADING
import uk.co.suskins.commutestatus.ui.welcome.EXTRA_CLEAR_CREDENTIALS
import uk.co.suskins.commutestatus.ui.welcome.EXTRA_ID_TOKEN
import uk.co.suskins.commutestatus.ui.welcome.MainActivity

class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModels()
    var idToken: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        refresh()
        initialiseUi()
    }

    private fun refresh() {
        //Refresh data from API
        idToken = intent.getStringExtra(EXTRA_ID_TOKEN)
        viewModel.getData(idToken)
    }

    private fun initialiseUi() {
        //Set button listeners
        findViewById<Button>(R.id.logoutBtn).setOnClickListener { logout() }
        findViewById<Button>(R.id.resetBtn).setOnClickListener { reset() }
        findViewById<Button>(R.id.createBtn).setOnClickListener { updatePreferences() }

        //Set initial view
        loadingBar.isVisible = true
        errorMessage.isVisible = false
        emailInput.isVisible = false
        emailText.isVisible = false
        homeStationInput.isVisible = false
        homeStationText.isVisible = false
        workStationInput.isVisible = false
        workStationText.isVisible = false

        //Show loading page
        val userStatusObserver = Observer<String> { status ->
            if (status.equals(LOADING)) {
                //Show spinny wheel
                loadingBar.isVisible = true
                errorMessage.isVisible = false
                emailInput.isVisible = false
                emailText.isVisible = false
                homeStationInput.isVisible = false
                homeStationText.isVisible = false
                workStationInput.isVisible = false
                workStationText.isVisible = false
            } else if (status.equals(ERRORED)) {
                //Show error message
                loadingBar.isVisible = false
                errorMessage.isVisible = true
                emailInput.isVisible = false
                emailText.isVisible = false
                homeStationInput.isVisible = false
                homeStationText.isVisible = false
                workStationInput.isVisible = false
                workStationText.isVisible = false
            } else {
                //Dont show  spinny wheel
                loadingBar.isVisible = false
                errorMessage.isVisible = false
                emailInput.isVisible = true
                emailText.isVisible = true
                homeStationInput.isVisible = true
                homeStationText.isVisible = true
                workStationInput.isVisible = true
                workStationText.isVisible = true
                reset()
            }

        }
        viewModel.userStatus.observe(this, userStatusObserver)

        val stationStatusObserver = Observer<String> { status ->
            if (status.equals(LOADING)) {
                //Do nothing if loading
            } else if (status.equals(ERRORED)) {
                //Show error message
                loadingBar.isVisible = false
                errorMessage.isVisible = true
                emailInput.isVisible = false
                emailText.isVisible = false
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

    private fun updatePreferences() {
        //Create UserRequest
        val userRequest = UserRequest(
            emailInput.text.toString(),
            null,
            getStationId(homeStationInput.text.toString()),
            getStationId(workStationInput.text.toString())
        )

        //PutRequest to Backend
        viewModel.putUser(userRequest, idToken)
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
        emailInput.text = Editable.Factory.getInstance().newEditable(viewModel.user.value!!.email)
        homeStationInput.text =
            Editable.Factory.getInstance().newEditable(viewModel.user.value!!.homeStation)

        workStationInput.text =
            Editable.Factory.getInstance().newEditable(viewModel.user.value!!.workStation)

    }

    private fun logout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(EXTRA_CLEAR_CREDENTIALS, true)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
        finish()
    }
}

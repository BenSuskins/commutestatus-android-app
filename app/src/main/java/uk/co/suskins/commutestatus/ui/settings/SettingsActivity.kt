package uk.co.suskins.commutestatus.ui.settings

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import uk.co.suskins.commutestatus.R
import uk.co.suskins.commutestatus.ui.welcome.EXTRA_CLEAR_CREDENTIALS
import uk.co.suskins.commutestatus.ui.welcome.EXTRA_ID_TOKEN
import uk.co.suskins.commutestatus.ui.welcome.MainActivity

class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        refresh()
        initialiseUi()
    }

    private fun refresh() {
        //Refresh data from API
        val idToken = intent.getStringExtra(EXTRA_ID_TOKEN)
        viewModel.getData(idToken)
    }

    private fun initialiseUi() {
        //Set button listeners
        findViewById<Button>(R.id.logoutBtn).setOnClickListener { logout() }
        findViewById<Button>(R.id.resetBtn).setOnClickListener { reset() }
        findViewById<Button>(R.id.updateBtn).setOnClickListener { updatePreferences() }

        //Reset form
//        reset()
    }

    private fun updatePreferences() {
        //Retrieve form values

        //Create UserRequest

        //PutRequest to Backend
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

package uk.co.suskins.commutestatus.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import uk.co.suskins.commutestatus.R
import uk.co.suskins.commutestatus.ui.welcome.EXTRA_CLEAR_CREDENTIALS
import uk.co.suskins.commutestatus.ui.welcome.MainActivity

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initialiseUi()
    }

    private fun initialiseUi() {
        val logoutButton = findViewById<Button>(R.id.logoutBtn)
        logoutButton.setOnClickListener { logout() }
//        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
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

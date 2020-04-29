package uk.co.suskins.commutestatus.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import uk.co.suskins.commutestatus.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initialiseView()
    }
    private fun initialiseView() {
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
    }
}

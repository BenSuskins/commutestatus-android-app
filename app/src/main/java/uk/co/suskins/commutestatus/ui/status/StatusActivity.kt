package uk.co.suskins.commutestatus.ui.status

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_status.*
import uk.co.suskins.commutestatus.R
import uk.co.suskins.commutestatus.data.Status
import uk.co.suskins.commutestatus.ui.welcome.EXTRA_ID_TOKEN

const val ON_TIME = "On time"
const val LOADING = "loading"
const val ERRORED = "errored"

class StatusActivity : AppCompatActivity() {
    private val TAG = "StatusActivity"
    private val viewModel: StatusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        refresh()
        initialiseUi()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.refresh -> {
                refresh()
                true
            }
            R.id.settings -> {
                //todo
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initialiseUi() {
        val UNKNOWN_PLATFORM = getString(R.string.unkownPlatform)
        //Initially set screen to show loading
        loadingBar.isVisible = true
        toHomePlatform.isVisible = false
        toHomeSTD.isVisible = false
        toHomeStatus.isVisible = false
        toWorkPlatform.isVisible = false
        toWorkSTD.isVisible = false
        toWorkStatus.isVisible = false
        errorMessage.isVisible = false

        //Create an observer for is loading
        val statusObserver = Observer<String> { status ->
            if (status.equals(LOADING)) {
                Log.i(TAG, "Loading")
                //Show spinny wheel
                loadingBar.isVisible = true
                toHomePlatform.isVisible = false
                toHomeSTD.isVisible = false
                toHomeStatus.isVisible = false
                toWorkPlatform.isVisible = false
                toWorkSTD.isVisible = false
                toWorkStatus.isVisible = false
                errorMessage.isVisible = false
            } else if (status.equals(ERRORED)) {
                Log.i(TAG, "Errored")
                //Show error message
                loadingBar.isVisible = false
                toHomePlatform.isVisible = false
                toHomeSTD.isVisible = false
                toHomeStatus.isVisible = false
                toWorkPlatform.isVisible = false
                toWorkSTD.isVisible = false
                toWorkStatus.isVisible = false
                errorMessage.isVisible = true
            } else {
                Log.i(TAG, "Finished Loading")
                //Dont show  spinny wheel
                loadingBar.isVisible = false
                toHomePlatform.isVisible = true
                toHomeSTD.isVisible = true
                toHomeStatus.isVisible = true
                toWorkPlatform.isVisible = true
                toWorkSTD.isVisible = true
                toWorkStatus.isVisible = true
                errorMessage.isVisible = false
            }

        }
        viewModel.status.observe(this, statusObserver)

        //Create an observer for the commute status
        val toHomeObserver = Observer<Status> { toHome ->
            if (toHome != null) {
                //Set to home
                toHomeSTD.text = getString(
                    R.string.std,
                    toHome.scheduledTimeOfDeparture,
                    toHome.to
                ) //todo handle no trains
                toHomeStatus.text =
                    getString(R.string.status, toHome.estimatedTimeOfDeparture)
                toHomePlatform.text = getString(
                    R.string.platform, if (toHome.platform != "") {
                        toHome.platform
                    } else {
                        UNKNOWN_PLATFORM
                    }
                )

                setStatusColours()
            }
        }

        val toWorkObserver = Observer<Status> { toWork ->
            if (toWork != null) {
                //Set to work
                toWorkSTD.text = getString(
                    R.string.std,
                    toWork.scheduledTimeOfDeparture,
                    toWork.to
                ) //todo handle no trains
                toWorkStatus.text =
                    getString(R.string.status, toWork.estimatedTimeOfDeparture)
                toWorkPlatform.text = getString(
                    R.string.platform, if (toWork.platform != "") {
                        toWork.platform
                    } else {
                        UNKNOWN_PLATFORM
                    }
                )
                setStatusColours()
            }
        }

        viewModel.getToWorkStatus().observe(this, toWorkObserver)
        viewModel.getToHomeStatus().observe(this, toHomeObserver)
    }

    fun refresh() {
        //Refresh Commute Status from API
        val idToken = intent.getStringExtra(EXTRA_ID_TOKEN)
        viewModel.getCommuteStatus(idToken)
    }

    private fun setStatusColours() {
        if (viewModel.getToWorkStatus().value != null) {
            when {
                viewModel.getToWorkStatus().value?.estimatedTimeOfDeparture.equals(
                    ON_TIME,
                    true
                ) -> {
                    toWorkStatus.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorOnTime
                        )
                    )
                }
                viewModel.getToWorkStatus().value?.isCancelled!! -> {
                    toWorkStatus.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorCancelled
                        )
                    )
                }
                else -> {
                    toWorkStatus.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorDelayed
                        )
                    )
                }
            }
        }

        if (viewModel.getToHomeStatus().value != null) {
            when {
                viewModel.getToHomeStatus().value?.estimatedTimeOfDeparture.equals(
                    ON_TIME,
                    true
                ) -> {
                    toHomeStatus.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorOnTime
                        )
                    )
                }
                viewModel.getToHomeStatus().value?.isCancelled!! -> {
                    toHomeStatus.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorCancelled
                        )
                    )
                }
                else -> {
                    toHomeStatus.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorDelayed
                        )
                    )
                }
            }
        }
    }

    fun activityClickAction(view: View) {
        //If screen clicked, update to next train
        viewModel.incrementIndex()
    }
}

package uk.co.suskins.commutestatus.ui.status

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_status.*
import uk.co.suskins.commutestatus.R
import uk.co.suskins.commutestatus.data.CommuteStatus
import uk.co.suskins.commutestatus.data.Status
import uk.co.suskins.commutestatus.ui.welcome.EXTRA_ID_TOKEN

const val ON_TIME = "On time"
const val LOADING = "loading"
const val ERRORED = "errored"

class StatusActivity : AppCompatActivity() {
    private val viewModel: StatusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        initialiseUi()
    }

    private fun initialiseUi() {
        //Obtain the token from the Intent's extras
        val idToken = intent.getStringExtra(EXTRA_ID_TOKEN)
        val UNKNOWN_PLATFORM = getString(R.string.unkownPlatform)
        //Initially set screen to show loading
        loadingBar.isVisible = true
        toHomePlatform.isVisible = false
        toHomeSTD.isVisible = false
        toHomeStatus.isVisible = false
        toWorkPlatform.isVisible = false
        toWorkSTD.isVisible = false
        toWorkStatus.isVisible = false

        //Create an observer for is loading
        val statusObserver = Observer<String> { status ->
            if (status.equals(LOADING)) {
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
                //Show spinny wheel
                loadingBar.isVisible = false
                toHomePlatform.isVisible = false
                toHomeSTD.isVisible = false
                toHomeStatus.isVisible = false
                toWorkPlatform.isVisible = false
                toWorkSTD.isVisible = false
                toWorkStatus.isVisible = false
                errorMessage.isVisible = true
            } else {
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
        val commuteStatusObserver = Observer<CommuteStatus> { statuses ->
            //Set to work
            val toWork = statuses.toWork.elementAt(viewModel.workIndex)
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

            //Set to home
            val toHome = statuses.toHome.elementAt(viewModel.homeIndex)
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

            setStatusColours(toWork, toHome)
        }
        viewModel.getCommuteStatus(idToken).observe(this, commuteStatusObserver)

    }

    private fun setStatusColours(toWork: Status, toHome: Status) {
        when {
            toWork.estimatedTimeOfDeparture.equals(ON_TIME, true) -> {
                toWorkStatus.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorOnTime
                    )
                )
            }
            toWork.isCancelled -> {
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

        when {
            toHome.estimatedTimeOfDeparture.equals(ON_TIME, true) -> {
                toHomeStatus.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorOnTime
                    )
                )
            }
            toHome.isCancelled -> {
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

    fun activityClickAction(view: View) {
        viewModel.incrementIndex()
    }
}

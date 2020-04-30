package uk.co.suskins.commutestatus.ui.status

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_status.*
import uk.co.suskins.commutestatus.R
import uk.co.suskins.commutestatus.data.CommuteStatus
import uk.co.suskins.commutestatus.data.Status
import uk.co.suskins.commutestatus.ui.welcome.EXTRA_ID_TOKEN

class StatusActivity : AppCompatActivity() {
    private val viewModel: StatusViewModel by viewModels()
    private var workIndex = 0
    private var homeIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        initialiseUi()
    }

    private fun initialiseUi() {
        //Obtain the token from the Intent's extras
        val idToken = intent.getStringExtra(EXTRA_ID_TOKEN)


        //Create an observer for is loading
        val loadingObserver = Observer<Boolean> { isLoading ->
            if (isLoading) {
                //Show spinny wheel
                loadingBar.isVisible = true
                toHomePlatform.isVisible = false
                toHomeSTD.isVisible = false
                toHomeStatus.isVisible = false
                toWorkPlatform.isVisible = false
                toWorkSTD.isVisible = false
                toWorkStatus.isVisible = false
            } else {
                //Dont show  spinny wheel
                loadingBar.isVisible = false
                toHomePlatform.isVisible = true
                toHomeSTD.isVisible = true
                toHomeStatus.isVisible = true
                toWorkPlatform.isVisible = true
                toWorkSTD.isVisible = true
                toWorkStatus.isVisible = true
            }

        }
        viewModel.isLoading().observe(this, loadingObserver)

        //Create an observer for the commute status
        val commuteStatusObserver = Observer<CommuteStatus> { statuses ->
            //Set to work
            val toWork = statuses.toWork.elementAt(workIndex)
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
                    "Unknown"
                }
            )

            //Set to home
            val toHome = statuses.toHome.elementAt(homeIndex)
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
                    "Unknown"
                }
            )

            setStatusColours(toWork, toHome)
        }
        viewModel.getCommuteStatus(idToken).observe(this, commuteStatusObserver)

    }

    @SuppressLint("ResourceAsColor")
    private fun setStatusColours(toWork: Status, toHome: Status) {
        when {
            toWork.estimatedTimeOfDeparture.equals("on time", true) -> {
                toWorkStatus.setTextColor(R.color.colorOnTime)
            }
            toWork.isCancelled -> {
                toWorkStatus.setTextColor(R.color.colorCancelled)
            }
            else -> {
                toWorkStatus.setTextColor(R.color.colorDelayed)
            }
        }

        when {
            toHome.estimatedTimeOfDeparture.equals("on time", true) -> {
                toHomeStatus.setTextColor(R.color.colorOnTime)
            }
            toHome.isCancelled -> {
                toHomeStatus.setTextColor(R.color.colorCancelled)
            }
            else -> {
                toHomeStatus.setTextColor(R.color.colorDelayed)
            }
        }
    }

    fun activityClickAction(view: View) {
        homeIndex++
        if (homeIndex >= viewModel.getNumberOfHomeStatuses()!!) {
            homeIndex = 0
        }

        workIndex++
        if (workIndex >= viewModel.getNumberOfWorkStatuses()!!) {
            workIndex = 0
        }
    }
}

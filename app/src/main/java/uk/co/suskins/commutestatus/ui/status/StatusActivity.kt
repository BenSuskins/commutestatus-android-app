package uk.co.suskins.commutestatus.ui.status

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_status.*
import uk.co.suskins.commutestatus.R
import uk.co.suskins.commutestatus.models.Status
import uk.co.suskins.commutestatus.ui.welcome.EXTRA_ID_TOKEN

class StatusActivity : AppCompatActivity() {
    private lateinit var viewModel: StatusViewModel
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

        viewModel = ViewModelProviders.of(this).get(StatusViewModel::class.java)
        viewModel.getStatuses().observe(this, Observer { statuses ->

            //Set to work
            val toWork = statuses[0].toWork.elementAt(workIndex)
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
            val toHome = statuses[0].toHome.elementAt(homeIndex)
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
        })
    }

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

    fun activityClickAction() {
        homeIndex++
        if (homeIndex >= viewModel.getNumberOfHomeStatuses()) {
            homeIndex = 0
        }

        workIndex++
        if (workIndex >= viewModel.getNumberOfWorkStatuses()) {
            workIndex = 0
        }
    }
}

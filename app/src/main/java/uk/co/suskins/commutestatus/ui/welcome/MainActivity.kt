package uk.co.suskins.commutestatus.ui.welcome

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.Auth0Exception
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.VoidCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import uk.co.suskins.commutestatus.R
import uk.co.suskins.commutestatus.ui.status.StatusActivity

const val EXTRA_ID_TOKEN: String = "com.auth0.ID_TOKEN"
const val EXTRA_CLEAR_CREDENTIALS: String = "com.auth0.CLEAR_CREDENTIALS"

public class MainActivity : AppCompatActivity() {
    private lateinit var auth0: Auth0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init Auth0
        auth0 = Auth0(this)
        auth0.isOIDCConformant = true

        //Check if the activity was launched to log the user out
        if (intent.getBooleanExtra(EXTRA_CLEAR_CREDENTIALS, false)) {
            logout()
        }

        initaliseUi()
    }

    private fun initaliseUi() {
        val loginButton: Button = findViewById(R.id.loginBtn)
        loginButton.setOnClickListener { login() }
    }

    private fun login() {
        WebAuthProvider.login(auth0)
            .withScheme("demo")
            .start(this, object : AuthCallback {
                override fun onFailure(@NonNull dialog: Dialog) {
                    runOnUiThread { dialog.show() }
                }

                override fun onFailure(exception: AuthenticationException) {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "Error: " + exception.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onSuccess(@NonNull credentials: Credentials) {
                    runOnUiThread {
                        val intent = Intent(applicationContext, StatusActivity::class.java)
                        intent.putExtra(EXTRA_ID_TOKEN, credentials.idToken)

                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        }

                        finish()
                    }
                }
            })
    }

    private fun logout() {
        WebAuthProvider.logout(auth0)
            .withScheme("demo")
            .start(this, object : VoidCallback {
                override fun onSuccess(payload: Void) {}
                override fun onFailure(error: Auth0Exception) {
                    //Log out canceled, keep the user logged in
                    showNextActivity()
                }
            })
    }

    private fun showNextActivity() {
        val intent = Intent(this, StatusActivity::class.java)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
        finish()
    }
}

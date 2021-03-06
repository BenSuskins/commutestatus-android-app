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
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.BaseCallback
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.VoidCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import uk.co.suskins.commutestatus.R
import uk.co.suskins.commutestatus.ui.signup.SignupActivity
import uk.co.suskins.commutestatus.ui.status.StatusActivity

const val EXTRA_ID_TOKEN: String = "com.auth0.ID_TOKEN"
const val EXTRA_CLEAR_CREDENTIALS: String = "com.auth0.CLEAR_CREDENTIALS"

class MainActivity : AppCompatActivity() {
    private lateinit var auth0: Auth0
    private lateinit var credentialsManager: SecureCredentialsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup CredentialsManager
        auth0 = Auth0(this)
        auth0.isOIDCConformant = true
        credentialsManager = SecureCredentialsManager(
            this,
            AuthenticationAPIClient(auth0),
            SharedPreferencesStorage(this)
        )

        //Check if the activity was launched to log the user out
        if (intent.getBooleanExtra(EXTRA_CLEAR_CREDENTIALS, false)) {
            doLogout()
        } else if (credentialsManager.hasValidCredentials()) {
            // Obtain the existing credentials and move to the next activity
            showStatusActivity()
        }

        initialiseUi()
    }

    private fun initialiseUi() {
        val loginButton: Button = findViewById(R.id.loginBtn)
        loginButton.setOnClickListener { login() }
        val signupButton: Button = findViewById(R.id.signUpBtn)
        signupButton.setOnClickListener { signup() }
    }

    private fun signup() {
        val intent = Intent(applicationContext, SignupActivity::class.java)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun login() {
        WebAuthProvider.login(auth0)
            .withScheme("demo")
            .withScope("openid offline_access")
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
                    credentialsManager.saveCredentials(credentials)
                    showStatusActivity()
                }
            })
    }

    private fun doLogout() {
        WebAuthProvider.logout(auth0)
            .withScheme("demo")
            .start(this, logoutCallback)
    }

    private val logoutCallback: VoidCallback = object : VoidCallback {
        override fun onSuccess(payload: Void?) {
            credentialsManager.clearCredentials()
        }

        override fun onFailure(error: Auth0Exception?) {
            // Log out canceled, keep the user logged in
            showStatusActivity()
        }
    }

    private fun showStatusActivity() {
        credentialsManager.getCredentials(object :
            BaseCallback<Credentials, CredentialsManagerException?> {
            override fun onSuccess(credentials: Credentials) {
                // Move to the next activity
                val intent = Intent(applicationContext, StatusActivity::class.java)
                intent.putExtra(EXTRA_ID_TOKEN, credentials.idToken)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                finish()
            }

            override fun onFailure(error: CredentialsManagerException?) {
                // Credentials could not be retrieved.
                finish()
            }
        })
    }
}

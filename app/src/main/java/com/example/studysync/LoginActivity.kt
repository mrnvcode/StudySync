package com.example.studysync

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Facebook SDK
        com.facebook.FacebookSdk.sdkInitialize(applicationContext)

        // Initialize Facebook Login button and CallbackManager
        callbackManager = CallbackManager.Factory.create()
        val buttonFacebookLogin: LoginButton = findViewById(R.id.buttonFacebookLogin)
        buttonFacebookLogin.setReadPermissions("email", "public_profile")
        buttonFacebookLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("FacebookLogin", "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("FacebookLogin", "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d("FacebookLogin", "facebook:onError", error)
            }
        })

        // Example usage of loginUser function
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val registerTextView: TextView = findViewById(R.id.registerTextView)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginUser(email, password)
        }

        // Link to RegistrationActivity for new users
        registerTextView.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleFacebookAccessToken(token: com.facebook.AccessToken) {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser
                    navigateToMainActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FacebookLogin", "signInWithCredential:failure", task.exception)
                    // TODO: Handle the login failure, display an error message, etc.
                    showErrorDialog("Facebook login failed.")
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser
                    navigateToMainActivity()
                } else {
                    // Login failed
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                    showErrorDialog("Login failed. Please check your credentials and try again.")
                }
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the LoginActivity so that the user cannot go back to it after logging in
    }

    private fun showErrorDialog(errorMessage: String) {
        // TODO: Implement a dialog or display an error message to the user
    }

    // Override onActivityResult to pass the result back to the Facebook SDK
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}

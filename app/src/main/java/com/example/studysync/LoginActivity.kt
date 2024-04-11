package com.example.studysync

import android.app.AlertDialog
import android.content.Context
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
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    // Firebase Auth instance
    private lateinit var auth: FirebaseAuth
    // Google Sign In Client
    private lateinit var googleSignInClient: GoogleSignInClient
    // Request code for Google Sign In
    private val RC_SIGN_IN = 9001
    // CallbackManager for Facebook Login
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Google Sign In Client
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Facebook CallbackManager
        callbackManager = CallbackManager.Factory.create()

        // Initialize views
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val registerTextView: TextView = findViewById(R.id.registerTextView)
        val googleLoginButton: Button = findViewById(R.id.buttonGoogleLogin)
        val facebookLoginButton: Button = findViewById(R.id.buttonFacebookLogin)
        val helpLinkTextView: TextView = findViewById(R.id.helpLinkTextView)

        // Login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginUser(email, password)
        }

        // Register text view click listener
        registerTextView.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        // Help Link click listener
        helpLinkTextView.setOnClickListener {
            val intent = Intent(this, LoginSupportActivity::class.java)
            startActivity(intent)
        }

        // Google login button click listener
        googleLoginButton.setOnClickListener {
            signInWithGoogle()
        }

        // Facebook login button click listener
        facebookLoginButton.setOnClickListener {
            signInWithFacebook()
        }
    }

    // Function to log in user with email and password
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser
                    navigateToDashboard()
                } else {
                    // Login failed
                    Log.w("Login", "signInWithEmailAndPassword:failure", task.exception)
                    showErrorDialog(this, "Login failed. Please check your credentials and try again.")
                }
            }
    }

    // Function to initiate Google Sign In
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Function to initiate Facebook Sign In
    private fun signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                // Handle cancel
            }

            override fun onError(error: FacebookException) {
                // Handle error
                showErrorDialog(this@LoginActivity, "Facebook sign in failed.")
            }
        })
    }

    // Function to handle Facebook access token and sign in
    private fun handleFacebookAccessToken(token: com.facebook.AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    navigateToProfileInformationActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    showErrorDialog(this,"Facebook sign in failed.")
                }
            }
    }

    // Function to handle Google Sign In result
    private fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                firebaseAuthWithGoogle(account.idToken!!)
            }
        } catch (e: ApiException) {
            showErrorDialog(this, "Google sign in failed.")
        }
    }

    // Function to authenticate with Firebase using Google credentials
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    navigateToProfileInformationActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    showErrorDialog(this,"Google sign in failed.")
                }
            }
    }

    // Function to navigate to Dashboard Activity
    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish() // Close the LoginActivity so that the user cannot go back to it after logging in
    }

    // Function to navigate to Profile Information Activity
    private fun navigateToProfileInformationActivity() {
        val intent = Intent(this, ProfileInformationActivity::class.java)
        startActivity(intent)
        finish() // Close the LoginActivity so that the user cannot go back to it after logging in
    }

    // Function to display error dialog
    private fun showErrorDialog(context: Context, errorMessage: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage(errorMessage)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            handleGoogleSignInResult(data)
        }

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}

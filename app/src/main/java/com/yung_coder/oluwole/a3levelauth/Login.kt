package com.yung_coder.oluwole.a3levelauth


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 */
class Login : Fragment() {

    var button_signUp: Button? = null
    var text_email: EditText? = null
    var text_password: EditText? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.fragment_login, container, false)

        button_signUp = rootView.findViewById<Button>(R.id.email_sign_in_button)
        text_email = rootView.findViewById(R.id.email)
        text_password = rootView.findViewById(R.id.password)

        login_form = rootView.findViewById(R.id.login_form)
        login_progress = rootView.findViewById(R.id.login_progress)

        button_signUp?.setOnClickListener {
            signIn()
        }

        return rootView
    }

    var login_form: ScrollView? = null
    var login_progress: ProgressBar? = null

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form?.visibility = if (show) View.GONE else View.VISIBLE
            login_form?.animate()
                    ?.setDuration(shortAnimTime)
                    ?.alpha((if (show) 0 else 1).toFloat())
                    ?.setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_form?.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            login_progress?.visibility = if (show) View.VISIBLE else View.GONE
            login_progress?.animate()
                    ?.setDuration(shortAnimTime)
                    ?.alpha((if (show) 1 else 0).toFloat())
                    ?.setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_progress?.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress?.visibility = if (show) View.VISIBLE else View.GONE
            login_form?.visibility = if (show) View.GONE else View.VISIBLE
        }
    }


    private fun isEmailValid(email: String): Boolean {
        var pattern = Pattern.compile("^[a-zA-Z0-9_]+@[a-zA-Z.]+$")
        var matcher = pattern.matcher(email)
        return matcher.find()
    }

    fun signIn() {
        text_email?.error = null
        text_password?.error = null

        var cancel = false
        var focusView: View? = null

        var email_str = text_email?.text.toString()
        var passwordStr = text_password?.text.toString()

        if (TextUtils.isEmpty(passwordStr)) {
            text_password?.error = "Enter your password"
            focusView = text_password
            cancel = true
        }

        if (TextUtils.isEmpty(email_str)) {
            text_email?.error = "This Field is required"
            focusView = text_email
            cancel = true
        } else if (!isEmailValid(email_str)) {
            text_email?.error = "Invalid Email Address"
            focusView = text_email
            cancel = true

        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
            mAuth.signInWithEmailAndPassword(email_str, passwordStr).addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    showProgress(false)
                    val fragment = PhoneAuth()
                    val ft = fragmentManager.beginTransaction()
                    ft.replace(R.id.fragment_container, fragment)
                    ft.commit()
                } else {
                    showProgress(false)
                    Toast.makeText(activity, "Unable to Login. Please check your Login Credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}// Required empty public constructor

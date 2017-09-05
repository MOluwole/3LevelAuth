package com.yung_coder.oluwole.a3levelauth


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.concurrent.TimeUnit
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.fragment_phone_auth.*


/**
 * A simple [Fragment] subclass.
 */
class PhoneAuth : Fragment() {

    companion object {
        val mAuth = FirebaseAuth.getInstance()
        val current_user = mAuth.currentUser
//        var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    }

    fun Authenticate(credential: PhoneAuthCredential){
        mAuth.signInWithCredential(credential).addOnCompleteListener{task: Task<AuthResult> ->
            if(task.isSuccessful){
                val fragment = QuestionAuth()
                val ft = fragmentManager.beginTransaction()
                ft.replace(R.id.fragment_container, fragment)
                ft.commit()
            }
            else{
                if (task.exception == FirebaseAuthInvalidCredentialsException(credential.toString(), "")) {
                    Toast.makeText(activity, "Invalid OTP code.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    var button_verify: Button? = null
    var text_number: EditText? = null
    var verifyId: String? = null
    var verify_view: LinearLayout? = null

    var phone_progress: ProgressBar? = null
    var phone_panel: LinearLayout? = null

    var button_otp: Button? = null


    fun showProgress(show: Boolean){
        if(show){
            phone_panel?.visibility = View.GONE
            phone_progress?.visibility = View.VISIBLE
        }
        else{
            phone_panel?.visibility = View.VISIBLE
            phone_progress?.visibility = View.GONE
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       val rootView = inflater!!.inflate(R.layout.fragment_phone_auth, container, false)
        if(current_user != null){
            mAuth.signOut()
        }

        text_number = rootView.findViewById(R.id.text_phone_number)
        button_verify = rootView.findViewById(R.id.button_phone_number)
        verify_view = rootView.findViewById(R.id.verify_panel)
        button_otp = rootView.findViewById(R.id.button_verify_number)

        phone_progress = rootView.findViewById(R.id.phone_progress)
        phone_panel = rootView.findViewById(R.id.phone_panel)


        val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                showProgress(false)
                Toast.makeText(activity, "Verification Successful", Toast.LENGTH_SHORT).show()
                text_number?.setText(credential.smsCode)
                verify_view?.visibility  = View.VISIBLE
                Authenticate(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)
                showProgress(false)
                verify_view?.visibility = View.GONE

                if (e is FirebaseAuthInvalidCredentialsException) {
                    text_number?.error = "Invalid phone number. Provide a valid phone Number"
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(activity, "Quota exceeded.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCodeSent(verificationId: String?,
                                    token: PhoneAuthProvider.ForceResendingToken?) {
                Log.d(TAG, "onCodeSent:" + verificationId!!)
                verifyId = verificationId
                Toast.makeText(activity, "OTP sent to your Mobile Phone", Toast.LENGTH_SHORT).show()

                verify_view?.visibility  = View.VISIBLE
            }

        }

        val num_pref = "phone_number"
        val context = activity
        val shared_pref = PreferenceManager.getDefaultSharedPreferences(getContext())
        val num = shared_pref.getString(num_pref, "+2349084678574")

        Log.e("Number", num)

        button_verify?.setOnClickListener {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    num,
                    120,
                    TimeUnit.SECONDS,
                    activity,
                    mCallbacks
            )
        }

        button_otp?.setOnClickListener {
            val otp = text_number?.text
            if(TextUtils.isEmpty(otp)){
                Toast.makeText(activity, "Provide the OTP code sent to you", Toast.LENGTH_SHORT).show()
            }
            else{
                val credential = verifyId?.let { it1 -> PhoneAuthProvider.getCredential(it1, otp.toString()) }
                if (credential != null) {
                    Authenticate(credential)
                }
                else{
                    Toast.makeText(activity, "Request a new OTP code", Toast.LENGTH_SHORT).show()
                    verify_view?.visibility = View.GONE
                }
            }
        }

        return rootView
    }

}// Required empty public constructor

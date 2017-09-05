package com.yung_coder.oluwole.a3levelauth


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


/**
 * A simple [Fragment] subclass.
 */
class QuestionAuth : Fragment() {

    companion object {
        val mAuth = FirebaseAuth.getInstance()
        val current_user = mAuth.currentUser
    }

    var button_answer: Button? = null
    var text_answer: EditText? = null
    var label_question: TextView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.fragment_question_auth, container, false)

        button_answer = rootView.findViewById(R.id.button_answer)
        text_answer = rootView.findViewById(R.id.text_answer)
        label_question = rootView.findViewById(R.id.text_question)

        val context = activity
        val question_key = "security_question"
        val answer_key = "security_answer"
        val shared_pref = PreferenceManager.getDefaultSharedPreferences(getContext())
        val question = shared_pref.getString(question_key, "What is your name?")
        label_question?.text = question

        val answer = shared_pref.getString(answer_key, "Yetunde")

        button_answer?.setOnClickListener {
            val my_answer = text_answer?.text.toString()
            if(answer.toUpperCase() == my_answer.toUpperCase()){
                Toast.makeText(context, "Verification successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(context, Browser::class.java))
            }
            else{
                Toast.makeText(context, "Wrong Answer. Provide the correct answer to continue", Toast.LENGTH_SHORT).show()
            }
        }
        return rootView
    }

}// Required empty public constructor

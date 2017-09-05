package com.yung_coder.oluwole.a3levelauth

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val login_fragment = Login()
        val fragment_manager = supportFragmentManager
        if(home_container == null){
            fragment_manager.beginTransaction().add(R.id.fragment_container, login_fragment).commit()
        }
        else{
            fragment_manager.beginTransaction().replace(R.id.fragment_container, login_fragment).commit()
        }
    }

    fun switchContent(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment, fragment.toString())
        ft.addToBackStack(null)
        ft.commit()
    }
}

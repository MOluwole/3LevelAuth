package com.yung_coder.oluwole.a3levelauth

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.preference.RingtonePreference
import android.text.TextUtils
import android.view.MenuItem
import android.support.v4.app.NavUtils
import android.content.ContentValues.TAG
import android.preference.Preference.OnPreferenceChangeListener



/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 * See [Android Design: Settings](http://developer.android.com/design/patterns/settings.html)
 * for design guidelines and the [Settings API Guide](http://developer.android.com/guide/topics/ui/settings.html)
 * for more information on developing a Settings UI.
 */
class SettingsActivity : AppCompatPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()

        val pref_num = findPreference("phone_number")
        pref_num.onPreferenceChangeListener = OnPreferenceChangeListener { preference, newValue ->
            val num = newValue.toString()
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.applicationContext)
            val editor = sharedPreferences.edit()
            editor.putString("phone_number", num)
            editor.commit()
//            Log.e(TAG, "" + locstr)
            true
        }

        val pref_question = findPreference("security_question")
        pref_question.onPreferenceChangeListener = OnPreferenceChangeListener{ preference, newValue ->
            val question = newValue.toString()
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.applicationContext)
            val editor = sharedPreferences.edit()
            editor.putString("security_question", question)
            editor.commit()
            true
        }

        val pref_answer = findPreference("security_answer")
        pref_answer.onPreferenceChangeListener = OnPreferenceChangeListener{ preference, newValue ->
            val answer = newValue.toString()
            val sharedPrefences = PreferenceManager.getDefaultSharedPreferences(this.applicationContext)
            val editor = sharedPrefences.edit()
            editor.putString("security_answer", answer)
            editor.commit()
            true
        }
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addPreferencesFromResource(R.xml.pref_settings)
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this)
            }
            return true
        }
        return super.onMenuItemSelected(featureId, item)
    }

}

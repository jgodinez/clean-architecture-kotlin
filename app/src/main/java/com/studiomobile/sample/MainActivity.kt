package com.studiomobile.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.activity_main.*


private const val MINIMUM_CACHE_EXPIRATION = 0L

class MainActivity : AppCompatActivity() {

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(MINIMUM_CACHE_EXPIRATION)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        crashButton.setOnClickListener {
            throw RuntimeException(getString(R.string.app_name))
        }

        remoteConfigButton.setOnClickListener {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val entries = remoteConfig.all.entries
                            .map {
                                it.value.asString()
                            }

                        textViewRemoteEntries.text = entries.joinToString()
                    } else {
                        Log.d("Firebase", "Fetch failed: ${task.exception}")
                    }
                }
        }
    }
}

package com.example.android_2_sem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android_2_sem.fragments.FindCitiesFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, FindCitiesFragment())
            .commit()
    }
}
package com.inspirecoding.lampapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_setState.setOnClickListener {
            if (lampView.state == LampView.turnedOff)
            {
                lampView.state = LampView.turnedOn
                btn_setState.text = getString(R.string.turn_off)
            }
            else
            {
                lampView.state = LampView.turnedOff
                btn_setState.text = getString(R.string.turn_on)
            }
        }
    }
}

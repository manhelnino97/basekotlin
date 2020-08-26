package com.omi.pharmacy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.omi.pharmacy.view.Authenticate.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.extras != null && (intent.getStringExtra("data") != null || intent.getStringExtra("id") != null)) {
            val intentH = Intent(this, LoginActivity::class.java)
            var data = intent.getStringExtra("data")
            if (intent.getStringExtra("data") == null) {
                data = "{\n" +
                        "     \"id\": \"${intent.getStringExtra("id")}\"\n" +
                        " }"
            }
            intentH.putExtra("data", data)
            startActivity(intentH)
            finish()
            return
        }
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK xor Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }
}

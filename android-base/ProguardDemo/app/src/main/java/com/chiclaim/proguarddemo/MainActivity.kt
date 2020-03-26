package com.chiclaim.proguarddemo

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val imageResId: Int = resources.getIdentifier("ic_get_by_identifier", "drawable", "com.chiclaim.proguarddemo")
        val image = findViewById<ImageView>(R.id.image_view)
        image.setImageResource(imageResId)
    }
}

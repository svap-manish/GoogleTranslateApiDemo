package com.googletranslateapidemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        translateBtn.setOnClickListener {
            translate(etInput.text.toString())
        }
    }

    private fun translate(inputText: String) {
        var translatedText: String?
        var translate: Translate? = null
        try {
            //Download your credentials.json file and paste it on res/raw folder
            resources.openRawResource(R.raw.credentials).use { inputStream ->
                //Get credentials:
                val myCredentials = GoogleCredentials.fromStream(inputStream)
                //Set credentials and get translate service:
                val translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build()
                translate = translateOptions.service
            }
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val translation = translate?.translate(inputText, Translate.TranslateOption.targetLanguage("en"))
                translatedText = translation?.translatedText
            }
            tv_outPut.text = translatedText
            Toast.makeText(this@MainActivity, translatedText, Toast.LENGTH_LONG).show()
        }
    }
}
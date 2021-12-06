package com.example.day4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val languageTextView = findViewById<TextInputLayout>(R.id.langTextView)
        val yearTextView = findViewById<TextInputLayout>(R.id.yearTextView)
        val button = findViewById<Button>(R.id.button)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        val countTextView = findViewById<TextView>(R.id.countTextView)

        val myApplication = application as MyApplication
        val httpApiService = myApplication.httpApiService



        button.setOnClickListener{
            var year = yearTextView.editText?.text?.toString()
            var lang = languageTextView.editText?.text?.toString()

            yearTextView.editText?.text?.clear()
            languageTextView.editText?.text?.clear()

            var maskedYear = if (year.isNullOrEmpty()) 0
                else Integer.valueOf(year)

            var maskedLang = if (lang.isNullOrEmpty()) ""
                else lang[0].uppercaseChar() + lang.substring(1)
                .lowercase(Locale.getDefault())

            CoroutineScope(Dispatchers.IO).launch {
                val decodedJsonResult = httpApiService.getBooks()
                val books = decodedJsonResult.toList()

                val filteredBooks = books.filter { it.year >= maskedYear && it.language == maskedLang }

                val booksAsString = StringBuilder("")

                for (item in filteredBooks.take(3))
                    booksAsString.append("Result: "+item.title + "\n")

                withContext(Dispatchers.Main){
                    resultTextView.text = booksAsString
                    countTextView.text = "Results: "+filteredBooks.size.toString()
                }

            }
        }
    }
}
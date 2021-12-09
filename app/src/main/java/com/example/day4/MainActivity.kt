package com.example.day4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.day4.data.AppDatabase
import com.example.day4.data.Author
import com.example.day4.data.AuthorWithBooks
import com.example.day4.data.Book
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

        val dao = AppDatabase.getInstance(this).authorDao()

        CoroutineScope(Dispatchers.IO).launch {
            val decodedJsonResult = httpApiService.getBooks()
            val books = decodedJsonResult.toList()

            var x: Int = 1
            val authorsList = books
                .distinctBy { it.author }
                .map {Author(x++, it.author)  }

            dao.insertAllAuthors(*authorsList.toTypedArray())

            var booksList  = mutableListOf<Book>()

            x=1
            for (item in books) {
                booksList.add(
                    Book(
                        x++,
                        authorsList.indexOfFirst { it.name == item.author }+1,
                        item.country,
                        item.imageLink,
                        item.language,
                        item.link,
                        item.pages,
                        item.title,
                        item.year
                    )
                )
            }

            dao.insertAllBooks(*booksList.toTypedArray())

            withContext(Dispatchers.Main){
                resultTextView.text = null
                countTextView.text = null
            }
        }

        button.setOnClickListener{
            var year = yearTextView.editText?.text?.toString()
            var author = languageTextView.editText?.text?.toString()

            yearTextView.editText?.text?.clear()
            languageTextView.editText?.text?.clear()

            var maskedYear = if (year.isNullOrEmpty()) 2222
                else Integer.valueOf(year)

            var maskedAuthor = if (author.isNullOrEmpty()) ""
                else author


            CoroutineScope(Dispatchers.IO).launch {

                val booksAsString = StringBuilder("")
                val result : List<AuthorWithBooks> =  dao.getAuthorWithBooks(maskedAuthor)

                var filteredBooks =
                    if (result.isNullOrEmpty() || result[0].books.isNullOrEmpty())
                        listOf<Book>()
                    else result[0].books

                if (filteredBooks.isNullOrEmpty())
                    booksAsString.append("No books")
                else {
                    if (maskedYear != 2222)
                        filteredBooks = result[0].books.filter { it.year >= maskedYear }

                    var x: Int = 1
                    for (item in filteredBooks)
                       booksAsString.append((x++).toString() + ") "+ item.title + " #" + item.id + " " + item.year +"\n")
                }

                withContext(Dispatchers.Main){
                    resultTextView.text = booksAsString
                    countTextView.text = filteredBooks?.size.toString() + " book(s) by " + author
                }

            }
        }
    }
}
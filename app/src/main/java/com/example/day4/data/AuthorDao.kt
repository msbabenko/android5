package com.example.day4.data

import androidx.room.*

@Dao
interface AuthorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAuthors(vararg authors: Author)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBooks(vararg books: Book)

    @Query("SELECT * FROM books WHERE year >= :year")
    suspend fun getBooksByYear(year: Int): List<Book>

    @Transaction
    @Query("SELECT * FROM authors WHERE name = :author")
    suspend fun getAuthorWithBooks(author: String): List<AuthorWithBooks>

}
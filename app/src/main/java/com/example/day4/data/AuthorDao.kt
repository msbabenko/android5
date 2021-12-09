package com.example.day4.data

import androidx.room.*

@Dao
interface AuthorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAuthors(vararg authors: Author)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBooks(vararg books: Book)


    @Query("SELECT * FROM authors")
    fun getAuthors(): List<Author>

    @Query("SELECT * FROM books")
    fun getBooks(): List<Book>

    @Transaction
    @Query("SELECT * FROM authors WHERE name = :author")
    fun getAuthorWithBooks(author: String): List<AuthorWithBooks>
}
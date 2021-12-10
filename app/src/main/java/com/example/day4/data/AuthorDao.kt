package com.example.day4.data

import androidx.room.*

@Dao
interface AuthorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAuthors(vararg authors: Author)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBooks(vararg books: Book)


    @Query("SELECT * FROM authors")
    suspend fun getAuthors(): List<Author>

    @Query("SELECT * FROM books")
    suspend fun getBooks(): List<Book>

    @Transaction
    @Query("SELECT * FROM authors WHERE name = :author")
    suspend fun getAuthorWithBooks(author: String): List<AuthorWithBooks>
}
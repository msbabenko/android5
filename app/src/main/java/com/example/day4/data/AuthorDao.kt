package com.example.day4.data

import androidx.room.*

@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthor(vararg authors: Author)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Transaction
    @Query("SELECT * FROM authors WHERE id = :id")
    suspend fun getAuthorWithBooks(id: Int): List<AuthorWithBooks>
}
package com.example.reviewapp

import androidx.core.view.WindowInsetsCompat
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Insert
    fun insert(review: Review): Long

    @Update
    fun update(review: Review)

    @Delete
    fun delete(review: Review)

    @Query("SELECT * FROM reviews")
    fun getAll(): List<Review>

}
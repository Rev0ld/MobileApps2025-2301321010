package com.example.reviewapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.reviewapp.data.entities.Review

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

    @Query("SELECT * from reviews WHERE id = :id LIMIT 1")
    fun getById(id: Int): Review?
}
package com.example.reviewapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val rating: String,
    val review: String,
    val photoPath: String?,
    val latitude: Double?,
    var longitude: Double?
)
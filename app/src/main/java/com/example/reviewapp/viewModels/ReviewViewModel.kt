package com.example.reviewapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reviewapp.data.database.AppDatabase
import com.example.reviewapp.data.entities.Review
import kotlinx.coroutines.launch

class ReviewViewModel(app: Application) : AndroidViewModel(app)
{
    private val dao = AppDatabase.Companion.get(app).reviewDao()

    val reviews = dao.getAll()

    fun add(contact: Review) = viewModelScope.launch {
        val t = Thread{ dao.insert(contact)}
        t.start()

    }
    fun update(contact: Review) = viewModelScope.launch {
        val t = Thread{ dao.update(contact) }
        t.start()
    }
    fun delete(contact: Review) = viewModelScope.launch {
        val t = Thread{dao.delete(contact)}
        t.start()
    }
}
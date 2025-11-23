package com.example.reviewapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.collections.get
import kotlin.text.insert

class ReviewViewModel(app: Application) : AndroidViewModel(app)
{
    private val dao = AppDatabase.get(app).reviewDao()

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

package com.example.chmovie.presentation.my_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyListViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is MyListViewModel Fragment"
    }
    val text: LiveData<String> = _text
}
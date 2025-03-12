package com.example.blinknotes.ui.search;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchScreenViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SearchScreenViewModel::class.java)) {
            SearchScreenViewModel() as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
class SearchScreenViewModel : ViewModel() {

}
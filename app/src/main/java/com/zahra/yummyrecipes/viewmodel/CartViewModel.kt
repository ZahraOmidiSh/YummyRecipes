package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.data.database.entity.ShoppingListEntity
import com.zahra.yummyrecipes.data.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(val repository: ShoppingListRepository) : ViewModel() {
    val readShoppingListData = repository.local.loadShoppingList().asLiveData()

    fun deleteShoppingListItem(entity: ShoppingListEntity) = viewModelScope.launch {
        repository.local.deleteShoppingList(entity)
    }
}
package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zahra.yummyrecipes.data.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel  @Inject constructor(repository: ShoppingListRepository): ViewModel() {
    val readShoppingListData = repository.local.loadShoppingList().asLiveData()
}
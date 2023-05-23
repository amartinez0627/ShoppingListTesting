package com.example.android.shoppinglisttesting.repositories

import androidx.lifecycle.LiveData
import com.example.android.shoppinglisttesting.commons.Resource
import com.example.android.shoppinglisttesting.data.local.ShoppingItem
import com.example.android.shoppinglisttesting.data.remote.responses.ImageResponse

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}
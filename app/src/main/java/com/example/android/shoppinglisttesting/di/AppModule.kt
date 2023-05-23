package com.example.android.shoppinglisttesting.di

import android.content.Context
import androidx.room.Room
import com.example.android.shoppinglisttesting.commons.Constants.BASE_URL
import com.example.android.shoppinglisttesting.commons.Constants.DATABASE_NAME
import com.example.android.shoppinglisttesting.data.local.ShoppingItemDatabase
import com.example.android.shoppinglisttesting.data.remote.PixabayAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Provides
    @Singleton
    fun providePixabayApi() : PixabayAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }
}
package com.example.android.shoppinglisttesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.android.shoppinglisttesting.R
import com.example.android.shoppinglisttesting.data.local.ShoppingItem
import com.example.android.shoppinglisttesting.getOrAwaitValue
import com.example.android.shoppinglisttesting.launchFragmentInHiltContainer
import com.example.android.shoppinglisttesting.repositories.FakeShoppingRepositoryAndroidTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class AddShoppingItemFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ShoppingFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun pressBackButton_popBackStack(){
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(),navController)
        }

        pressBack()

        verify(navController).popBackStack()
    }

    @Test
    fun clickIvShoppingImage_navigateToImagePickFragment(){
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(),navController)
        }

        onView(withId(R.id.ivShoppingImage)).perform(click())

        verify(navController).navigate(
            AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
        )
    }

    @Test
    fun clickInsertIntoDb_shoppingItemInsertedIntoDb(){
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        val name = "shopping item"
        val amount = "5"
        val price = "5.5"
        launchFragmentInHiltContainer<AddShoppingItemFragment>(
            fragmentFactory = fragmentFactory
        ) {
            viewModel = testViewModel
        }

        onView(withId(R.id.etShoppingItemName)).perform(replaceText(name))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText(amount))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText(price))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(testViewModel.shoppingItems.getOrAwaitValue()).
        contains(ShoppingItem(name, amount.toInt() , price.toFloat(),"" ))
    }

/*    @Test
    fun pressBackButton_setsImageUrlToEmpty(){
        val navController = mock(NavController::class.java)
        val viewModel = mock(ShoppingViewModel::class.java)
        val testPath = "testPath"
        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(),navController)
        }

        viewModel.setCurrentImageUrl(testPath)

        pressBack()

        verify(viewModel).currentImageUrl.getOrAwaitValue().isEmpty()
    }*/
}
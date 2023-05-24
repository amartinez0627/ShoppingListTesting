package com.example.android.shoppinglisttesting.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.android.shoppinglisttesting.data.local.ShoppingItem
import com.example.android.shoppinglisttesting.databinding.ItemShoppingBinding
import javax.inject.Inject

class ShoppingItemAdapter @Inject constructor(
    private val glide: RequestManager
) :RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder>(){

    inner class ShoppingItemViewHolder(
        val binding: ItemShoppingBinding
        ): RecyclerView.ViewHolder(binding.root)

    private val difCallback = object : DiffUtil.ItemCallback<ShoppingItem>(){
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this,difCallback)

    var shoppingItems: List<ShoppingItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        val binding = ItemShoppingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return  ShoppingItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val shoppingItem = shoppingItems[position]
        with(holder){
            binding.apply {
                glide.load(shoppingItem.imageUrl).into(ivShoppingImage)

                tvName.text = shoppingItem.name
                val amountText = "${shoppingItem.amount}x"
                val priceText = "${shoppingItem.price}$"
                tvShoppingItemAmount.text = amountText
                tvShoppingItemPrice.text = priceText
            }
        }
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }
}
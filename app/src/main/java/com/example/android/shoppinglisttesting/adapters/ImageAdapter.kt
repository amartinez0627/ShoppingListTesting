package com.example.android.shoppinglisttesting.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.android.shoppinglisttesting.databinding.ItemImageBinding
import javax.inject.Inject

class ImageAdapter @Inject constructor(
    private val glide: RequestManager
) :RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){
    inner class ImageViewHolder(val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    var images: List<String>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )

        return ImageViewHolder(binding)
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit){
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url = images[position]
        with(holder){
            glide.load(url).into(binding.ivShoppingImage)

            binding.ivShoppingImage.setOnClickListener{
                onItemClickListener?.let { click ->
                    click(url)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}
package com.example.android.shoppinglisttesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.shoppinglisttesting.R
import com.example.android.shoppinglisttesting.adapters.ImageAdapter
import com.example.android.shoppinglisttesting.commons.Constants.GRID_SPAN_COUNT
import com.example.android.shoppinglisttesting.commons.Constants.SEARCH_TIME_DELAY
import com.example.android.shoppinglisttesting.commons.Status
import com.example.android.shoppinglisttesting.databinding.FragmentImagePickBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter
): Fragment(R.layout.fragment_image_pick) {

    lateinit var viewModel: ShoppingViewModel
    private var _binding: FragmentImagePickBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagePickBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]

        setupRecyclerView()
        subscribeToObservers()

        var job: Job? = null

        binding.etSearch.addTextChangedListener { editable->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchForImage(editable.toString())
                    }
                }
            }
        }

        imageAdapter.setOnItemClickListener { url ->
            findNavController().popBackStack()
            viewModel.setCurrentImageUrl(url)
        }
    }

    private fun setupRecyclerView(){
        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }
    }

    private fun subscribeToObservers(){
        viewModel.images.observe(viewLifecycleOwner){
            it?.getContentIfNotHandled()?.let { result ->
                when(result.status){
                    Status.SUCCESS -> {
                        val urls = result.data?.hits?.map { imageResult ->  imageResult.previewURL}
                        imageAdapter.images = urls ?: listOf()
                        binding.progressBar.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireView(),
                            result.message ?: "An unknown error occurred.",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.castprogramms.elegion.ui

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.castprogramms.elegion.Resource
import com.castprogramms.elegion.databinding.ChatsFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatsFragment : Fragment() {

    private val viewModel: ChatsViewModel by viewModel()
    private lateinit var binding: ChatsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChatsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.coroutineScope.launchWhenStarted {
            viewModel.getAllPosts().collectLatest {
                when (it) {
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            it.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.addressListView.adapter =
                            ArrayAdapter(requireContext(), R.layout.simple_list_item_1, it.data!!)
                    }
                }
            }
        }

    }
}
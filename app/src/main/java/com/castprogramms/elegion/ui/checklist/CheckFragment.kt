package com.castprogramms.elegion.ui.checklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.coroutineScope
import com.castprogramms.elegion.R
import com.castprogramms.elegion.data.CheckItem
import com.castprogramms.elegion.databinding.FragmentCheckBinding
import com.castprogramms.elegion.repository.Resource
import com.castprogramms.elegion.tools.buildTextInputDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckFragment : Fragment() {

    private lateinit var binding : FragmentCheckBinding
    private val viewModel : CheckViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAddButton()
        loadTasks()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initAddButton() {
        binding.floatingActionButton.setOnClickListener {
            buildTextInputDialog(requireContext(), "Задача"){
                if (it.isNotEmpty()){
                    lifecycle.coroutineScope.launch {
                        viewModel.createTask(CheckItem(title = it)).collectLatest {
                            when (it){
                                is Resource.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                }
                                is Resource.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }
                                is Resource.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    loadTasks()
                                }
                            }
                        }
                    }
                }
            }.show()
        }
    }

    private fun loadTasks() {
        lifecycle.coroutineScope.launch {
            viewModel.loadTasks().collectLatest{
                when (it) {
                    is Resource.Error -> {binding.progressBar.visibility = View.GONE }
                    is Resource.Loading -> {binding.progressBar.visibility = View.VISIBLE}
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.checkList.adapter =
                        ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it.data!!)
                    }
                }
            }
        }
    }
}
package com.castprogramms.elegion.ui.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.checkList.layoutManager = LinearLayoutManager(requireContext())
        initAddButton()
        loadTasks()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initAddButton() {
        binding.floatingActionButton.setOnClickListener {
            buildTextInputDialog(requireContext(), getString(R.string.task)){
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
                        val adapter = TaskListAdapter(it.data!!) { value, tack ->
                            viewModel.changeTaskStatus(tack, value)
                            loadTasks()
                        }
                        binding.checkList.adapter = adapter
                    }
                }
            }
        }
    }
}
package com.castprogramms.elegion.ui

import android.R
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.castprogramms.elegion.Resource
import com.castprogramms.elegion.data.TelegramAddress
import com.castprogramms.elegion.databinding.ChatsFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
        updateAddressList()
        setOnAddressListItemListener()
        binding.addAddressFab.setOnClickListener {
            buildAddressAddDialog().show()
        }
    }

    private fun setOnAddressListItemListener() {
        binding.addressListView.setOnItemClickListener { _, _, index, l ->
            viewModel.getChatByIndex(index)?.let {
                openUri(it.uri)
            }
        }
    }

    private fun openUri(uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

    private fun buildAddressAddDialog(): AlertDialog.Builder {
        var address = ""
        return AlertDialog.Builder(requireContext())
                .setTitle("Вставьте ссылку на ваш Telegram")
                .setView(EditText(requireContext()).apply {
                    addTextChangedListener { address = text.toString() }
                })
                .setPositiveButton("Ok") { _, _ ->
                    //TODO USERNAME
                    if (address.isNotEmpty()) {
                        lifecycle.coroutineScope.launch {
                            addAddress(TelegramAddress(
                                    "username",
                                    address
                            ))
                        }
                    }
                }
    }

    private fun updateAddressList() {
        lifecycle.coroutineScope.launch {
            loadAddressList()
        }
    }

    private suspend fun addAddress(ta: TelegramAddress) {
        viewModel.addPost(ta).collectLatest {
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
                    updateAddressList()
                }
            }
        }
    }

    private suspend fun loadAddressList() {
        viewModel.loadAllPosts().collectLatest {
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
package com.castprogramms.elegion.ui.chats

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.castprogramms.elegion.data.TelegramAddress
import com.castprogramms.elegion.databinding.ChatsFragmentBinding
import com.castprogramms.elegion.repository.Resource
import com.castprogramms.elegion.tools.ChatsAdapter
import com.castprogramms.elegion.tools.buildTextInputDialog
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
//        setOnAddressListItemListener()
        binding.addAddressFab.setOnClickListener {
            buildAddressAddDialog().show()
        }
    }

    /*private fun setOnAddressListItemListener() {
        binding.addressRecyclerView.setOnItemClickListener { _, _, index, l ->
            viewModel.getChatByIndex(index)?.let {
                openUri(it.uri)
            }
        }
    }*/

    private fun openUri(uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

    private fun buildAddressAddDialog(): AlertDialog {
        return buildTextInputDialog(
            requireContext(),
            "Вставьте ссылку на ваш Telegram"
        ) {
            if (it.isNotEmpty()) {
                lifecycle.coroutineScope.launch {
                    addAddress(
                        TelegramAddress(
                            "username",
                            it
                        )
                    )
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
                    binding.addressRecyclerView.hideShimmer()
                    Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is Resource.Loading -> {
                    binding.addressRecyclerView.showShimmer()
                }
                is Resource.Success -> {
                    binding.addressRecyclerView.hideShimmer()
                    updateAddressList()
                }
            }
        }
    }

    private suspend fun loadAddressList() {
        viewModel.loadAllPosts().collectLatest {
            when (it) {
                is Resource.Error -> {
                    binding.addressRecyclerView.hideShimmer()
                    Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is Resource.Loading -> {
                    binding.addressRecyclerView.showShimmer()
                }
                is Resource.Success -> {
                    binding.addressRecyclerView.hideShimmer()
                    binding.addressRecyclerView.adapter = ChatsAdapter(it.data) { openUri(it) }
                }
            }
        }
    }
}
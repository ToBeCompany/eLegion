package com.castprogramms.elegion.ui.chats

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.castprogramms.elegion.R
import com.castprogramms.elegion.data.TelegramAddress
import com.castprogramms.elegion.databinding.ChatsAlertDialogBinding
import com.castprogramms.elegion.databinding.ChatsFragmentBinding
import com.castprogramms.elegion.repository.Resource
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
        binding.addAddressFab.setOnClickListener {
            buildAddressAddDialog().show()
        }
    }

    private fun openUri(uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

    private fun buildAddressAddDialog(): AlertDialog {
        return buildTextInputDialog(
            requireContext(),
            getString(R.string.hintPuturi)
        ) { link: String, name: String ->
            if (link.isNotEmpty() && name.isNotEmpty()) {
                lifecycle.coroutineScope.launch {
                    addAddress(TelegramAddress(name, link))
                }
            }
        }
    }

    private fun updateAddressList() {
        lifecycle.coroutineScope.launch {
            loadAddressList()
        }
    }

    private fun buildTextInputDialog(
        context: Context,
        title: String,
        result: (input: String, name: String) -> Unit
    ): AlertDialog {
        var address = ""
        val view = LayoutInflater.from(context).inflate(R.layout.chats_alert_dialog, null)
        val binding = ChatsAlertDialogBinding.bind(view)
        val ad = AlertDialog.Builder(context)
            .setView(view)
            .create()
            .apply {
                if (window != null)
                    window!!.setBackgroundDrawable(ColorDrawable(0))
            }

        binding.button.setOnClickListener {
            result(address, binding.titleTg.text.toString())
            ad.dismiss()
        }
        binding.textDialog.apply {
            addTextChangedListener { address = text.toString() }
        }
        binding.textTitle.text = title
        return ad
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
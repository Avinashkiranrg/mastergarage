package com.vendor.mastergarage.ui.outerui.fragment_main.account.all.contact

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.databinding.FragmentContactUsBinding
import com.vendor.mastergarage.model.ContactusItem
import com.vendor.mastergarage.model.FaqItem
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent
import android.net.Uri
import androidx.navigation.Navigation
import com.vendor.mastergarage.R


@AndroidEntryPoint
class ContactUsFragment : Fragment() {

    lateinit var binding: FragmentContactUsBinding
    private val viewModel: ContactUsFragViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContactUsBinding.inflate(inflater, container, false)

        binding.logo.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.contact.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        binding.numbers.text = "${vItem.phone} / ${vItem.alternatePhone}"
                        binding.MailUsId1.text = "${vItem.emailId}"
                        binding.MailUsId2.text = "${vItem.alternateEmailId}"
                        binding.MeetusAddress.text = "${vItem.address}"

                        binding.faceBookBtn.setOnClickListener {
                            val shareText = "${vItem.facebookLink}"
                            gotoUrl(shareText)

                        }
                        binding.instagramBtn.setOnClickListener {
                            val shareText = "${vItem.instagramLink}"
                            gotoUrl(shareText)

                        }
                        binding.twitterBtn.setOnClickListener {
                            val shareText = "${vItem.twitterLink}"
                            gotoUrl(shareText)

                        }
                        binding.linkedinBtn.setOnClickListener {
                            val shareText = "${vItem.linkedinLink}"
                            gotoUrl(shareText)

                        }
                    }
                }
                is Response.Failure -> {
                    // Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT) .show()
                    Log.e(TAG, it.errorMessage.toString())
                }
            }
        })

        binding.fAQs.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_contactUsFragment_to_FAQsFragment)
        }

        return binding.root
    }

    private fun gotoUrl(shareText: String) {
        val uri: Uri =
            Uri.parse(shareText) // missing 'http://' will cause crashed

        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {

        }
    }

    companion object {
        private const val TAG = "ContactUsFragment"
    }
}
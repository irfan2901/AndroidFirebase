package com.example.firebasedemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.firebasedemo.databinding.FragmentOtpVerificationBinding
import com.google.firebase.auth.FirebaseAuth

class OtpVerificationFragment : Fragment() {

    private lateinit var binding: FragmentOtpVerificationBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var navController: NavController
    private lateinit var phoneNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        navController = Navigation.findNavController(view)

        phoneNumber = arguments?.getString("number").toString()

        binding.tvNumber.text = phoneNumber

        authViewModel.authState.observe(viewLifecycleOwner) { isAuthenticated ->
            if (isAuthenticated) {
                val bundle = Bundle().apply {
                    putString("number", phoneNumber)
                    putString("id", FirebaseAuth.getInstance().currentUser?.uid)
                }
                navController.navigate(
                    R.id.action_otpVerificationFragment_to_setupProfileFragment,
                    bundle
                )
            }
        }

        authViewModel.otpState.observe(viewLifecycleOwner) { isSent ->
            if (isSent) {
                Toast.makeText(requireContext(), "Otp sent", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backArrow.setOnClickListener {
            navController.popBackStack()
        }

        binding.loginBtn.setOnClickListener {
            val editTexts = arrayOf(
                binding.etOtp1,
                binding.etOtp2,
                binding.etOtp3,
                binding.etOtp4,
                binding.etOtp5,
                binding.etOtp6
            )
            val enteredOtp = editTexts.joinToString("") { it.text.toString() }

            if (enteredOtp.length < editTexts.size) {
                Toast.makeText(requireContext(), "Invalid OTP", Toast.LENGTH_SHORT).show()
            } else {
                editTexts.forEach { it.text?.clear(); it.clearFocus() }
                verifyOtpAndSignIn(enteredOtp)
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigate(R.id.action_otpVerificationFragment_to_phoneNumberFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        otpEditTextListener()
        sendOtp()
    }

    private fun sendOtp() {
        authViewModel.sendOtp(phoneNumber, requireActivity())
    }

    private fun verifyOtpAndSignIn(enteredOtp: String) {
        authViewModel.verifyOtp(enteredOtp)
    }

    private fun otpEditTextListener() {
        val editTexts = arrayOf(
            binding.etOtp1,
            binding.etOtp2,
            binding.etOtp3,
            binding.etOtp4,
            binding.etOtp5,
            binding.etOtp6
        )

        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val length = s?.length ?: 0
                    if (length == 1 && i < editTexts.size - 1) {
                        editTexts[i + 1].requestFocus()
                    } else if (length == 0 && i > 0) {
                        editTexts[i - 1].requestFocus()
                    }
                }
            })

            editTexts[i].setOnKeyListener { _, keyCode, _ ->
                if (editTexts[i].text?.length == 1 && keyCode != KeyEvent.KEYCODE_DEL) {
                    return@setOnKeyListener true
                }
                false
            }
        }
    }
}
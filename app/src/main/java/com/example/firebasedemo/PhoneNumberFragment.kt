package com.example.firebasedemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.firebasedemo.databinding.FragmentPhoneNumberBinding
import com.google.firebase.auth.FirebaseAuth

class PhoneNumberFragment : Fragment() {

    private lateinit var binding: FragmentPhoneNumberBinding
    private lateinit var navController: NavController
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        val authenticatedUser = FirebaseAuth.getInstance().currentUser
        if (authenticatedUser != null) {
            navController.navigate(R.id.action_phoneNumberFragment_to_profileFragment)
        }

        binding.sendOtpButton.setOnClickListener {
            val number = binding.etNumber.text.toString()
            if (number.length < 10) {
                binding.etNumberLayout.error = "Enter a valid number"
            } else {
                binding.etNumberLayout.error = null
                val bundle = Bundle().apply {
                    putString("number", number)
                }
                navController.navigate(
                    R.id.action_phoneNumberFragment_to_otpVerificationFragment,
                    bundle
                )
            }
        }
    }
}
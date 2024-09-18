package com.example.firebasedemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.firebasedemo.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        userViewModel.showCurrentUserDetails()
        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.textViewName.text = it.name ?: "N/A"
                binding.textViewEmail.text = it.email ?: "N/A"
            }
        }

        binding.updateProfileButton.setOnClickListener {
            val bundle = Bundle().apply {
                putString("name", binding.textViewName.text.toString())
                putString("email", binding.textViewEmail.text.toString())
            }
            navController.navigate(R.id.action_profileFragment_to_updateProfileFragment, bundle)
        }
    }
}
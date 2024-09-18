package com.example.firebasedemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.firebasedemo.databinding.FragmentSetupProfileBinding

class SetupProfileFragment : Fragment() {
    private lateinit var binding: FragmentSetupProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var navController: NavController
    private var id: String = ""
    private var number: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetupProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        id = arguments?.getString("id").toString()
        number = arguments?.getString("number").toString()

        userViewModel.userLiveData.observe(viewLifecycleOwner) { userModel ->
            if (userModel != null) {
                navController.navigate(R.id.action_setupProfileFragment_to_profileFragment)
            }
        }

        binding.saveProfileButton.setOnClickListener {
            val name = binding.etProfileName.text.toString()
            val email = binding.etProfileEmail.text.toString()

            if (name.isEmpty()) {
                binding.profileNameLayout.error = "Name cannot be empty"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.profileEmailLayout.error = "Email cannot be empty"
                return@setOnClickListener
            }

            binding.profileNameLayout.error = null
            binding.profileEmailLayout.error = null

            val user = User(id = id, phoneNumber = number, name = name, email = email)

            userViewModel.saveUserToDatabase(user) {
                navController.navigate(R.id.action_setupProfileFragment_to_profileFragment)
            }
        }
    }
}
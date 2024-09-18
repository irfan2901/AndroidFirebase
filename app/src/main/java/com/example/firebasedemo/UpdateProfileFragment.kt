package com.example.firebasedemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.firebasedemo.databinding.FragmentUpdateProfileBinding

class UpdateProfileFragment : Fragment() {

    private lateinit var binding: FragmentUpdateProfileBinding
    private lateinit var navController: NavController
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        userViewModel.showCurrentUserDetails()
        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            binding.etUpdateName.setText(user?.name ?: "")
            binding.etUpdateEmail.setText(user?.email ?: "")
        }

        binding.saveUpdateButton.setOnClickListener {
            val updatedName = binding.etUpdateName.text.toString()
            val updatedEmail = binding.etUpdateEmail.text.toString()
            val updatedUser = User(
                id = userViewModel.userLiveData.value?.id,
                phoneNumber = userViewModel.userLiveData.value?.phoneNumber,
                name = updatedName,
                email = updatedEmail
            )
            userViewModel.updateUser(updatedUser) {
                navController.navigate(R.id.action_updateProfileFragment_to_profileFragment)
            }
        }
    }
}
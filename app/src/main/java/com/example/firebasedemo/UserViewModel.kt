package com.example.firebasedemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserViewModel : ViewModel() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val uid: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val userRef: DatabaseReference = database.getReference("Users").child(uid ?: "")

    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: LiveData<User?> get() = _userLiveData

    fun saveUserToDatabase(user: User, onSuccess: () -> Unit) {
        uid?.let {
            userRef.setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    // Handle error if needed
                }
            }
        }
    }

    fun updateUser(updatedUser: User, onSuccess: () -> Unit) {
        uid?.let {
            val updates = mapOf(
                "id" to updatedUser.id,
                "phoneNumber" to updatedUser.phoneNumber,
                "name" to updatedUser.name,
                "email" to updatedUser.email
            )
            userRef.updateChildren(updates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                }
            }
        }
    }

    fun deleteUser() {
        uid?.let {
            userRef.removeValue()
        }
    }

    fun showCurrentUserDetails() {
        uid?.let {
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    _userLiveData.postValue(user)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
}
package com.example.SlothGaming.data.fire_base

import com.example.SlothGaming.data.models.User
import com.example.SlothGaming.data.repository.AuthRepository
import com.example.SlothGaming.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import safeCall

class fireBaseAuth(): AuthRepository {
    private val fireBaseAuth  = FirebaseAuth.getInstance()

    private val userRef = FirebaseFirestore.getInstance().collection("users")
    override suspend fun currentUser(): Resource<User> {
        return withContext(Dispatchers.IO){
            safeCall {
                val userId  = fireBaseAuth.currentUser!!.uid
                val currentUser =  userRef.document(userId)
                   .get().await().toObject(User::class.java)
                Resource.Success(currentUser!!)
            }
        }
    }

    override suspend fun createUser(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Resource<User> {
        return withContext(Dispatchers.IO){
            safeCall {
                val registerResult =
                    fireBaseAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = registerResult.user?.uid!!
                val newUser = User(firstName,lastName,email,phoneNumber)
                userRef.document(userId).set(newUser).await()
                Resource.Success(newUser)
            }

        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Resource<User> {
        return withContext(Dispatchers.IO){
            safeCall {
                val loginResult = fireBaseAuth.signInWithEmailAndPassword(email,password).await()
                val userId = loginResult.user?.uid!!
                val currentUser =  userRef.document(userId)
                    .get().await().toObject(User::class.java)

                Resource(currentUser)
            }
        }
    }

    override fun logOut() {
        fireBaseAuth.signOut()
    }
}
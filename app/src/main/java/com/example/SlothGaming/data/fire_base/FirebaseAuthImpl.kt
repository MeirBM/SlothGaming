package com.example.SlothGaming.data.fire_base

import com.example.SlothGaming.data.models.User
import com.example.SlothGaming.data.repository.AuthRepository
import com.example.SlothGaming.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import safeCall
import javax.inject.Inject

class FirebaseAuthImpl @Inject constructor
    (private val firebaseAuth: FirebaseAuth,
     private val firestore: FirebaseFirestore ): AuthRepository {


    private val userRef = firestore.collection("users")
    override suspend fun currentUser(): Resource<User> {
        return withContext(Dispatchers.IO){
            safeCall {
                val userId  = firebaseAuth.currentUser?.uid!!
                val currentUser =  userRef.document(userId)
                   .get().await().toObject(User::class.java)
                Resource.success(currentUser!!)
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
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = registerResult.user?.uid!!
                val newUser = User(firstName,lastName,email,phoneNumber)
                userRef.document(userId).set(newUser).await()
                Resource.success(newUser)
            }

        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Resource<User> {
        return withContext(Dispatchers.IO){
            safeCall {
                val loginResult = firebaseAuth.signInWithEmailAndPassword(email,password).await()
                val userId = loginResult.user?.uid!!
                val currentUser =  userRef.document(userId)
                    .get().await().toObject(User::class.java)
                Resource.success(currentUser!!)
            }
        }
    }

    override fun logOut() {
        firebaseAuth.signOut()
    }

    override   fun isUserAuth(): Boolean {
        return firebaseAuth.currentUser != null
    }
}
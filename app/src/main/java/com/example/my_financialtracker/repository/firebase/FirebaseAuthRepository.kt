package com.example.my_financialtracker.repository.firebase

import com.example.my_financialtracker.repository.AuthRepository
import com.example.my_financialtracker.repository.AuthUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthUser> {
        return runCatching {
            val result = firebaseAuth.signInWithEmailAndPassword(email.trim(), password).await()
            val user = requireNotNull(result.user) { "User account not available." }
            AuthUser(
                uid = user.uid,
                displayName = user.displayName ?: user.email.orEmpty(),
                email = user.email.orEmpty(),
            )
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<AuthUser> {
        return runCatching {
            val result = firebaseAuth.createUserWithEmailAndPassword(email.trim(), password).await()
            val user = requireNotNull(result.user) { "User account could not be created." }
            user.updateProfile(
                com.google.firebase.auth.userProfileChangeRequest {
                    displayName = name.trim()
                },
            ).await()
            AuthUser(
                uid = user.uid,
                displayName = name.trim(),
                email = user.email.orEmpty(),
            )
        }
    }
}

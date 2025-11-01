package uk.ac.tees.mad.careerconnect.presentation.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider

import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {


    private val _currentUserData = MutableStateFlow(GetUserInfo())
    val currentUserData: StateFlow<GetUserInfo> = _currentUserData

    val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun logoutUser() {

        auth.signOut()

    }



    fun signUp(
        email: String,
        password: String,
        name: String,
        onResult: (String, Boolean) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid

                        if (userId != null) {
                            val userInfo = PostUserInfo(
                                profileImageUrl = "",
                                name = name,
                                email = email,
                                uid = userId,
                                passkey = password,

                            )

                            db.collection("user").document(userId).set(userInfo)
                                .addOnSuccessListener {
                                    onResult("Signup successful", true)
                                }.addOnFailureListener { exception ->
                                    auth.currentUser?.delete() // rollback user creation
                                    onResult("Failed to save user info", false)
                                }
                        } else {
                            onResult("User ID not found", false)
                        }
                    } else {
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthUserCollisionException -> "This email is already registered"
                            is FirebaseAuthWeakPasswordException -> "Password is too weak"
                            else -> task.exception?.localizedMessage ?: "Signup failed"
                        }
                        onResult("mine $errorMessage ", false)
                    }
                }
            } catch (e: Exception) {
                onResult("Unexpected error: ${e.localizedMessage}", false)
            }
        }
    }


    fun logIn(
        email: String,
        passkey: String,
        onResult: (String, Boolean) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, passkey).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult("Login successful", true)
                    } else {
                        val errorMessage = task.exception?.localizedMessage ?: "Login failed"
                        onResult(errorMessage, false)
                    }
                }
            } catch (e: Exception) {
                onResult("Error: ${e.localizedMessage}", false)
            }
        }
    }

    fun handleGoogleSignIn(
        context: Context,
        onResult: (String, Boolean) -> Unit,
    ) {
        viewModelScope.launch {
            googleSignIn(context).collect { result ->
                result.fold(onSuccess = { authResult ->
                    val currentUser = authResult.user
                    if (currentUser != null) {
                        val postUserInfo = PostUserInfo(
                            profileImageUrl = currentUser.photoUrl?.toString() ?: "",
                            name = currentUser.displayName ?: "",
                            email = currentUser.email ?: "",
                            uid = currentUser.uid,
                            passkey = "",

                        )

                        db.collection("user").document(currentUser.uid).set(postUserInfo)
                            .addOnSuccessListener {
                                onResult("Signup successful", true)
                            }.addOnFailureListener { exception ->
                                onResult(
                                    "Failed to save user info: ${exception.localizedMessage}",
                                    false
                                )
                            }
                    } else {
                        onResult("Google sign-in failed: user is null", false)
                    }
                }, onFailure = { e ->
                    onResult("Google sign-in failed: ${e.localizedMessage}", false)
                })
            }
        }
    }


    private suspend fun googleSignIn(context: Context): Flow<Result<AuthResult>> {
        val firebaseAuth = FirebaseAuth.getInstance()

        return callbackFlow {
            try {
                val credentialManager = CredentialManager.create(context)

                val googleIdOption =
                    GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false)
                        .setServerClientId("745490922487-23veqpd9ubdep99h7q97j5llda4l02ai.apps.googleusercontent.com")
                        .setAutoSelectEnabled(true).build()

                val request =
                    GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()

                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    val authCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                    trySend(Result.success(authResult))
                } else {
                    trySend(Result.failure(Exception("Invalid credential type.")))
                }
            } catch (e: GetCredentialCancellationException) {
                trySend(Result.failure(Exception("Sign-in was canceled.")))
            } catch (e: Exception) {
                trySend(Result.failure(e))
            }

            awaitClose { close() }
        }
    }








}


data class PostUserInfo(
    val profileImageUrl: String,
    val name: String,
    val email: String,
    val uid: String,
    val passkey: String,

)

data class GetUserInfo(
    val profileImageUrl: String = "",
    val name: String = "",
    val email: String = "",
    val uid: String = "",
    val passkey: String = "",

)

package uk.ac.tees.mad.weatherly.presentaion.utilsScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.sqlite.driver.AndroidSQLiteConnection
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel

@Composable
fun ProfilePage(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {

    Box(contentAlignment = Alignment.Center){
        Button(onClick = { authViewModel.logoutUser() }) { }
    }



}
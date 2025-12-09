package uk.ac.tees.mad.weatherly.presentaion.HomeScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavController
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel
import uk.ac.tees.mad.weatherly.R
import uk.ac.tees.mad.weatherly.presentaion.utilsScreens.FavPage
import uk.ac.tees.mad.weatherly.presentaion.utilsScreens.HomePage
import uk.ac.tees.mad.weatherly.presentaion.utilsScreens.ProfilePage
import uk.ac.tees.mad.weatherly.presentaion.viewModels.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavController,
    homeViewModel: HomeViewModel,
) {

    val navItems = listOf(
        NavItems(
            "Favorite",
            filledIcon = Icons.Filled.Favorite,
            outlinedIcon = Icons.Outlined.FavoriteBorder
        ),
        NavItems(
            "Home",
            filledIcon = Icons.Filled.Home,
            outlinedIcon = Icons.Outlined.Home
        ),
        NavItems(
            "Profile",
            filledIcon = Icons.Filled.AccountCircle,
            outlinedIcon = Icons.Outlined.AccountCircle
        )
    )

    var selectedIndex by rememberSaveable { mutableIntStateOf(1) }

    Scaffold(modifier.fillMaxSize(), bottomBar = {
        NavigationBar(
            modifier = Modifier.height(70.dp),
            containerColor = colorResource(id = R.color.app)
        ) {

            navItems.fastForEachIndexed() { index, navItem ->
                val isSelected = selectedIndex == index
                NavigationBarItem(
                    modifier = Modifier.offset(y = 10.dp),
                    selected = false,
                    onClick = {
                        selectedIndex = index
                    },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) navItem.filledIcon else navItem.outlinedIcon,
                            contentDescription = null,
                            tint = Color(0xFF000000)

                        )
                    },
                    label = {
                        Text(
                            text = navItem.title,
                            modifier = Modifier.offset(y = (-4).dp),
                            color = Color(0xFF000000)
                        )
                    }
                )
            }
        }
    }) { innerPadding ->

        ContentScreen(
            selectedIndex = selectedIndex,
            navController = navController,
            authViewModel = authViewModel,
            homeViewModel = homeViewModel,
            modifier = Modifier.padding(innerPadding),

        )

    }
}


data class NavItems(val title: String, val filledIcon: ImageVector, val outlinedIcon: ImageVector)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentScreen(

    selectedIndex: Int,
    navController: NavController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    modifier: Modifier,
) {
    MaterialTheme {
        when (selectedIndex) {


            0 -> {
                FavPage(
                    homeViewModel = homeViewModel,
                    authViewModel = authViewModel,
                    navController = navController
                )


            }

            1 -> HomePage(
                homeViewModel = homeViewModel,
                authViewModel = authViewModel,
                navController = navController,

            )


            2 -> ProfilePage(
                authViewModel = authViewModel,
                homeViewModel = homeViewModel



            )


        }
    }
}
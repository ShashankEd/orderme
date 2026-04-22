package com.product.orderfromhere.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.product.orderfromhere.viewmodel.ApolloViewModel
import com.product.orderfromhere.viewmodel.LoginViewModel
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.product.orderfromhere.GetAllProductQuery
import com.product.orderfromhere.model.Product
import com.product.orderfromhere.model.server.createApolloClient
import com.product.orderfromhere.view.navigation.AppRoute
import com.product.orderfromhere.view.navigation.AuthRoute
import com.product.orderfromhere.view.navigation.SplashRoute
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val products: ArrayList<Product>) : UiState()
    data class Error(val message: String) : UiState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navHostController: NavHostController) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha =  0f)
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
        modifier = Modifier.shadow(0.dp),
        title = { Text(text = "Dashboard") },
        navigationIcon = {
             if(navHostController.currentDestination?.route != AppRoute.Dashboard.route) {
                 IconButton(onClick = {
                     // Exit the app
                     navHostController.popBackStack()
                 }) {
                     Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Go back")
                 }
             } else {

             }
        },
        actions = { },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: LoginViewModel, navController: NavHostController, apolloViewModel: ApolloViewModel) {
//    apolloViewModel.updateEndpoint("http://10.0.2.2:8000/graphql/other")
    println("Apollo base url = ${apolloViewModel.baseURL.value}")
    val apolloClient = createApolloClient(apolloViewModel.baseURL.value)
    val scope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf<UiState>(UiState.Loading) }

    val sessionToken by viewModel.sessionFromStore.collectAsState()
    println("user name: ${viewModel.userNameData.value}")
    println("sessionToken in Dashboard = ${sessionToken}")
    if(sessionToken.isNotEmpty()) {
        LaunchedEffect(Unit) {
            val response = apolloClient.query(GetAllProductQuery())
                .addHttpHeader("Authorization", "Bearer $sessionToken")
                .execute()
            println("GetAllProduct Response = ${response.data?.getAllProduct.toString()}")

            if(!response.data?.getAllProduct.isNullOrEmpty()) {
                val products = response.data?.getAllProduct?.map { item ->
                    Product(
                        id = item?.id,
                        title = item?.title,
                        brand = item?.brand,
                        description = item?.description,
                    )
                } as ArrayList< Product>
                if(products.isNotEmpty()) {
                    uiState = UiState.Success(products = products)
                } else {
                    uiState = UiState.Error("Get product failed")
                }
            } else {
                uiState = UiState.Error("Get product failed")
            }
        }
    }

    when(uiState) {
        is UiState.Success -> {
            val products = (uiState as UiState.Success).products
            Scaffold(
                topBar = { AppBar(navController) }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        // Apply the padding provided by the Scaffold
                        .padding(innerPadding)
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                            scope.launch {
                                // truncate the session token from the data store
//                                viewModel.deleteSessionToken()
                                // navigate to auth_graph
//                navController.navigate("auth_graph") {
//                    popUpTo(AppRoute.Dashboard.route) { inclusive = true }
//                }
                                navController.navigate(AppRoute.Settings.route)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn {
                        items(items = products) {

                                item ->
                            Card(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    text = item.title.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(30.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        is UiState.Error -> {
            println("****** GetProduct Error*****")
            Scaffold(
                topBar = { AppBar(navController) }
            ) { innerPadding ->
                Card(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "Some Error",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
        is UiState.Loading -> {
            println("****** GetProduct Loading*****")

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Products") },
                        modifier = Modifier.statusBarsPadding()
                    )
                }
            ) { innerPadding ->
                Card(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "Loading....",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
        else -> {
            println("****** Some error ****** ")
            Scaffold(
                topBar = { AppBar(navController) }
            ) { innerPadding ->
                Card(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "Some Error",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
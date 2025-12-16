package com.product.orderfromhere.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.product.orderfromhere.LoginMutation
import com.product.orderfromhere.RegisterMutation
import com.product.orderfromhere.model.server.createApolloClient
import com.product.orderfromhere.view.navigation.AppNavigation
import com.product.orderfromhere.view.navigation.AppRoute
import com.product.orderfromhere.view.navigation.AuthRoute
//import com.product.orderfromhere.view.navigation.NavigationHostController
//import com.product.orderfromhere.view.navigation.Route
import com.product.orderfromhere.view.ui.theme.OrderFromHereTheme
import com.product.orderfromhere.viewmodel.ApolloViewModel
import com.product.orderfromhere.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

//    private val loginViewModel by viewModels<LoginViewModel>()
    private val apolloViewModel by viewModels<ApolloViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        apolloViewModel.updateEndpoint("authenticate")
        setContent {
            OrderFromHereTheme {
//                LoginForm(loginViewModel)
                 val navHostController = rememberNavController()
//                NavigationHostController(navHostController)
                AppNavigation()
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavHostController, apolloViewModel: ApolloViewModel) {
    LoginOrRegisterForm(viewModel, true, navController, apolloViewModel)
}

@Composable
fun RegisterScreen(viewModel: LoginViewModel, navController: NavHostController, apolloViewModel: ApolloViewModel) {
    LoginOrRegisterForm(viewModel, false, navController, apolloViewModel)
}


@Composable
fun LoginOrRegisterForm(viewModel: LoginViewModel, isLogin: Boolean, navController: NavHostController, apolloViewModel: ApolloViewModel) {
    val userNameValue by viewModel.userNameData.observeAsState("")
    val passwordValue by viewModel.passwordData.observeAsState("")
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val apolloClient = createApolloClient(apolloViewModel.baseURL.value)
    println("isLogin = ${isLogin}")
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
                .padding(horizontal = 30.dp
                )

        ) {
            Text(
                text = "Order Here!",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.height(50.dp))

            LoginField(
                value = userNameValue,
                onChange = fun(value: String) {
                    println("Login username is ${value}")
                    //update the live data
                    if(value.isNotEmpty() && value.isNotBlank()) {
                        viewModel.updateUsername(value.trim())
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .background(color = Color.Transparent),
                label = "Username",
                placeholder = viewModel.userNameData?.value.toString()
            )
            Spacer(modifier = Modifier.height(20.dp))

            PasswordField(
                value = passwordValue,
                onChange = fun(value: String) {
                    println("Login password is ${value}")
                    //update the live data
                    if(value.isNotEmpty() && value.isNotBlank()) {
                        viewModel.updatePassword(value.trim())
                    }
                },
                submit = {},
                modifier = Modifier.fillMaxWidth(),
                label = "Password",
                placeholder = "Password"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = fun() {
                    // TODO: call the API here to login & remove the logs
                    println("******* Calling  ${if (isLogin) "Login" else "Register"}  API *******")
                    println("Username: ${viewModel.userNameData.value}")
                    println("Password: ${viewModel.passwordData.value}")
                    scope.launch {
                        if(isLogin) {
                            val response = apolloClient.mutation(LoginMutation(
                                usernam = viewModel.userNameData?.value.toString(),
                                pass = viewModel.passwordData?.value.toString()
                            )).execute()

                            if(response?.data != null) {
                                println("******* Login Success *******")
                                println(" Login  response Token: ${response.data?.login}")
                                if(!response.data?.login.isNullOrEmpty()) {
                                    Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
                                    viewModel.updateSessionToken(response?.data?.login.toString())
                                    navController.navigate(AppRoute.Dashboard.route)
                                }
                            } else {
                                println("*******  Login  Failed *******")
                            }
                        } else {
                            val response = apolloClient.mutation(RegisterMutation(
                                usernam = viewModel.userNameData?.value.toString(),
                                pass = viewModel.passwordData?.value.toString(),
                                isAd = false
                            )).execute()
                            if(response?.data != null) {
                                println("******* Register Success *******")
                                println(" Register response: ${response.data?.registerUser}")
                                Toast.makeText(context, "Register Successful", Toast.LENGTH_LONG).show()
                                if(response.data?.registerUser == "Registration successful") {
                                    navController.navigate(AuthRoute.Login.route)
                                }
                            } else {
                                println("*******  Register  Failed *******")
                            }
                        }
                    }
                },
                enabled = true,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Text( if(isLogin)  "Login" else "Register")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = if(!isLogin) "Already have an account, Click to Login! " else "No Account, click here to register!",
                color = if(isLogin ) Color.Red else Color.Blue,
                modifier = Modifier.clickable {
                    if(isLogin)
                        navController.navigate(AuthRoute.Register.route)
                    else
                        navController.navigate(AuthRoute.Login.route)
                }
            )
        }
    }
}

@Composable
fun LabeledCheckbox(
    label: String,
    onCheckChanged: () -> Unit,
    isChecked: Boolean
) {

    Row (
        Modifier
            .clickable(
                onClick = onCheckChanged
            )
            .padding(4.dp)
    ) {
        Checkbox(checked = isChecked, onCheckedChange = null)
        Spacer(Modifier.size(6.dp))
        Text(label)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OrderFromHereTheme {
        Greeting("Android")
    }
}
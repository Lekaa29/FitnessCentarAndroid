package com.example.fitnesscentarchat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.fitnesscentarchat.ui.theme.*

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitnesscentarchat.data.api.RetrofitBuilder
import com.example.fitnesscentarchat.data.repository.AttendanceRepository
import com.example.fitnesscentarchat.data.repository.AuthRepository
import com.example.fitnesscentarchat.data.repository.CacheRepository
import com.example.fitnesscentarchat.data.repository.FitnessCenterRepository
import com.example.fitnesscentarchat.data.repository.MembershipRepository
import com.example.fitnesscentarchat.data.repository.MessageRepository
import com.example.fitnesscentarchat.data.repository.ShopRepository
import com.example.fitnesscentarchat.data.repository.UserRepository
import com.example.fitnesscentarchat.ui.screens.chat.ChatScreen
import com.example.fitnesscentarchat.ui.screens.chat.ChatViewModel
import com.example.fitnesscentarchat.ui.screens.chat.ChatsScreen
import com.example.fitnesscentarchat.ui.screens.fitnessCenter.FitnessCenterScreen
import com.example.fitnesscentarchat.ui.screens.fitnessCenter.FitnessCenterViewModel
import com.example.fitnesscentarchat.ui.screens.fitnessCenter.FitnessCenterWithBottomNav
import com.example.fitnesscentarchat.ui.screens.hub.HubScreen
import com.example.fitnesscentarchat.ui.screens.hub.HubViewModel
import com.example.fitnesscentarchat.ui.screens.hub.ShopViewModel
import com.example.fitnesscentarchat.ui.screens.login.LoginScreen
import com.example.fitnesscentarchat.ui.screens.login.LoginViewModel
import com.example.fitnesscentarchat.ui.screens.shop.PurchasesScreen
import com.example.fitnesscentarchat.ui.screens.users.UsersScreen
import com.example.fitnesscentarchat.ui.screens.users.UsersViewModel
import com.google.gson.Gson


class MainActivity : ComponentActivity() {

    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var messageRepository: MessageRepository
    private lateinit var attendanceRepository: AttendanceRepository
    private lateinit var fitnessCenterRepository: FitnessCenterRepository
    private lateinit var membershipRepository: MembershipRepository
    private lateinit var shopRepository: ShopRepository
    private lateinit var cacheRepository: CacheRepository

    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitBuilder.init(applicationContext)

        // Initialize repositories
        val sharedPreferences = getSharedPreferences("chat_app_prefs", MODE_PRIVATE)
        val apiService = RetrofitBuilder.chatApiService

        authRepository = AuthRepository(apiService, sharedPreferences)
        userRepository = UserRepository(apiService, authRepository)
        messageRepository = MessageRepository(apiService, authRepository)

        attendanceRepository = AttendanceRepository(apiService, authRepository)
        fitnessCenterRepository = FitnessCenterRepository(apiService)
        membershipRepository = MembershipRepository(apiService, authRepository)
        shopRepository = ShopRepository(apiService, authRepository)
        cacheRepository = CacheRepository(applicationContext, Gson())

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable("login") {
                            val viewModel = LoginViewModel(authRepository)
                            LoginScreen(
                                viewModel = viewModel,
                                onNavigateToHub = {
                                    navController.navigate("hub") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("hub") {
                            val viewModel = HubViewModel(fitnessCenterRepository, membershipRepository, cacheRepository, authRepository)
                            HubScreen(
                                viewModel = viewModel,
                                onFitnessCenterSelected = { fitnessCenterId ->
                                    navController.navigate("fitnessCenter/$fitnessCenterId")
                                },
                                onUserChatSelect = {
                                    navController.navigate("messages")

                                },
                                onUserItemsSelect = {
                                    navController.navigate("purchases")
                                }
                            )
                        }



                        composable("users") {
                            val viewModel = UsersViewModel(userRepository)
                            UsersScreen(
                                viewModel = viewModel,
                                onUserSelected = { conversationId ->
                                    navController.navigate("conversation/$conversationId")
                                }
                            )
                        }

                        composable("messages") {
                            val viewModel = ChatViewModel(messageRepository, userRepository)
                            ChatsScreen(
                                viewModel = viewModel,
                                onChatSelected = { conversationId, userId, chatName ->
                                    navController.navigate("conversation/$conversationId/$userId/$chatName")
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // New route for user's bought items
                        composable("purchases") {
                            val viewModel = ShopViewModel(shopRepository, membershipRepository, fitnessCenterRepository)
                            PurchasesScreen(
                                viewModel = viewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = "conversation/{conversationId}/{userId}/{chatName}",
                            arguments = listOf(
                                navArgument("conversationId") { type = NavType.IntType },
                                navArgument("userId") { type = NavType.IntType },
                                navArgument("chatName") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->

                            val conversationId = backStackEntry.arguments?.getInt("conversationId") ?: return@composable
                            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
                            val chatName = backStackEntry.arguments?.getString("chatName") ?: return@composable

                            val viewModel = ChatViewModel(messageRepository, userRepository)

                            ChatScreen(
                                viewModel = viewModel,
                                authRepository = authRepository,
                                conversationId = conversationId,
                                userId = userId,
                                title = chatName,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }



                        composable(
                            route = "fitnessCenter/{fitnessCenterId}",
                            arguments = listOf(
                                navArgument("fitnessCenterId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val fitnessCenterId = backStackEntry.arguments?.getInt("fitnessCenterId") ?: return@composable
                            val viewModel = FitnessCenterViewModel(fitnessCenterRepository, membershipRepository, attendanceRepository, userRepository, cacheRepository)

                            FitnessCenterScreen(
                                viewModel = viewModel,
                                fitnessCenterId = fitnessCenterId,
                                authRepository = authRepository,
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onChatClicked = {  conversationId, userId, chatName ->
                                    navController.navigate("conversation/$conversationId/$userId/$chatName") },

                                onUserChatSelect = {
                                    navController.navigate("messages")

                                },
                                onUserItemsSelect = {
                                    navController.navigate("purchases")
                                }
                            )
                        }

                        composable(
                            route = "fitnessCenter/{fitnessCenterId}",
                            arguments = listOf(
                                navArgument("fitnessCenterId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val fitnessCenterId = backStackEntry.arguments?.getInt("fitnessCenterId") ?: return@composable

                            FitnessCenterWithBottomNav(
                                fitnessCenterId = fitnessCenterId,
                                // Pass all the repositories you need
                                fitnessCenterRepository = fitnessCenterRepository,
                                membershipRepository = membershipRepository,
                                attendanceRepository = attendanceRepository,
                                userRepository = userRepository,
                                authRepository = authRepository,
                                shopRepository = shopRepository,
                                cacheRepository = cacheRepository,
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onChatClicked = {  conversationId, userId, chatName ->
                                    navController.navigate("conversation/$conversationId/$userId/$chatName") },
                                onUserChatSelect = {
                                    navController.navigate("messages")

                                },
                                onUserItemsSelect = {
                                    navController.navigate("purchases")
                                }

                            )
                        }


                    }
                }
            }
        }
    }
}


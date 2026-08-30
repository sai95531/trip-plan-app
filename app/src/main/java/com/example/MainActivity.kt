package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.ExpenseEntity
import com.example.data.model.TripEntity
import com.example.ui.screens.AddExpenseScreen
import com.example.ui.screens.AddTripBottomSheet
import com.example.ui.screens.DestinationExploreScreen
import com.example.ui.screens.SpendingSummaryScreen
import com.example.ui.screens.TravelTranslatorScreen
import com.example.ui.screens.TripDashboardScreen
import com.example.ui.screens.TripPlannerScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.DestinationExploreViewModel
import com.example.ui.viewmodel.TravelTranslatorViewModel
import com.example.ui.viewmodel.TripPlannerViewModel
import com.example.ui.viewmodel.TripViewModel

enum class MainTab {
    TRACKER,
    PLANNER,
    EXPLORE,
    TRANSLATE
}

sealed interface Screen {
    data class Main(val tab: MainTab = MainTab.TRACKER) : Screen
    data class AddExpense(val expenseToEdit: ExpenseEntity? = null) : Screen
    data class SpendingSummary(val trip: TripEntity) : Screen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TripPlannerAndTrackerApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripPlannerAndTrackerApp(
    tripViewModel: TripViewModel = viewModel(),
    plannerViewModel: TripPlannerViewModel = viewModel(),
    exploreViewModel: DestinationExploreViewModel = viewModel(),
    translatorViewModel: TravelTranslatorViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Main(MainTab.TRACKER)) }
    var currentTab by remember { mutableStateOf(MainTab.TRACKER) }
    var showAddTripSheet by remember { mutableStateOf(false) }
    var tripToEdit by remember { mutableStateOf<TripEntity?>(null) }

    val selectedTrip by tripViewModel.selectedTrip.collectAsStateWithLifecycle()

    BackHandler(enabled = currentScreen !is Screen.Main || currentTab != MainTab.TRACKER) {
        if (currentScreen !is Screen.Main) {
            currentScreen = Screen.Main(currentTab)
        } else if (currentTab != MainTab.TRACKER) {
            currentTab = MainTab.TRACKER
            currentScreen = Screen.Main(MainTab.TRACKER)
        }
    }

    Scaffold(
        bottomBar = {
            if (currentScreen is Screen.Main) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentTab == MainTab.TRACKER,
                        onClick = {
                            currentTab = MainTab.TRACKER
                            currentScreen = Screen.Main(MainTab.TRACKER)
                        },
                        icon = { Icon(Icons.Default.Payments, contentDescription = "Tracker") },
                        label = { Text("Tracker", fontWeight = if (currentTab == MainTab.TRACKER) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("nav_tracker")
                    )

                    NavigationBarItem(
                        selected = currentTab == MainTab.PLANNER,
                        onClick = {
                            currentTab = MainTab.PLANNER
                            currentScreen = Screen.Main(MainTab.PLANNER)
                        },
                        icon = { Icon(Icons.Default.DateRange, contentDescription = "Planner") },
                        label = { Text("Planner", fontWeight = if (currentTab == MainTab.PLANNER) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("nav_planner")
                    )

                    NavigationBarItem(
                        selected = currentTab == MainTab.EXPLORE,
                        onClick = {
                            currentTab = MainTab.EXPLORE
                            currentScreen = Screen.Main(MainTab.EXPLORE)
                        },
                        icon = { Icon(Icons.Default.Explore, contentDescription = "Explore") },
                        label = { Text("Explore", fontWeight = if (currentTab == MainTab.EXPLORE) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("nav_explore")
                    )

                    NavigationBarItem(
                        selected = currentTab == MainTab.TRANSLATE,
                        onClick = {
                            currentTab = MainTab.TRANSLATE
                            currentScreen = Screen.Main(MainTab.TRANSLATE)
                        },
                        icon = { Icon(Icons.Default.Translate, contentDescription = "Translate") },
                        label = { Text("Translate", fontWeight = if (currentTab == MainTab.TRANSLATE) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("nav_translate")
                    )
                }
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "main_screen_transition",
            modifier = Modifier.padding(innerPadding)
        ) { screen ->
            when (screen) {
                is Screen.Main -> {
                    when (currentTab) {
                        MainTab.TRACKER -> {
                            TripDashboardScreen(
                                viewModel = tripViewModel,
                                onNavigateToAddExpense = {
                                    currentScreen = Screen.AddExpense()
                                },
                                onNavigateToEditExpense = { expense ->
                                    currentScreen = Screen.AddExpense(expenseToEdit = expense)
                                },
                                onNavigateToSpendingSummary = { trip ->
                                    currentScreen = Screen.SpendingSummary(trip)
                                },
                                onOpenAddTrip = {
                                    tripToEdit = null
                                    showAddTripSheet = true
                                },
                                onOpenEditTrip = { trip ->
                                    tripToEdit = trip
                                    showAddTripSheet = true
                                }
                            )
                        }

                        MainTab.PLANNER -> {
                            TripPlannerScreen(
                                viewModel = plannerViewModel,
                                onSaveTripToTracker = { newTrip ->
                                    tripViewModel.createTrip(
                                        name = newTrip.name,
                                        destination = newTrip.destination,
                                        budget = newTrip.budget,
                                        startDate = newTrip.startDate,
                                        endDate = newTrip.endDate,
                                        currencySymbol = newTrip.currencySymbol,
                                        currencyCode = newTrip.currencyCode,
                                        tripType = newTrip.tripType,
                                        colorHex = newTrip.colorHex
                                    )
                                    currentTab = MainTab.TRACKER
                                    currentScreen = Screen.Main(MainTab.TRACKER)
                                }
                            )
                        }

                        MainTab.EXPLORE -> {
                            DestinationExploreScreen(
                                viewModel = exploreViewModel,
                                onAddExpenseShortcut = { title, amount, category ->
                                    selectedTrip?.let { trip ->
                                        tripViewModel.addExpense(
                                            tripId = trip.id,
                                            title = title,
                                            amount = amount,
                                            category = category,
                                            notes = "Logged from Explore Guide"
                                        )
                                        currentTab = MainTab.TRACKER
                                        currentScreen = Screen.Main(MainTab.TRACKER)
                                    }
                                }
                            )
                        }

                        MainTab.TRANSLATE -> {
                            TravelTranslatorScreen(viewModel = translatorViewModel)
                        }
                    }
                }

                is Screen.AddExpense -> {
                    selectedTrip?.let { trip ->
                        AddExpenseScreen(
                            trip = trip,
                            viewModel = tripViewModel,
                            expenseToEdit = screen.expenseToEdit,
                            onNavigateBack = {
                                currentScreen = Screen.Main(currentTab)
                            }
                        )
                    } ?: run {
                        currentScreen = Screen.Main(currentTab)
                    }
                }

                is Screen.SpendingSummary -> {
                    SpendingSummaryScreen(
                        trip = screen.trip,
                        viewModel = tripViewModel,
                        onNavigateBack = {
                            currentScreen = Screen.Main(currentTab)
                        }
                    )
                }
            }
        }
    }

    // Modal Sheet for Adding or Editing Trip
    if (showAddTripSheet) {
        AddTripBottomSheet(
            tripToEdit = tripToEdit,
            onSave = { name, destination, budget, start, end, currencySymbol, currencyCode, tripType, colorHex ->
                if (tripToEdit != null) {
                    tripViewModel.updateTrip(
                        tripToEdit!!.copy(
                            name = name,
                            destination = destination,
                            budget = budget,
                            startDate = start,
                            endDate = end,
                            currencySymbol = currencySymbol,
                            currencyCode = currencyCode,
                            tripType = tripType,
                            colorHex = colorHex
                        )
                    )
                } else {
                    tripViewModel.createTrip(
                        name = name,
                        destination = destination,
                        budget = budget,
                        startDate = start,
                        endDate = end,
                        currencySymbol = currencySymbol,
                        currencyCode = currencyCode,
                        tripType = tripType,
                        colorHex = colorHex
                    )
                }
                showAddTripSheet = false
                tripToEdit = null
            },
            onDismiss = {
                showAddTripSheet = false
                tripToEdit = null
            }
        )
    }
}

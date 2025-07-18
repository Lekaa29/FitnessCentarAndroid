package com.example.fitnesscentarchat.ui.screens.hub

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.example.fitnesscentarchat.ui.screens.hub.components.HubContent
import com.example.fitnesscentarchat.ui.screens.map.MapScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubScreen(
    viewModel: HubViewModel,
    onFitnessCenterSelected: (Int) -> Unit,
    onUserChatSelect: () -> Unit,
    onUserItemsSelect: () -> Unit

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isMapMode by remember { mutableStateOf(false) }
    val recentSearches = remember { mutableStateListOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }

    var selectedGymForMap by remember { mutableStateOf<FitnessCenter?>(null) }


    var searchText by remember { mutableStateOf("") }

    if(isMapMode) {
        MapScreen(onBackClick = {
            isMapMode = false
            selectedGymForMap = null
        },
            selectedGymForMap,
            uiState,
            onFitnessCenterSelected
        )
    } else {
        HubContent(
            onFitnessCenterSelected,
            uiState,
            onMapClick = { isMapMode = true },
            isSearchExpanded = isSearchExpanded,
            searchText = searchText,
            recentSearches = recentSearches,
            onSearchExpandedChange = { isSearchExpanded = it },
            onSearchTextChange = { searchText = it },
            onGymLocationClick = { gym ->
                selectedGymForMap = gym
                isMapMode = true
            },
            onUserChatSelect=onUserChatSelect,
            onUserItemsSelect=onUserItemsSelect
        )

    }




}


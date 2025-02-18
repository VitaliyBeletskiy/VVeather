package vibe.weather.ui.screens.locations

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vibe.weather.R
import vibe.weather.data.model.Location
import vibe.weather.utils.openMapsApp
import vibe.weather.utils.showToast

@Composable
fun LocationsScreen(
    viewModel: LocationsViewModel,
    navigateBack: () -> Unit,
    onLocationChosen: (Location) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.messageResId?.let { resId ->
        Toast.makeText(LocalContext.current, stringResource(resId), Toast.LENGTH_SHORT).show()
        viewModel.onMessageShown()
    }

    Scaffold(
        topBar = {
            LocationsTopAppBar(
                navigateBack = navigateBack,
                modifier = modifier.fillMaxWidth(),
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            SearchPanel(
                onSearchPanelFocusChanged = { isSearchActive = it },
                onSearch = { viewModel.searchLocation(it) },
            )
            if (isSearchActive) {
                FoundLocationsContent(
                    locations = uiState.locations,
                    isLoading = uiState.isLoading,
                    onLocationClicked = onLocationChosen,
                )
            } else {
                ExistingLocationsContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationsTopAppBar(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.locations),
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun SearchPanel(
    onSearchPanelFocusChanged: (Boolean) -> Unit = {},
    onSearch: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val searchQueryIsNotBlank = remember(searchQuery.value) {
        searchQuery.value.isNotBlank()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isCancelButtonShown = rememberSaveable { mutableStateOf(false) }
    val focusManager: FocusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
            },
            placeholder = { Text(text = stringResource(R.string.find_location)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                )
            },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions {
                if (!searchQueryIsNotBlank) return@KeyboardActions
                onSearch(searchQuery.value.trim())
                searchQuery.value = ""
                keyboardController?.hide()
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Blue,
                cursorColor = Color.Black,
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged {
                    isCancelButtonShown.value = it.isFocused
                    onSearchPanelFocusChanged(it.isFocused)
                },
        )
        if (isCancelButtonShown.value) {
            Button(
                onClick = {
                    searchQuery.value = ""
                    focusManager.clearFocus()
                },
                modifier = Modifier.wrapContentWidth(),
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    }
}

@Composable
private fun FoundLocationsContent(
    locations: List<Location>,
    isLoading: Boolean,
    onLocationClicked: (Location) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(5.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.wrapContentSize(),
            )
            return@Surface
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(count = locations.size) { idx ->
                LocationItem(location = locations[idx], onLocationClicked = onLocationClicked)
            }
        }
    }
}

@Composable
private fun LocationItem(
    location: Location,
    onLocationClicked: (Location) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val noMapAppFoundString = stringResource(R.string.no_map_app_found)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onLocationClicked(location) },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f),
            ) {
                Text(text = location.name)
                Text(text = location.region)
            }
            IconButton(
                onClick = {
                    openMapsApp(context, location.lat, location.lon) {
                        showToast(context, noMapAppFoundString)
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Show on map",
                )
            }
        }
    }
}

@Composable
private fun ExistingLocationsContent() {
    Text(text = "Placeholder Existing locations")
}

@Preview
@Composable
private fun LocationsScreenPreview() {
    LocationsScreen(
        viewModel = hiltViewModel<LocationsViewModel>(),
        navigateBack = {},
    )
}

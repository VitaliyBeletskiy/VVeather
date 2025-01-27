package vibe.weather.ui.screens.locations

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vibe.weather.R
import vibe.weather.utils.logD

@Composable
fun LocationsScreen(navigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            LocationsTopAppBar(
                navigateBack = navigateBack,
                modifier = Modifier.fillMaxWidth(),
            )
        },
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            SearchField {
                logD("Looking for $it")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsTopAppBar(
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
private fun SearchField(onSearch: (String) -> Unit = {}) {
    val searchQueryState = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(searchQueryState.value) {
        searchQueryState.value.isNotBlank()
    }

    OutlinedTextField(
        value = searchQueryState.value,
        onValueChange = {
            searchQueryState.value = it
        },
        placeholder = { Text(text = stringResource(R.string.find_location)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions {
            if (!valid) return@KeyboardActions
            onSearch(searchQueryState.value.trim())
            searchQueryState.value = ""
            keyboardController?.hide()
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,
            cursorColor = Color.Black,
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
    )
}

@Preview
@Composable
private fun LocationsScreenPreview() {
    LocationsScreen(navigateBack = {})
}

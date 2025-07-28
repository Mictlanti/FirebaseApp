package com.horizon.firebaseapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HeadLineLarge(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 40.sp,
    fontStyle: FontStyle = FontStyle.Italic,
    fontWeight: FontWeight = FontWeight.Bold,
    color: Color = MaterialTheme.colorScheme.onBackground,
    letterSpacing: TextUnit = 3.sp
) {
    Text(
        text,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        color = color,
        modifier = modifier
    )
}

@Composable
fun BodyLarge(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 25.sp,
    fontStyle: FontStyle = FontStyle.Normal,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = MaterialTheme.colorScheme.onBackground,
    letterSpacing: TextUnit = 2.5.sp
) {
    Text(
        text,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        color = color,
        modifier = modifier
    )
}

@Composable
fun BodyMedium(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 20.sp,
    fontStyle: FontStyle = FontStyle.Italic,
    fontWeight: FontWeight = FontWeight.W600,
    letterSpacing: TextUnit = 2.5.sp,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign = TextAlign.Unspecified
) {
    Text(
        text,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        textAlign = textAlign,
        color = color,
        modifier = modifier
    )
}

@Composable
fun BodySmall(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 15.sp,
    fontStyle: FontStyle = FontStyle.Italic,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign = TextAlign.Unspecified,
    letterSpacing: TextUnit = 2.sp
) {
    Text(
        text,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        textAlign = textAlign,
        color = color,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarViewFB(
    title: String,
    navController: NavController,
    actions: @Composable () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            BodyMedium(text = title)
        },
        navigationIcon = {
            NavigationIconTopBar(navController)
        },
        actions = { actions() }
    )
}

@Composable
fun NavigationIconTopBar(navController: NavController) {
    IconButton(
        onClick = { navController.popBackStack() }
    ) {
        Icon(
            Icons.Default.ArrowBackIosNew,
            "Arrow Back",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}
package com.steleot.jetpackcompose.playground.compose.rest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.insets.systemBarsPadding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.steleot.jetpackcompose.playground.LocalIsDarkTheme
import com.steleot.jetpackcompose.playground.LocalUser
import com.steleot.jetpackcompose.playground.R
import com.steleot.jetpackcompose.playground.compose.customexamples.AdViewExample
import com.steleot.jetpackcompose.playground.compose.reusable.*
import com.steleot.jetpackcompose.playground.navigation.MainNavRoutes
import com.steleot.jetpackcompose.playground.utils.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

private val routes = listOf(
    MainNavRoutes.Activity,
    MainNavRoutes.Animation,
    MainNavRoutes.ConstraintLayout,
    MainNavRoutes.CustomExamples,
    MainNavRoutes.ExternalLibraries,
    MainNavRoutes.Foundation,
    MainNavRoutes.Material,
    MainNavRoutes.Material3,
    MainNavRoutes.MaterialIcons,
    MainNavRoutes.MaterialIConsExtended,
    MainNavRoutes.Navigation,
    MainNavRoutes.Paging,
    MainNavRoutes.Runtime,
    MainNavRoutes.Ui,
    MainNavRoutes.ViewModel,
)

private val drawerItems = listOf(
    DrawerListItemData.DividerData,
    DrawerListItemData.MenuData(
        MainNavRoutes.Favorites,
        Icons.Filled.Favorite,
        menuAction = MenuAction.TOAST
    ),
    DrawerListItemData.MenuData(
        MainNavRoutes.Popular,
        Icons.Filled.ThumbUp
    ),
    DrawerListItemData.MenuData(
        MainNavRoutes.Articles,
        Icons.Filled.Article,
        menuAction = MenuAction.TOAST
    ),
    DrawerListItemData.DividerData,
    DrawerListItemData.MenuData(
        "send feedback",
        Icons.Filled.Feedback,
        "Send Feedback",
        MenuAction.FEEDBACK
    ),
    DrawerListItemData.MenuData(
        "privacy policy",
        Icons.Filled.PrivacyTip,
        menuAction = MenuAction.PRIVACY_POLICY
    ),
    DrawerListItemData.DividerData,
    DrawerListItemData.MenuData(
        MainNavRoutes.Settings,
        Icons.Filled.Settings
    ),
    DrawerListItemData.MenuData(
        MainNavRoutes.ReleaseNotes,
        Icons.Filled.NewReleases
    ),
)

private const val PrivacyPolicyUrl = "https://jetpack-compose-play.flycricket.io/privacy.html"

@Composable
fun MainScreenWithDrawer(
    navController: NavHostController,
    firebaseAuth: FirebaseAuth,
    firebaseAnalytics: FirebaseAnalytics,
    googleSignInClient: GoogleSignInClient,
    title: String = stringResource(id = R.string.app_name),
    list: List<String> = routes,
    navigateToSearch: (() -> Unit)? = { navController.navigate(MainNavRoutes.Search) },
    showAd: Boolean = true,
    setUser: (FirebaseUser?) -> Unit
) {
    val state = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var errorDialogText by remember { mutableStateOf("") }
    var showingErrorDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val user = LocalUser.current
    val launcher =
        rememberLauncherForActivityResult(GoogleSignContract(googleSignInClient)) { idToken ->
            scope.launch {
                try {
                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                    val result = firebaseAuth.signInWithCredential(credential).await()
                    Timber.d("Successful login.")
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, Bundle().apply {
                        putString(FirebaseAnalytics.Param.METHOD, "google")
                    })
                    setUser(result.user)
                } catch (e: Exception) {
                    Timber.e(e, "Failed to sign in with Google.")
                    errorDialogText = "Failed to sign in with Google. Please try again later."
                    showingErrorDialog = true
                }
            }
        }

    if (showingErrorDialog) {
        ErrorAlertDialog(errorDialogText) {
            showingErrorDialog = false
        }
    }

    Scaffold(
        scaffoldState = state,
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            MenuTopAppBar(
                title = title,
                openDrawer = {
                    scope.launch {
                        state.drawerState.open()
                    }
                },
                navigateToSearch = navigateToSearch
            )
        },
        drawerContent = {
            DrawerUserItem(
                user = LocalUser.current,
                signInOnClick = {
                    launcher.launch(null)
                },
                signOutOnClick = {
                    firebaseAuth.signOut()
                    setUser(null)
                }
            )
            drawerItems.forEach {
                when (it) {
                    is DrawerListItemData.DividerData -> Divider()
                    is DrawerListItemData.MenuData -> {
                        DrawerListItem(it.text, icon = {
                            Icon(
                                it.imageVector, contentDescription = "Open ${it.text}"
                            )
                        }) {
                            scope.launch {
                                state.drawerState.close()
                                when (it.menuAction) {
                                    MenuAction.NAVIGATION -> {
                                        if (user == null && it.text == MainNavRoutes.Popular) {
                                            errorDialogText = "You need to sign to open ${it.text}."
                                            showingErrorDialog = true
                                            return@launch
                                        }
                                        navController.navigate(it.text)
                                    }
                                    MenuAction.TOAST -> Toast.makeText(
                                        context,
                                        "Coming soon",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    MenuAction.FEEDBACK -> context.sendFeedback()
                                    MenuAction.PRIVACY_POLICY -> uriHandler.openUri(PrivacyPolicyUrl)
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {
        MainScreenContent(navController, it, list, showAd)
    }
}

@Composable
private fun ErrorAlertDialog(
    text: String,
    closeAction: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        text = {
            Text(text = text)
        },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = android.R.string.ok),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable(onClick = closeAction)
                )
            }
        },
    )
}

@Composable
private fun DrawerUserItem(
    user: FirebaseUser?,
    signInOnClick: () -> Unit,
    signOutOnClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(100.dp)
            .padding(16.dp)
    ) {
        if (user != null) {
            SignedInUser(user, signOutOnClick)
        } else {
            GoogleSignInButton(LocalIsDarkTheme.current, signInOnClick)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun BoxScope.SignedInUser(
    user: FirebaseUser,
    signOutOnClick: () -> Unit,
) {
    val painter = rememberImagePainter(
        data = user.photoUrl,
        builder = {
            transformations(CircleCropTransformation())
        }
    )
    Row(
        modifier = Modifier.align(Alignment.Center)
    ) {
        Box {
            Image(
                painter = painter,
                contentDescription = "User photo",
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            when (painter.state) {
                is ImagePainter.State.Loading -> {
                    Box(Modifier.matchParentSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
                is ImagePainter.State.Error -> {
                    Image(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Account default icon"
                    )
                }
                else -> {
                    Timber.d("Else image load states")
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        ) {
            Text(
                user.displayName ?: "Name not found",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
            )
            Text(
                user.email ?: "Email not found",
                style = MaterialTheme.typography.body2,
            )
        }
        TextButton(
            onClick = signOutOnClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colors.onSurface
            ),
            modifier = Modifier
                .align(Alignment.CenterVertically),
            border = BorderStroke(1.dp, MaterialTheme.colors.onSurface)
        ) {
            Text(
                text = "Sign out",
            )
        }
    }
}

@Composable
private fun BoxScope.GoogleSignInButton(
    isDarkTheme: Boolean = true,
    signInOnClick: () -> Unit = { },
) {
    Button(
        onClick = signInOnClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isDarkTheme) Color(0xFF4285F4.toInt()) else Color(
                0xFFFFFFFF.toInt()
            )
        ),
        modifier = Modifier.align(Alignment.Center),
        shape = RoundedCornerShape(1.dp),
        contentPadding = PaddingValues(1.dp)
    ) {
        Row {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_google_icon
                ),
                contentDescription = "Google icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .background(
                        if (isDarkTheme) Color(0xFFFFFFFF.toInt()) else Color.Unspecified,
                        RoundedCornerShape(1.dp)
                    )
                    .padding(8.dp)
            )
            Text(
                text = "Sign in with Google",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp, end = 8.dp)
            )
        }
    }
}

@Composable
fun MainScreen(
    navController: NavHostController,
    title: String = stringResource(id = R.string.app_name),
    list: List<String> = routes,
    navigateToSearch: (() -> Unit)? = { navController.navigate(MainNavRoutes.Search) },
    showAd: Boolean = true,
) {
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            DefaultTopAppBar(
                title = title,
                navigateToSearch = navigateToSearch
            )
        },
    ) {
        MainScreenContent(navController, it, list, showAd)
    }
}

@Composable
fun MainScreenContent(
    navController: NavHostController,
    paddingValues: PaddingValues,
    list: List<String>,
    showAd: Boolean = true,
) {
    val routesWithRibbons = remember {
        list.map { route ->
            route to (route in ribbonRoutes)
        }
    }
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        routesWithRibbons.forEach { (route, shouldShowRibbon) ->
            DefaultCardListItem(
                text = route,
                hasRibbon = shouldShowRibbon
            ) {
                navController.navigate(route)
            }
        }
        if (showAd) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(55.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                AdViewExample(R.string.main_screen_banner_id)
            }
        }
    }
}

sealed class DrawerListItemData {

    class MenuData(
        val text: String,
        val imageVector: ImageVector,
        val contentDescription: String = "Open $text",
        val menuAction: MenuAction = MenuAction.NAVIGATION
    ) : DrawerListItemData()

    object DividerData : DrawerListItemData()
}

enum class MenuAction {
    NAVIGATION, TOAST, FEEDBACK, PRIVACY_POLICY
}

@Composable
private fun DrawerListItem(
    text: String,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    DefaultListItem(
        text = AnnotatedString(text.capitalizeFirstLetter()),
        modifier = Modifier.clickable(onClick = onClick),
        icon = icon
    )
}

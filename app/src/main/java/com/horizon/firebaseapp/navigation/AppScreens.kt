package com.horizon.firebaseapp.navigation

sealed class AppScreens(val route: String) {
    data object LogInScreen : AppScreens("logInView")
    data object HomeScreen : AppScreens("HomeView")
    data object Analytics : AppScreens("AnalyticsView")
    data object ChatRoute : AppScreens("RealtimeView")
    data object AdmobScreen : AppScreens("AdmobView")
    data object CloudScreen : AppScreens("CloudView")
    data object NotesScreen : AppScreens("NotesView")
    data object EditNoteScreen : AppScreens("EditNoteView")
    data object StorageScreen : AppScreens("StorageView")
    data object RemoteConfigScreen : AppScreens("RemoteConfigView")
    data object CloudMessagingScreen : AppScreens("FCMView")
    data object CrashlyticsScreen : AppScreens("CrashlyticsView")
    data object AILogicScreen : AppScreens("AIView")
}

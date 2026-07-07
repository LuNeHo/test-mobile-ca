package com.luneho.testmobileca

import androidx.compose.ui.window.ComposeUIViewController
import com.luneho.testmobileca.di.appModules
import org.koin.compose.KoinApplication

fun MainViewController() = ComposeUIViewController {
    KoinApplication(application = { modules(appModules) }) {
        App()
    }
}
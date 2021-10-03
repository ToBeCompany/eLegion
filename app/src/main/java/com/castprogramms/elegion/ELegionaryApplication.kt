package com.castprogramms.elegion

import android.app.Application
import com.castprogramms.elegion.interactors.StartInteractor
import com.castprogramms.elegion.repository.*
import com.castprogramms.elegion.ui.authentication.AuthenticationViewModel
import com.castprogramms.elegion.ui.calendar.CalendarViewModel
import com.castprogramms.elegion.ui.chats.ChatsViewModel
import com.castprogramms.elegion.ui.checklist.CheckViewModel
import com.castprogramms.elegion.ui.profile.ProfileViewModel
import com.castprogramms.elegion.ui.registration.RegistrationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class ELegionaryApplication : Application() {

    private val appModule = module {
        single { AddressRepository() }
        single { CalendarRepository() }
        single { UserRepository() }
        single { TaskRepository() }
        factory { StartInteractor(get(),get()) }
        viewModel { ChatsViewModel(get()) }
        viewModel { CalendarViewModel(get()) }
        viewModel { AuthenticationViewModel(get(), this@ELegionaryApplication) }
        viewModel { CheckViewModel(get(), get()) }
        viewModel { ProfileViewModel(get()) }
        viewModel { RegistrationViewModel(get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ELegionaryApplication)
            modules(appModule)
        }
    }
}
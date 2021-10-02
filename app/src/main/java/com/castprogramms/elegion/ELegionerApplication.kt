package com.castprogramms.elegion

import android.app.Application
import com.castprogramms.elegion.repository.AddressRepository
import com.castprogramms.elegion.repository.CalendarRepository
import com.castprogramms.elegion.repository.TaskRepository
import com.castprogramms.elegion.repository.UserRepository
import com.castprogramms.elegion.ui.calendar.CalendarViewModel
import com.castprogramms.elegion.ui.chats.ChatsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class ELegionerApplication : Application() {

    val appModule = module {
        single<AddressRepository> { AddressRepository() }
        single<CalendarRepository> { CalendarRepository() }
        single<UserRepository> { UserRepository() }
        single<TaskRepository> { TaskRepository() }
        viewModel { ChatsViewModel(get()) }
        viewModel { CalendarViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ELegionerApplication)
            modules(appModule)
        }
    }
}
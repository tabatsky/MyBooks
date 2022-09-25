package jatx.mybooks

import android.app.Application
import jatx.mybooks.data.db.AppDatabase
import jatx.mybooks.data.db.repository.BookRepositoryImpl
import jatx.mybooks.domain.repository.BookRepository

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        val appDatabase = AppDatabase.invoke(this)
        BookRepository.INSTANCE = BookRepositoryImpl(appDatabase)
    }
}
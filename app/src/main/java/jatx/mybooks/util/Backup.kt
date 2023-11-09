package jatx.mybooks.util

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import jatx.mybooks.R
import jatx.mybooks.domain.models.Book
import jatx.mybooks.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.PrintWriter
import java.util.Scanner

object Backup {
    lateinit var saveLauncher: ActivityResultLauncher<Array<String>>
    lateinit var loadLauncher: ActivityResultLauncher<Array<String>>

    private val permissions = if (Build.VERSION.SDK_INT > 29) {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    fun tryToSaveBackup() {
        saveLauncher.launch(
            permissions
        )
    }
}

fun AppCompatActivity.onSavePermissionGranted() {
    Log.e("BackupSave", "started")
    lifecycleScope.launch {
        withContext(Dispatchers.IO) {
            Log.e("BackupSave", "launched")
            BookRepository.INSTANCE.getAllBooks().first().let { books ->
                try {
                    val dir = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    dir.mkdirs()
                    val outFile = File(dir, "MyBooks.txt")
                    val pw = PrintWriter(outFile)
                    books.forEach {
                        pw.println(it.backupString)
                    }
                    pw.flush()
                    pw.close()
                    withContext(Dispatchers.Main) {
                        showToast(getString(R.string.toast_save_data_success))
                    }
                    Log.e("BackupSave", "success")
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        showToast(getString(R.string.toast_some_error))
                    }
                }
            }
        }
    }
}

fun AppCompatActivity.onLoadFromUri(uri: Uri, context: Context) {
    Log.e("BackupLoad", "started")
    lifecycleScope.launch {
        withContext(Dispatchers.IO) {
            Log.e("BackupLoad", "launched")
            try {
                val books = arrayListOf<Book>()

                val sc = Scanner(context.contentResolver.openInputStream(uri))
                while (sc.hasNextLine()) {
                    val backupString = sc.nextLine().trim()
                    if (backupString.isEmpty()) continue
                    val book = Book.parseBackupString(backupString)
                    books.add(book)
                }
                sc.close()

                BookRepository.INSTANCE.deleteAllBooks()
                BookRepository.INSTANCE.addBooks(books)

                withContext(Dispatchers.Main) {
                    showToast(getString(R.string.toast_load_data_success))
                }
                Log.e("Backup", "success")
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showToast(getString(R.string.toast_some_error))
                }
            }
        }
    }
}

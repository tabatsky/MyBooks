package jatx.mybooks.util

import android.Manifest
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import jatx.mybooks.R
import jatx.mybooks.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.PrintWriter

object Backup {
    lateinit var saveLauncher: ActivityResultLauncher<Array<String>>

    fun tryToSaveBackup() {
        val permissions = if (Build.VERSION.SDK_INT > 29) {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        saveLauncher.launch(
            permissions
        )
    }
}

fun AppCompatActivity.onSavePermissionGranted() {
    Log.e("Backup", "started")
    lifecycleScope.launch {
        withContext(Dispatchers.IO) {
            Log.e("Backup", "launched")
            BookRepository.INSTANCE.getAllBooks().stateIn(lifecycleScope).value.let { books ->
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
}


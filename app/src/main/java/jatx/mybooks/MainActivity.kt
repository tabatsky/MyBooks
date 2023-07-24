package jatx.mybooks

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import jatx.mybooks.util.Backup.loadLauncher
import jatx.mybooks.util.Backup.saveLauncher
import jatx.mybooks.util.Backup.tryToLoadBackup
import jatx.mybooks.util.onLoadPermissionGranted
import jatx.mybooks.util.onSavePermissionGranted


class MainActivity : AppCompatActivity() {

    init {
        saveLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { map ->
            if (map.all { it.value }) {
                onSavePermissionGranted()
            }
        }

        loadLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { map ->
            if (map.all { it.value }) {
                onLoadPermissionGranted()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestForManageExternalStorage()

        initBookList()
    }

    private fun initBookList() {
        Log.e("MainActivity", "onAppStart")
        if (Build.VERSION.SDK_INT < 33) {
            tryToLoadBackup()
        } else {
            onLoadPermissionGranted()
        }
    }

    private fun requestForManageExternalStorage() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }
}
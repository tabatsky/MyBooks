package jatx.mybooks

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
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
}
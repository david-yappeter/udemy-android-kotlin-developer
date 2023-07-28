package myplayground.example.permissiontest

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import myplayground.example.permissiontest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach { permission ->
                val permissionName = permission.key
                val isGranted = permission.value
                if (isGranted) {
                    when (permissionName) {
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            Toast.makeText(
                                this,
                                "Permission granted for precision location",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        Manifest.permission.ACCESS_COARSE_LOCATION -> {
                            Toast.makeText(
                                this,
                                "Permission granted for coarse location",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Toast.makeText(this, "Permission granted for camera", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val btnCameraPermission = binding.btnCameraPermission

        btnCameraPermission.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
            ) {
                showRationaleDialog(
                    "Permission Demo requires camera access",
                    "Camera cannot be used because Camera access is denied",
                )
            } else {
                cameraAndLocationResultLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    )
                )
            }
        }
    }

    private fun showRationaleDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}
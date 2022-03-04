package page.smirnov.wallester.core.util.extension

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.allPermissionsGranted(permissions: Array<String>): Boolean {
    return permissions.all(::isPermissionGranted)
}

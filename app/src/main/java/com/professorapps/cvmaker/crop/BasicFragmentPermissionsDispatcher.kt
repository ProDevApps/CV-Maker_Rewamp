package com.professorapps.cvmaker.crop

import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.PermissionUtils
import java.lang.ref.WeakReference

internal object BasicFragmentPermissionsDispatcher {
    private const val REQUEST_CROPIMAGE = 1
    private const val REQUEST_PICKIMAGE = 0
    private val PERMISSION_PICKIMAGE = arrayOf("android.permission.READ_EXTERNAL_STORAGE")
    private val PERMISSION_CROPIMAGE = arrayOf("android.permission.WRITE_EXTERNAL_STORAGE")

    fun pickImageWithCheck(basicFragment: BasicFragment) {
        if (PermissionUtils.hasSelfPermissions(basicFragment.activity, *PERMISSION_PICKIMAGE)) {
            basicFragment.pickImage()
        } else if (PermissionUtils.shouldShowRequestPermissionRationale(
                basicFragment,
                *PERMISSION_PICKIMAGE
            )
        ) {
            basicFragment.showRationaleForPick(PickImagePermissionRequest(basicFragment))
        } else {
            basicFragment.requestPermissions(PERMISSION_PICKIMAGE, 0)
        }
    }

    fun cropImageWithCheck(basicFragment: BasicFragment) {
        if (PermissionUtils.hasSelfPermissions(basicFragment.activity, *PERMISSION_CROPIMAGE)) {
            basicFragment.cropImage()
        } else if (PermissionUtils.shouldShowRequestPermissionRationale(
                basicFragment,
                *PERMISSION_CROPIMAGE
            )
        ) {
            basicFragment.showRationaleForCrop(CropImagePermissionRequest(basicFragment))
        } else {
            basicFragment.requestPermissions(PERMISSION_CROPIMAGE, 1)
        }
    }

    fun onRequestPermissionsResult(basicFragment: BasicFragment, i: Int, iArr: IntArray) {
        if (i != 0) {
            if (i == 1 && PermissionUtils.verifyPermissions(*iArr)) {
                basicFragment.cropImage()
            }
        } else if (PermissionUtils.verifyPermissions(*iArr)) {
            basicFragment.pickImage()
        }
    }


    private class PickImagePermissionRequest(basicFragment: BasicFragment) : PermissionRequest {
        private val weakTarget = WeakReference(basicFragment)

        override fun cancel() {
        }

        override fun proceed() {
            val basicFragment = weakTarget.get()
            basicFragment?.requestPermissions(PERMISSION_PICKIMAGE, 0)
        }
    }


    private class CropImagePermissionRequest(basicFragment: BasicFragment) : PermissionRequest {
        private val weakTarget = WeakReference(basicFragment)

        override fun cancel() {
        }

        override fun proceed() {
            val basicFragment = weakTarget.get()
            basicFragment?.requestPermissions(PERMISSION_CROPIMAGE, 1)
        }
    }
}

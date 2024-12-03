package com.professorapps.cvmaker.crop

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.professorapps.cvmaker.AboutmeActivity
import com.professorapps.cvmaker.R
import com.professorapps.cvmaker.crop_lib.CropCallback
import com.professorapps.cvmaker.crop_lib.CropImageView
import com.professorapps.cvmaker.crop_lib.LoadCallback
import com.professorapps.cvmaker.crop_lib.Logger.i
import com.professorapps.cvmaker.crop_lib.SaveCallback
import com.professorapps.cvmaker.crop_lib.Utils.ensureUriPermission
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

@RuntimePermissions
class BasicFragment : Fragment() {
    var bmp: Bitmap? = null
    var imgback: ImageView? = null
    var imgcrop2: ImageView? = null
    var imgcrop_done: ImageView? = null
    private var mCropView: CropImageView? = null
    lateinit var rootview: View
    private val mCompressFormat = CompressFormat.JPEG
    private var mFrameRect: RectF? = null
    private var mSourceUri: Uri? = null
    private val btnListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.button16_9 -> {
                mCropView!!.setCropMode(CropImageView.CropMode.RATIO_16_9)
                return@OnClickListener
            }

            R.id.button1_1 -> {
                mCropView!!.setCropMode(CropImageView.CropMode.SQUARE)
                return@OnClickListener
            }

            R.id.button3_4 -> {
                mCropView!!.setCropMode(CropImageView.CropMode.RATIO_3_4)
                return@OnClickListener
            }

            R.id.button4_3 -> {
                mCropView!!.setCropMode(CropImageView.CropMode.RATIO_4_3)
                return@OnClickListener
            }

            R.id.button9_16 -> {
                mCropView!!.setCropMode(CropImageView.CropMode.RATIO_9_16)
                return@OnClickListener
            }

            R.id.buttonCircle -> {
                mCropView!!.setCropMode(CropImageView.CropMode.CIRCLE)
                return@OnClickListener
            }

            R.id.buttonCustom -> {
                mCropView!!.setCustomRatio(7, 5)
                return@OnClickListener
            }

            R.id.buttonDone, R.id.buttonPickImage -> return@OnClickListener
            R.id.buttonFitImage -> {
                mCropView!!.setCropMode(CropImageView.CropMode.FIT_IMAGE)
                return@OnClickListener
            }

            R.id.buttonFree -> {
                mCropView!!.setCropMode(CropImageView.CropMode.FREE)
                return@OnClickListener
            }

            R.id.buttonRotateLeft -> {
                mCropView!!.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D)
                return@OnClickListener
            }

            R.id.buttonRotateRight -> {
                mCropView!!.rotateImage(CropImageView.RotateDegrees.ROTATE_90D)
                return@OnClickListener
            }

            R.id.buttonShowCircleButCropAsSquare -> {
                mCropView!!.setCropMode(CropImageView.CropMode.CIRCLE_SQUARE)
                return@OnClickListener
            }

            else -> return@OnClickListener
        }
    }
    private val mLoadCallback: LoadCallback = object : LoadCallback {
        override fun onError(th: Throwable?) {
        }

        override fun onSuccess() {
        }
    }
    private val mCropCallback: CropCallback = object : CropCallback {
        override fun onError(th: Throwable?) {
        }

        override fun onSuccess(bitmap: Bitmap?) {
        }
    }
    private val mSaveCallback: SaveCallback = object : SaveCallback {
        override fun onSuccess(uri: Uri?) {
            this@BasicFragment.dismissProgress()
            (this@BasicFragment.activity as BasicActivity?)!!.startResultActivity(uri)
        }

        override fun onError(th: Throwable?) {
            this@BasicFragment.dismissProgress()
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        retainInstance = true
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        this.rootview = layoutInflater.inflate(R.layout.fragment_basic, null as ViewGroup?, false)
        return this.view
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        bindViews(view)
        this.imgback = view.findViewById<View>(R.id.imgback) as ImageView
        this.imgcrop_done = view.findViewById<View>(R.id.imgcrop_done) as ImageView
        this.imgcrop2 = view.findViewById<View>(R.id.imgcrop2) as ImageView
        mCropView!!.setDebug(true)
        if (bundle != null) {
            this.mFrameRect = bundle.getParcelable<Parcelable>(KEY_FRAME_RECT) as RectF?
            this.mSourceUri = bundle.getParcelable<Parcelable>(KEY_SOURCE_URI) as Uri?
        }
        if (this.mSourceUri == null) {
            this.mSourceUri = Uri.parse(requireActivity().intent.getStringExtra("imageUri"))
            imgcrop2!!.setImageURI(this.mSourceUri)
            imgcrop2!!.isDrawingCacheEnabled = true
            this.bmp = (imgcrop2!!.drawable as BitmapDrawable).bitmap
            this.mSourceUri = getImageUri(activity, this.bmp)
            mCropView!!.load(this.mSourceUri).initialFrameRect(this.mFrameRect).useThumbnail(false)
                .execute(
                    this.mLoadCallback
                )
            Log.e("aoki", "mSourceUri = " + this.mSourceUri)
        }
        imgback!!.setOnClickListener { this@BasicFragment.requireActivity().finish() }
        imgcrop_done!!.setOnClickListener {
            if (this@BasicFragment.mSourceUri != null) {
                mCropView!!.crop(this@BasicFragment.mSourceUri).execute(object : CropCallback {
                    override fun onError(th: Throwable?) {
                    }

                    override fun onSuccess(bitmap: Bitmap?) {
                        val imageUri =
                            this@BasicFragment.getImageUri(this@BasicFragment.activity, bitmap)
                        val intent = Intent()
                        intent.putExtra("imageUri2", imageUri.toString())
                        intent.putExtra(AboutmeActivity.EXTRA_PROFILE_ID, imageUri.toString())
                        this@BasicFragment.requireActivity().setResult(-1, intent)
                        this@BasicFragment.requireActivity().finish()
                    }
                })
            }
        }
    }


    fun getImageUri(context: Context?, bitmap: Bitmap?): Uri {
        val externalStorageDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(externalStorageDirectory.absolutePath + "/.temp/")
        file.mkdir()
        var file2: File? = null
        try {
            file2 = File.createTempFile("baby_pic", ".jpg", file)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap!!.compress(CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val fileOutputStream = FileOutputStream(file2)
            fileOutputStream.write(byteArray)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(file2)
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putParcelable(KEY_FRAME_RECT, mCropView!!.actualCropRect)
        bundle.putParcelable(KEY_SOURCE_URI, mCropView!!.sourceUri)
    }

    override fun onActivityResult(i: Int, i2: Int, intent: Intent?) {
        super.onActivityResult(i, i2, intent)
        if (i2 == -1) {
            this.mFrameRect = null
            if (i == REQUEST_PICK_IMAGE) {
                this.mSourceUri = intent!!.data
                mCropView!!.load(this.mSourceUri).initialFrameRect(this.mFrameRect)
                    .useThumbnail(true).execute(
                    this.mLoadCallback
                )
            } else if (i == REQUEST_SAF_PICK_IMAGE) {
                this.mSourceUri = ensureUriPermission(
                   requireContext(), intent!!
                )
                mCropView!!.load(this.mSourceUri).initialFrameRect(this.mFrameRect)
                    .useThumbnail(true).execute(
                    this.mLoadCallback
                )
            }
        }
    }

    override fun onRequestPermissionsResult(i: Int, strArr: Array<String>, iArr: IntArray) {
        super.onRequestPermissionsResult(i, strArr, iArr)
    }

    private fun bindViews(view: View) {
        this.mCropView = view.findViewById<View>(R.id.cropImageView) as CropImageView
        view.findViewById<View>(R.id.buttonDone).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.buttonFitImage).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.button1_1).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.button3_4).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.button4_3).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.button9_16).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.button16_9).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.buttonFree).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.buttonPickImage).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.buttonRotateLeft).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.buttonRotateRight).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.buttonCustom).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.buttonCircle).setOnClickListener(this.btnListener)
        view.findViewById<View>(R.id.buttonShowCircleButCropAsSquare).setOnClickListener(
            this.btnListener
        )
    }

    @NeedsPermission("android.permission.READ_EXTERNAL_STORAGE")
    fun pickImage() {
        if (Build.VERSION.SDK_INT < 19) {
            startActivityForResult(
                Intent("android.intent.action.GET_CONTENT").setType("image/*"),
                REQUEST_PICK_IMAGE
            )
            return
        }
        val intent = Intent("android.intent.action.OPEN_DOCUMENT")
        intent.addCategory("android.intent.category.OPENABLE")
        intent.setType("image/*")
        startActivityForResult(intent, REQUEST_SAF_PICK_IMAGE)
    }

    @NeedsPermission("android.permission.WRITE_EXTERNAL_STORAGE")
    fun cropImage() {
        showProgress()
        mCropView!!.crop(this.mSourceUri).execute(this.mCropCallback)
    }

    @OnShowRationale("android.permission.READ_EXTERNAL_STORAGE")
    fun showRationaleForPick(permissionRequest: PermissionRequest) {
        showRationaleDialog(R.string.permission_pick_rationale, permissionRequest)
    }

    @OnShowRationale("android.permission.WRITE_EXTERNAL_STORAGE")
    fun showRationaleForCrop(permissionRequest: PermissionRequest) {
        showRationaleDialog(R.string.permission_crop_rationale, permissionRequest)
    }

    fun showProgress() {
        requireFragmentManager().beginTransaction()
            .add(ProgressDialogFragment.Companion.instance, PROGRESS_DIALOG)
            .commitAllowingStateLoss()
    }




    fun dismissProgress() {
        val fragmentManager = fragmentManager
        if (isResumed && fragmentManager != null) {
            val progressDialogFragment = fragmentManager.findFragmentByTag(PROGRESS_DIALOG) as? ProgressDialogFragment
            if (progressDialogFragment != null) {
                fragmentManager.beginTransaction()
                    .remove(progressDialogFragment)
                    .commitAllowingStateLoss()
            }
        }
    }


    fun createSaveUri(): Uri? {
        return createNewUri(context, this.mCompressFormat)
    }

    object AnonymousClass9 {
        val `$SwitchMap$android$graphics$Bitmap$CompressFormat`: IntArray =
            IntArray(CompressFormat.entries.toTypedArray().size)

        init {
            try {
                `$SwitchMap$android$graphics$Bitmap$CompressFormat`[CompressFormat.JPEG.ordinal] = 1
            } catch (unused: NoSuchFieldError) {
            }
            try {
                `$SwitchMap$android$graphics$Bitmap$CompressFormat`[CompressFormat.PNG.ordinal] = 2
            } catch (unused2: NoSuchFieldError) {
            }
        }
    }

    private fun showRationaleDialog(@StringRes i: Int, permissionRequest: PermissionRequest) {
        AlertDialog.Builder(requireContext())
            .setPositiveButton(R.string.button_allow) { dialogInterface, i2 -> permissionRequest.proceed() }
            .setNegativeButton(R.string.button_deny) { dialogInterface, i2 -> permissionRequest.cancel() }
            .setCancelable(false).setMessage(i).show()
    }

    companion object {
        private const val KEY_FRAME_RECT = "FrameRect"
        private const val KEY_SOURCE_URI = "SourceUri"
        private const val PROGRESS_DIALOG = "ProgressDialog"
        private const val REQUEST_PICK_IMAGE = 10011
        private const val REQUEST_SAF_PICK_IMAGE = 10012
        private const val TAG = "BasicFragment"
        fun newInstance(): BasicFragment {
            val basicFragment = BasicFragment()
            basicFragment.arguments = Bundle()
            return basicFragment
        }

        val dirPath: String
            get() {
                val file: File?
                val externalStorageDirectory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )
                file = if (externalStorageDirectory.canWrite()) {
                    File(externalStorageDirectory.path + "/cropped images")
                } else {
                    null
                }
                if (file != null) {
                    if (!file.exists()) {
                        file.mkdirs()
                    }
                    if (file.canWrite()) {
                        return file.path
                    }
                }
                return ""
            }

        fun getUriFromDrawableResId(context: Context, i: Int): Uri {
            return Uri.parse(
                "android.resource://" + context.resources.getResourcePackageName(i) + "/" + context.resources.getResourceTypeName(
                    i
                ) + "/" + context.resources.getResourceEntryName(i)
            )
        }

        fun createNewUri(context: Context?, compressFormat: CompressFormat): Uri? {
            val currentTimeMillis = System.currentTimeMillis()
            val format = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date(currentTimeMillis))
            val str = "scv" + format + "." + getMimeType(compressFormat)
            val str2 = dirPath + "/" + str
            val file = File(str2)
            val contentValues = ContentValues()
            contentValues.put("title", format)
            contentValues.put("_display_name", str)
            contentValues.put("mime_type", "image/" + getMimeType(compressFormat))
            contentValues.put("_data", str2)
            val j = currentTimeMillis / 1000
            contentValues.put("date_added", j)
            contentValues.put("date_modified", j)
            if (file.exists()) {
                contentValues.put("_size", file.length())
            }
            val insert = context!!.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            i("SaveUri = $insert")
            return insert
        }


        fun getMimeType(compressFormat: CompressFormat): String {
            i("getMimeType CompressFormat = $compressFormat")
            val i =
                AnonymousClass9.`$SwitchMap$android$graphics$Bitmap$CompressFormat`[compressFormat.ordinal]
            if (i == 1) {
                return "jpeg"
            }
            if (i != 2) {
            }
            return "png"
        }

        fun createTempUri(context: Context): Uri {
            return Uri.fromFile(File(context.cacheDir, "cropped"))
        }
    }
}

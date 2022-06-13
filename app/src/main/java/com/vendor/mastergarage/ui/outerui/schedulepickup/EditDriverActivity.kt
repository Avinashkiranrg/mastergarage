package com.vendor.mastergarage.ui.outerui.schedulepickup

import android.Manifest
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import coil.load
import coil.transform.CircleCropTransformation
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.theartofdev.edmodo.cropper.CropImage
import com.vendor.mastergarage.R
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.ActivityEditDriverBinding
import com.vendor.mastergarage.model.DriversItem
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.utlis.DobPickerFragment
import com.vendor.mastergarage.utlis.imageFromUrl
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditDriverActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    lateinit var binding: ActivityEditDriverBinding
    private var myBitmap: Bitmap? = null
    private var myAttachmentBitmap: Bitmap? = null
    private val viewModel: DriverViewModel by viewModels()
    private var driverId: Int? = null
//    private var imageUri: String? = null
//    private var licenceUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.imageView.setOnClickListener {
            requestPermissions()
        }

        val obj = intent.getParcelableExtra<DriversItem>("driversItem")

        if (obj != null) {
            driverId = obj.driverId
            binding.firstName.setText(obj.firstName)
            binding.lastName.setText(obj.lastName)
            binding.licenceNo.setText(obj.licenceNo)
            obj.imageUri?.let { binding.imageView.imageFromUrl(it) }

//            try {
//                imageUri = obj.imageUri
//                obj.imageUri?.let { binding.imageView.imageFromUrl(it) }
//            } catch (r: NullPointerException) {
//
//            }
            try {
//                licenceUri = obj.licenceUri
                if (obj.licenceUri != null) {
                    binding.uploadFile.visibility = View.GONE
                    binding.useCamera.visibility = View.GONE
                    binding.attachFile.visibility = View.VISIBLE
                    binding.attachFile.text = "myLicence"
                }
            } catch (r: NullPointerException) {

            }

            binding.dobField.setText(obj.dob)
            binding.phoneNo.setText(obj.mobileNo)
        }

        binding.attachFile.setOnCloseIconClickListener {
            binding.uploadFile.visibility = View.VISIBLE
            binding.useCamera.visibility = View.VISIBLE
            binding.attachFile.visibility = View.GONE
            myAttachmentBitmap = null
        }



        binding.dobField.setOnClickListener {
            val datePicker: DialogFragment = DobPickerFragment()
            datePicker.show(supportFragmentManager, "date picker")
        }

        binding.submitBtn.setOnClickListener {
            when {
                binding.attachFile.visibility == View.GONE -> {
                    Toast.makeText(this, "upload licence", Toast.LENGTH_SHORT).show()
                }
                binding.firstName.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Enter first name", Toast.LENGTH_SHORT).show()
                }
                binding.lastName.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Enter last name", Toast.LENGTH_SHORT).show()
                }
                binding.phoneNo.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Enter phone number", Toast.LENGTH_SHORT).show()
                }
                binding.phoneNo.text.toString().length < 10 -> {
                    Toast.makeText(this, "You entered a invalid phone number", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.dobField.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Enter date of birth", Toast.LENGTH_SHORT).show()
                }
                binding.licenceNo.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Enter licence number", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.submitBtn.visibility = View.INVISIBLE
                    sendToTheServer()
                }
            }
        }

        viewModel.added.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Response.Loading -> {

                }
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            finish()
                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.submitBtn.visibility = View.VISIBLE
                        }
                    }
                    Toast.makeText(this, vItem?.message, Toast.LENGTH_SHORT)
                        .show()
                    Log.e("Success", "it.errorMessage.toString()")

                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, it.errorMessage.toString())
                    binding.progressBar.visibility = View.GONE
                    binding.submitBtn.visibility = View.VISIBLE
                }
            }
        })

        binding.uploadFile.setOnClickListener {
            galleryCheckPermission()
        }
        binding.useCamera.setOnClickListener {
            cameraCheckPermission()
        }
    }

    private fun cameraCheckPermission() {

        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ).withListener(

                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {

                            if (report.areAllPermissionsGranted()) {
                                camera()
                            }

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRotationalDialogForPermission()
                    }

                }
            ).onSameThread().check()
    }

    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        openCameraLauncher.launch(intent)
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        openGalleryLauncher.launch(intent)
    }

    private val openGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {

                    val selectedImage: Uri? = result.data?.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? =
                        selectedImage?.let {
                            contentResolver.query(
                                it,
                                filePathColumn,
                                null,
                                null,
                                null
                            )
                        }
                    cursor?.moveToFirst()
                    val index: Int = cursor!!.getColumnIndex(filePathColumn[0])
                    val imagePath = cursor.getString(index)
                    cursor.close()
//                    binding.imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath))
                    val bitmap = BitmapFactory.decodeFile(imagePath)
                    Log.e("openGalleryLauncher", bitmap.toString())
                    if (bitmap != null) {
                        myAttachmentBitmap = bitmap
                    }
                    binding.uploadFile.visibility = View.GONE
                    binding.useCamera.visibility = View.GONE
                    binding.attachFile.visibility = View.VISIBLE
                    binding.attachFile.text = "myLicence"
                }
            }
        }

    private val openCameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val bitmap = result.data?.extras?.get("data") as Bitmap

                    Log.e("openCameraLauncher", bitmap.toString())
                    if (bitmap != null) {
                        myAttachmentBitmap = bitmap
                    }
                    binding.uploadFile.visibility = View.GONE
                    binding.useCamera.visibility = View.GONE
                    binding.attachFile.visibility = View.VISIBLE
                    binding.attachFile.text = "myLicence"
                }
            }
        }


    private fun galleryCheckPermission() {

        Dexter.withContext(this).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    this@EditDriverActivity,
                    "You have denied the storage permission to select image",
                    Toast.LENGTH_SHORT
                ).show()
                showRotationalDialogForPermission()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?, p1: PermissionToken?
            ) {
                showRotationalDialogForPermission()
            }
        }).onSameThread().check()
    }


    private fun requestPermissions() {
        // below line is use to request
        // permission in the current activity.
        Dexter.withContext(this) // below line is use to request the number of
            // permissions which are required in our app.
            .withPermissions(
                Manifest.permission.CAMERA,  // below is the list of permissions
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ) // after adding permissions we are
            // calling an with listener method.
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    // this method is called when all permissions are granted
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        // do you work now
                        pickFromGallery();
                    }
                    // check for permanent denial of any permission
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permanently,
                        // we will show user a dialog message.
                        showRotationalDialogForPermission()

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {
                    // this method is called when user grants some
                    // permission and denies some of them.
                    permissionToken.continuePermissionRequest()
                }
            }).withErrorListener { // we are displaying a toast message for error message.
                Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show()
            } // below line is use to run the permissions
            // on same thread and to check the permissions
            .onSameThread().check()
    }

    private fun pickFromGallery() {
        CropImage.activity().start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                binding.imageView.load(resultUri) {
                    crossfade(true)
                    crossfade(1000)
                    transformations(CircleCropTransformation())
                }
                myBitmap = getBitmapFromUri(resultUri, this)

            }
        }
    }

    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
        val parcelFileDescriptor: ParcelFileDescriptor? =
            context.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }

    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage(
                "It looks like you have turned off permissions"
                        + "required for this feature. It can be enable under App settings!!!"
            )

            .setPositiveButton("Go TO SETTINGS") { _, _ ->

                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)

                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }

            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


    private fun sendToTheServer() {
        var imageUri: String? = null
        if (myBitmap != null) {
            imageUri = myBitmap?.let { it1 -> getStringImage(it1) }
        }

        var licenceUri: String? = null
        if (myAttachmentBitmap != null) {
            licenceUri = myAttachmentBitmap?.let { it1 -> getStringImage(it1) }
        }

        driverId?.let {
            viewModel.updateDriver(
                it,
                binding.firstName.text.toString(),
                binding.lastName.text.toString(),
                binding.licenceNo.text.toString(),
                imageUri,
                licenceUri,
                getDateData(binding.dobField.text.toString()),
                binding.phoneNo.text.toString(),
            )
        }

//        if (myBitmap != null) {
//            if (myAttachmentBitmap != null) {
//                driverId?.let {
//                    viewModel.updateDriver(
//                        it,
//                        binding.firstName.text.toString(),
//                        binding.lastName.text.toString(),
//                        binding.licenceNo.text.toString(),
//                        imageUri,
//                        licenceUri,
//                        getDateData(binding.dobField.text.toString()),
//                        binding.phoneNo.text.toString(),
//                    )
//                }
//            } else {
//                driverId?.let {
//                    imageUri?.let { it1 ->
//                        myAttachmentBitmap?.let { it2 -> getStringImage(it2) }?.let { it3 ->
//                            viewModel.updateDriver(
//                                it,
//                                binding.firstName.text.toString(),
//                                binding.lastName.text.toString(),
//                                binding.licenceNo.text.toString(),
//                                it1,
//                                it3,
//                                getDateData(binding.dobField.text.toString()),
//                                binding.phoneNo.text.toString(),
//                            )
//                        }
//                    }
//                }
//            }
//        } else if (myAttachmentBitmap != null) {
//            driverId?.let {
//                myBitmap?.let { it1 -> getStringImage(it1) }?.let { it2 ->
//                    licenceUri?.let { it1 ->
//                        viewModel.updateDriver(
//                            it,
//                            binding.firstName.text.toString(),
//                            binding.lastName.text.toString(),
//                            binding.licenceNo.text.toString(),
//                            it2,
//                            it1,
//                            getDateData(binding.dobField.text.toString()),
//                            binding.phoneNo.text.toString(),
//                        )
//                    }
//                }
//            }
//        } else {
//            driverId?.let {
//                myBitmap?.let { it1 -> getStringImage(it1) }?.let { it2 ->
//                    myAttachmentBitmap?.let { it1 -> getStringImage(it1) }?.let { it3 ->
//                        viewModel.updateDriver(
//                            it,
//                            binding.firstName.text.toString(),
//                            binding.lastName.text.toString(),
//                            binding.licenceNo.text.toString(),
//                            it2,
//                            it3,
//                            getDateData(binding.dobField.text.toString()),
//                            binding.phoneNo.text.toString(),
//                        )
//                    }
//                }
//            }
//        }


    }

    private fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RequestPermissionCode -> if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    this,
                    "Permission Granted , Now your application can access Camera",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Permission Granted , Now your application can not  access Camera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun getDateData(toString: String): String {
        val _myFormat = "dd/MM/yyyy" // mention the format you need
        val _sdf = SimpleDateFormat(_myFormat, Locale.getDefault())
        val date = _sdf.parse(toString)
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

        val cal = Calendar.getInstance()
        try {
            cal.time = date

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return sdf.format(cal.time)
    }


    companion object {
        const val RequestPermissionCode = 111
        private const val TAG = "EditDriverActivity"
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//        dd/MM/yyyy
//        val date: String = "$year-${month + 1}-$dayOfMonth"
        val date: String = "$dayOfMonth/${month + 1}/$year"
        binding.dobField.setText(date)
    }
}
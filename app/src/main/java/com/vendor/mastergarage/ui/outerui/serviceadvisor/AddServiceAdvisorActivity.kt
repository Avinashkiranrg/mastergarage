package com.vendor.mastergarage.ui.outerui.serviceadvisor

import android.Manifest
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.vendor.mastergarage.constraints.Constraints.Companion.TRUE_INT
import com.vendor.mastergarage.databinding.ActivityAddServiceAdvisorBinding
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.model.ServiceAdvisorItem
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddServiceAdvisorActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddServiceAdvisorBinding
    private lateinit var uri: Uri
    private var myBitmap: Bitmap? = null
    private val viewModel: AdvisorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServiceAdvisorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.imageView.setOnClickListener {
//            val pictureDialog = AlertDialog.Builder(this)
//            pictureDialog.setTitle("Select Action")
//            val pictureDialogItem = arrayOf(
//                "Select photo from Gallery",
//                "Capture photo from Camera"
//            )
//            pictureDialog.setItems(pictureDialogItem) { dialog, which ->
//
//                when (which) {
//                    0 -> galleryCheckPermission()
//                    1 -> cameraCheckPermission()
//                }
//            }
//
//            pictureDialog.show()
            requestPermissions()
        }

//        val date =
//            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(System.currentTimeMillis())
//
//        binding.dobHead.setHint(date)

        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd/MM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                binding.dobField.setText(sdf.format(cal.time))
            }

        binding.dobField.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.submitBtn.setOnClickListener {
            when {
                myBitmap == null -> {
                    Toast.makeText(this, "upload image", Toast.LENGTH_SHORT).show()
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
                binding.phoneNo.text.toString().length<10 -> {
                    Toast.makeText(this, "You entered a invalid phone number", Toast.LENGTH_SHORT).show()
                }
                binding.dobField.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Enter date of birth", Toast.LENGTH_SHORT).show()
                }
                binding.designation.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Enter designation", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.submitBtn.visibility = View.INVISIBLE
                    sendToTheServer()
                }
            }
        }

        binding.checkbox1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkbox1.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.blue2
                    )
                )
                binding.checkbox1.buttonTintList =
                    ColorStateList.valueOf(getColor(R.color.blue2))
            } else {
                binding.checkbox1.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )
                binding.checkbox1.buttonTintList =
                    ColorStateList.valueOf(getColor(R.color.black))
                ModelPreferencesManager.put(null, Constraints.ADVISOR_STORE)

            }
        }
        viewModel.added.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Response.Loading -> {

                }
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == TRUE_INT) {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.submitBtn.visibility = View.INVISIBLE

                            if (binding.checkbox1.isChecked) {
                                val advisor = ServiceAdvisorItem(
                                    -1,
                                    vItem.designation,
                                    "2021-02-15",
                                    vItem.first_name,
                                    vItem.imageUri,
                                    vItem.last_name,
                                    vItem.mobile_no,
                                    viewModel.getStoredOutletObject()?.outletId,
                                    "1"
                                )
                                ModelPreferencesManager.put(advisor, Constraints.ADVISOR_STORE)
                            } else {
                                ModelPreferencesManager.put(null, Constraints.ADVISOR_STORE)
                            }

                            finish()
                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.submitBtn.visibility = View.VISIBLE
                        }
                        Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT)
                            .show()
                    }
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

    private fun galleryCheckPermission() {

        Dexter.withContext(this).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    this@AddServiceAdvisorActivity,
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


    private fun sendToTheServer() {

        Log.e("myBitmap", myBitmap.toString())


        if (myBitmap != null) {
            viewModel.getStoredOutletObject()?.outletId?.let {
                viewModel.addServiceAdvisor(
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.designation.text.toString(),
                    getStringImage(myBitmap!!),
                    getDateData(binding.dobField.text.toString()),
                    binding.phoneNo.text.toString(),
                    it
                )
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

    private fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }


    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
//        startActivityForResult(intent, GALLERY_REQUEST_CODE)
        openGalleryLauncher.launch(intent)
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

    private val openCameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val bitmap = result.data?.extras?.get("data") as Bitmap

                    //we are using coroutine image loader (coil)
                    binding.imageView.load(bitmap) {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }
                    Log.e("val bitmap", bitmap.toString())
                    if (bitmap != null) {
                        myBitmap = bitmap
                    }
//                    cropImages(uri)
                    pickFromGallery();
                }
            }
        }


    private val openGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    uri = result.data!!.data!!
                    binding.imageView.load(result.data?.data) {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }

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
                    binding.imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath))
                    val bitmap = BitmapFactory.decodeFile(imagePath)
                    Log.e("bitmap", bitmap.toString())
                    if (bitmap != null) {
                        myBitmap = bitmap
                    }
//                    cropImages(uri)
                    pickFromGallery();
                }
            }
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

//                val selectedImage: Uri? = resultUri
//                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//                val cursor: Cursor? =
//                    selectedImage?.let {
//                        contentResolver.query(
//                            it,
//                            filePathColumn,
//                            null,
//                            null,
//                            null
//                        )
//                    }
//                cursor?.moveToFirst()
//                val index: Int = cursor!!.getColumnIndex(filePathColumn[0])
//                val imagePath = cursor.getString(index)
//                cursor.close()
//                binding.imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath))
//                val bitmap = BitmapFactory.decodeFile(imagePath)

                Log.e("resultUri", resultUri.toString())
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


    companion object {
        const val RequestPermissionCode = 111
        private const val TAG = "AddServiceAdvisorActivity"
    }


}
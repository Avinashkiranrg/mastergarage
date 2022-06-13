package com.vendor.mastergarage.ui.outerui.service

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.SpareAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.ActivityAddServiceBinding
import com.vendor.mastergarage.model.SparesDetails
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class AddServiceActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddServiceBinding

    var year_of_warranty = arrayOf("Year", "1", "2", "3", "4", "5", "6")
    lateinit var yearSpinner: Spinner

    private var spareList: ArrayList<SparesDetails>? = null

    lateinit var spareAdapter: SpareAdapter

    private var leadId: Int? = null
    private var partType: Int? = null

    private var myAttachmentBitmap: Bitmap? = null

    val viewModel: ServiceRViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
        binding.toolbar.setNavigationOnClickListener { finish() }

        try {
            leadId = intent.getIntExtra("leadId", -1)

        } catch (n: NullPointerException) {

        }

        spareList = ArrayList()

        yearSpinner = Spinner(this)

        yearSpinner.adapter =
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                year_of_warranty
            )
        yearSpinner.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            yearSpinner.background.colorFilter =
                BlendModeColorFilter(Color.TRANSPARENT, BlendMode.CLEAR)
        } else {
            yearSpinner.background.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        binding.layout1.addView(yearSpinner)

        binding.year.setOnClickListener {
            yearSpinner.performClick()
        }
        yearSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, arg2: Int, arg3: Long) {
                binding.year.setText(year_of_warranty[arg2])
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        binding.uploadFile.setOnClickListener {
            galleryCheckPermission()
        }
        binding.useCamera.setOnClickListener {
            cameraCheckPermission()
        }

        binding.attachFile.setOnCloseIconClickListener {
            binding.uploadFile.visibility = View.VISIBLE
            binding.useCamera.visibility = View.VISIBLE
            binding.attachFile.visibility = View.GONE
            myAttachmentBitmap = null
        }
        if (spareList != null) {
            spareAdapter = SpareAdapter(this, spareList!!)
            binding.recyclerView.apply {
                setHasFixedSize(true)
                adapter = spareAdapter
                layoutManager =
                    LinearLayoutManager(
                        this@AddServiceActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
            }
        }

        binding.addSpares.setOnClickListener {
            if (binding.manufacturerName.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter manufacturer name", Toast.LENGTH_SHORT)
                    .show()
            } else if (partType == null) {
                Toast.makeText(this, "Please checkbox", Toast.LENGTH_SHORT)
                    .show()
            } else {
                var _year: Int = 0
                _year = try {
                    binding.year.text.toString().toInt()
                } catch (n: NumberFormatException) {
                    0
                }
                var boolean: Boolean = true
                spareList?.forEach { it1 ->
                    if (it1.manufacturer_name == binding.manufacturerName.text.toString().trim() &&
                        it1.part_name == binding.partNumber.text.toString().trim()
                    ) {
                        boolean = false
                    }
                }
                if (boolean) {
                    spareList!!.add(
                        SparesDetails(
                            spareList?.size?.plus(1),
                            manufacturer_name = binding.manufacturerName.text.toString(),
                            part_name = binding.partNumber.text.toString(),
                            warranty = binding.warranty.text.toString(),
                            year = _year,
                            part_type = partType,
                            serviceId = 0,
                        )
                    )
                    binding.manufacturerName.setText("")
                    binding.partNumber.setText("")
                    binding.warranty.setText("")
                    binding.year.setText("0")



                    spareAdapter.addSparesList(spareList!!)
                } else {
                    Toast.makeText(this, "Already exist", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.submit.setOnClickListener {
            if (binding.ServiceName.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter service name", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.serviceCost.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter cost", Toast.LENGTH_SHORT)
                    .show()
            } else if (spareList == null) {
                Toast.makeText(this, "Please enter spare details", Toast.LENGTH_SHORT)
                    .show()
            } else if (myAttachmentBitmap == null) {
                Toast.makeText(this, "Please upload attachment", Toast.LENGTH_SHORT)
                    .show()
            } else {
                var _year: Int = 0
                _year = try {
                    binding.year.text.toString().toInt()
                } catch (n: NumberFormatException) {
                    0
                }
                if (!binding.manufacturerName.text.toString().isNullOrEmpty()) {
                    spareList!!.add(
                        SparesDetails(
                            spareList?.size?.plus(1),
                            manufacturer_name = binding.manufacturerName.text.toString(),
                            part_name = binding.partNumber.text.toString(),
                            warranty = binding.warranty.text.toString(),
                            year = _year,
                            part_type = partType,
                            serviceId = 0,
                        )
                    )
                }

                if (binding.year.text.toString().length > 3) {
                    binding.year.setText("0")
                }

                binding.submit.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.VISIBLE
                val gson = Gson()
                val json_spare_string = gson.toJson(spareList)
                val service_name = binding.ServiceName.text.toString()
                var service_cost: Float = 0F
                var other_charges: Float = 0F
                try {
                    other_charges = binding.otherCharges.text.toString().toFloat()
                } catch (n: NumberFormatException) {
                    other_charges = 0F
                }

                try {
                    service_cost = binding.serviceCost.text.toString().toFloat()
                } catch (n: NumberFormatException) {
                    service_cost = 0F
                }
                val additionalInfo = binding.additionalType.text.toString()
                val attachmentUri: String? = myAttachmentBitmap?.let { it1 -> getStringImage(it1) }
                Log.e("attachmentUri.toString()", attachmentUri.toString())
                Log.e("json_spare_string", json_spare_string.toString())
                viewModel.getStoredOutletObject()?.outletId?.let { it1 ->
                    viewModel.getStoredVendorObject()?.vendorId?.let { it2 ->
                        leadId?.let { it3 ->
                            if (attachmentUri != null) {
                                viewModel.addService(
                                    it3,
                                    it1,
                                    it2,
                                    service_name,
                                    service_cost,
                                    other_charges,
                                    additionalInfo,
                                    attachmentUri,
                                    json_spare_string
                                )
                            }
                        }
                    }
                }
            }
        }

        viewModel.added.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            finish()
                        }
                    }
                }
                is Response.Failure -> {
                    binding.submit.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    Log.e(TAG, it.errorMessage.toString())
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        })

        checkBox3(this)
    }

    private fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
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
                    this@AddServiceActivity,
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
                    binding.attachFile.text = "attachment"
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
                    binding.attachFile.text = "attachment"
                }
            }
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

    private fun checkBox3(context: Context) {
        binding.oemPart.buttonTintList =
            ContextCompat.getColorStateList(context, R.color.blue2)
        binding.oemPart.isChecked = true
        partType = Constraints.OEM_PART

        binding.oesPart.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                partType = Constraints.OES_PART
                binding.oesPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
                binding.oemPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                binding.oemPart.isChecked = false
                binding.otherPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                binding.otherPart.isChecked = false
            } else {
                binding.oesPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
            }
        }

        binding.oemPart.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                partType = Constraints.OEM_PART
                binding.oemPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
                binding.oesPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                binding.oesPart.isChecked = false
                binding.otherPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                binding.otherPart.isChecked = false
            } else {
                binding.oemPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
            }
        }

        binding.otherPart.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                partType = Constraints.OTHER_PART
                binding.otherPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue2)
                binding.oemPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                binding.oemPart.isChecked = false
                binding.oesPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
                binding.oesPart.isChecked = false

            } else {
                binding.oemPart.buttonTintList =
                    ContextCompat.getColorStateList(context, R.color.blue)
            }
        }

    }

    companion object {
        const val RequestPermissionCode = 111
        private const val TAG = "AddServiceActivity"
    }

}
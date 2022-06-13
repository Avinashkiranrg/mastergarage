package com.vendor.mastergarage.ui.outerui.service

import android.content.ActivityNotFoundException
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.Spare2Adapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.ActivityEditRecommServiceBinding
import com.vendor.mastergarage.model.ServiceDetails
import com.vendor.mastergarage.model.SparesDetails
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.notifications.NotificationUi
import com.vendor.mastergarage.utlis.imageFromUrl
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class EditRecommServiceActivity : AppCompatActivity(), Spare2Adapter.OnItemClickListener {
    lateinit var binding: ActivityEditRecommServiceBinding

    private var leadId: Int? = null
    private var myAttachmentBitmap: Bitmap? = null
    var year_of_warranty = arrayOf("Year", "1", "2", "3", "4", "5", "6")
    lateinit var yearSpinner: Spinner

    private var serviceDetails: ServiceDetails? = null

    val viewModel: EditRecomViewModel by viewModels()

    var filterList: ArrayList<ServiceDetails?>? = null

    lateinit var spareAdapter: Spare2Adapter

    private var spareList: ArrayList<SparesDetails>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecommServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
        binding.toolbar.setNavigationOnClickListener { finish() }


        try {
            serviceDetails = intent.getParcelableExtra<ServiceDetails>("serviceDetails")

            binding.ServiceName.setText(serviceDetails?.name)
            binding.serviceCost.setText("${serviceDetails?.cost}")
            binding.otherCharges.setText("${serviceDetails?.other_charges}")
            binding.additionalType.setText(serviceDetails?.additional_details)

            serviceDetails?.attachedUri?.let { binding.attachUri.imageFromUrl(it) }

            if (serviceDetails?.spares?.isEmpty() != true) {
                spareList = serviceDetails?.spares as ArrayList<SparesDetails>
                if (spareList != null) {
                    spareAdapter = Spare2Adapter(this, spareList!!, this)
                    binding.recyclerView.apply {
                        setHasFixedSize(true)
                        adapter = spareAdapter
                        layoutManager =
                            LinearLayoutManager(
                                this@EditRecommServiceActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                }
            }


        } catch (n: NullPointerException) {
        }

        binding.uploadFile.setOnClickListener {
            galleryCheckPermission()
        }
        binding.useCamera.setOnClickListener {
            cameraCheckPermission()
        }

        binding.delete.setOnClickListener {
            serviceDetails?.serviceId?.let { viewModel.deleteService(it) }
        }

        viewModel.delete.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            finish()
                        }
                        Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Response.Failure -> {
                    Log.e(TAG, it.errorMessage.toString())
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.update.setOnClickListener {
            if (binding.ServiceName.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter service name", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.serviceCost.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter cost", Toast.LENGTH_SHORT)
                    .show()
            } else {
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
                var attachmentUri: String? = null
                if (myAttachmentBitmap != null) {
                    attachmentUri = myAttachmentBitmap?.let { it1 -> getStringImage(it1) }
                }
                serviceDetails?.serviceId?.let { it1 ->
                    viewModel.editService(
                        serviceId = it1,
                        service_name = binding.ServiceName.text.toString().trim(),
                        service_cost = service_cost,
                        other_charges = other_charges,
                        additionalInfo = binding.additionalType.text.toString().trim(),
                        attachmentUri = attachmentUri,
                    )
                }
            }
        }
        viewModel.editService.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
//                            finish()
                        }
                        Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Response.Failure -> {
                    Log.e(TAG, it.errorMessage.toString())
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        })

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
                    this@EditRecommServiceActivity,
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
                    binding.attachUri.setImageBitmap(BitmapFactory.decodeFile(imagePath))
                    val bitmap = BitmapFactory.decodeFile(imagePath)
                    Log.e("openGalleryLauncher", bitmap.toString())
                    if (bitmap != null) {
                        myAttachmentBitmap = bitmap
                    }
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

    override fun onDelete(sparesDetails: SparesDetails, position: Int) {
        sparesDetails.spareId?.let { viewModel.deleteRcommServiceSpare(it) }
        viewModel.update.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            spareList?.remove(sparesDetails)
                            spareAdapter.notifyItemRemoved(position)
                            spareList?.let { it1 ->
                                spareAdapter.notifyItemRangeChanged(
                                    position,
                                    it1.size
                                )
                            }
                        }
                        Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Response.Failure -> {
                    Log.e(TAG, it.errorMessage.toString())
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onSave(
        sparesId: Int,
        manufacturer_name: String,
        part_name: String,
        warranty: String,
        year: Int,
        part_type: Int,
        position: Int
    ) {
        Log.e("sparesId","$sparesId,$manufacturer_name,$part_name,$warranty,$year,$part_type");
        viewModel.editServiceSpare(
            sparesId,
            manufacturer_name,
            part_name,
            warranty,
            year,
            part_type
        )
        viewModel.editServiceSpare.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {
                            spareList?.get(position)?.warranty = warranty
                            spareList?.get(position)?.manufacturer_name = manufacturer_name
                            spareList?.get(position)?.part_name = part_name
                            spareList?.get(position)?.year = year
                            spareList?.get(position)?.part_type = part_type
                            spareAdapter.notifyItemChanged(position)
                        }
                        Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Response.Failure -> {
                    Log.e(TAG, it.errorMessage.toString())
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    companion object {
        private const val TAG = "EditRecommServiceActivity"
    }
}
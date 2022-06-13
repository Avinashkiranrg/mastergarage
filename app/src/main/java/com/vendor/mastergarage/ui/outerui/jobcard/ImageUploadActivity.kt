package com.vendor.mastergarage.ui.outerui.jobcard

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.nguyenhoanglam.imagepicker.model.GridCount
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.model.RootDirectory
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import com.vendor.mastergarage.adapters.ImageUploadActivityAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.ActivityImageUploadBinding
import com.vendor.mastergarage.model.ImageModel
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint
import java.io.*


@AndroidEntryPoint
class ImageUploadActivity : AppCompatActivity(), ImageUploadActivityAdapter.OnItemClickListener {
    lateinit var binding: ActivityImageUploadBinding

    lateinit var imageAdapter: ImageUploadActivityAdapter
    private val viewModel: ImageUploadViewModel by viewModels()

    var imageList = ArrayList<ImageModel>()

    private val OPEN_MEDIA_PICKER = 1  // Request code
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =
        100 // Request code for read external storage
    private var leadId: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        try {
            leadId = intent.getIntExtra("leadId", -1)

            viewModel.getJobCardImage(leadId!!)

        } catch (n: NullPointerException) {

        }


        imageList = ArrayList()
        val imageModel = ImageModel(
            imageId = -1,
            imageUri = "",
            status = "",
            leadId = -1,
            position = LAST_POS
        )
        imageList.add(imageModel)

        imageAdapter = ImageUploadActivityAdapter(this, imageList, this)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = imageAdapter
            layoutManager = GridLayoutManager(this@ImageUploadActivity, 2)
        }

        binding.saveImage.setOnClickListener { it ->
            if (imageList.size > 1) {
                val arrayList = ArrayList<ImageModel>()
                imageList.forEach {
                    if (it.position == FIRST_POS) {
                        val bitmap = getBitmapFromUri(Uri.parse(it.imageUri), this)
                        val encodedString = bitmap?.let { it1 -> getStringImage(it1) }
                        val imageModel = ImageModel(
                            imageId = -1,
                            imageUri = encodedString.toString(),
                            status = "",
                            leadId = -1,
                            position = "${leadId.toString()}1"
                        )
                        arrayList.add(imageModel)
                    }
                }


                val gson = Gson()
                val json_image_string = gson.toJson(arrayList)

                Log.e("json_image_string", json_image_string)
                Log.e("leadId", leadId.toString())
                binding.progressBar.visibility = View.VISIBLE
                binding.saveImage.visibility = View.GONE

                leadId?.let { it1 -> viewModel.uploadJobCardImage(it1, json_image_string) }
            } else {
                Toast.makeText(this, "upload image", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.jobCard.observe(this, Observer {
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
                            binding.saveImage.visibility = View.VISIBLE
                        }
                        Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Response.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.saveImage.visibility = View.VISIBLE
                    Log.e("TAG", it.errorMessage.toString())
                }
            }
        })

        viewModel.getImage.observe(this, Observer {
            when (it) {
                is Response.Loading -> {

                }
                is Response.Success -> {
                    val vItem = it.data as MutableList<ImageModel>
                    imageList.addAll(vItem)
                    imageAdapter.setFilter(imageList)
                }
                is Response.Failure -> {
                    Log.e("TAG", it.errorMessage.toString())
                }
            }
        })
    }


    override fun onItemClick(imageModel: ImageModel, max: Int) {
        Log.e("max", max.toString())
        if (imageModel.position == LAST_POS && max > 0) {
            if (!permissionIfNeeded()) {
                val config = ImagePickerConfig(
                    statusBarColor = "#000000",
                    isLightStatusBar = false,
                    isFolderMode = true,
                    isMultipleMode = true,
                    maxSize = max,
                    isShowCamera = true,
                    rootDirectory = RootDirectory.PICTURES,
                    subDirectory = "Photos",
                    folderGridCount = GridCount(2, 4),
                    imageGridCount = GridCount(3, 5),
                    limitMessage = "You could only select up to 10 photos",
                    // See more at configuration attributes table below
                )

                launcher.launch(config)
            }
        } else {
            Toast.makeText(this, "upload quota is exceed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemDeleteClick(imageModel: ImageModel) {
        imageModel.imageId?.let { viewModel.deleteImage(it) }

        viewModel.response.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        if (vItem.success == Constraints.TRUE_INT) {

                        }
                        Toast.makeText(this, vItem.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private val launcher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            images.forEach {
                try {
                    val imageModel = ImageModel(
                        imageId = -1,
                        imageUri = it.uri.toString(),
                        status = "",
                        leadId = -1,
                        position = FIRST_POS
                    )
                    imageList.add(imageModel)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
        imageAdapter.setFilter(imageList)
    }


    private fun permissionIfNeeded(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Toast.makeText(this, "Please give us permission", Toast.LENGTH_SHORT).show()
                }

                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
                return true
            }
        }
        return false
    }


    private fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
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

    companion object {
        const val LAST_POS = "last"
        const val FIRST_POS = "First"
        const val KIDDLE_POS = "kiddle"
    }
}
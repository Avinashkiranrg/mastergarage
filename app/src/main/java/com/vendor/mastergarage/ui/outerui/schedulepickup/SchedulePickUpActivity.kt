package com.vendor.mastergarage.ui.outerui.schedulepickup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vendor.mastergarage.R
import com.vendor.mastergarage.adapters.ChooseServiceAdvisorAdapter
import com.vendor.mastergarage.adapters.DateListAdapter
import com.vendor.mastergarage.adapters.TimeListAdapter
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.ActivitySchedulePickUpBinding
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.model.DriversItem
import com.vendor.mastergarage.model.ServiceAdvisorItem
import com.vendor.mastergarage.networkcall.Response
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SchedulePickUpActivity : AppCompatActivity(), DateListAdapter.OnItemClickListener,
    TimeListAdapter.OnItemClickListener {

    lateinit var binding: ActivitySchedulePickUpBinding

    private lateinit var serviceAdaptor: ChooseServiceAdvisorAdapter

    private val viewModel: SchedulePickUpViewModel by viewModels()

    private var size: Int? = null

    var filterList: ArrayList<ServiceAdvisorItem?>? = null

    private var leadId: Int? = null
    lateinit var dateListAdapter: DateListAdapter
    lateinit var timeListAdapter: TimeListAdapter

    private var driverId: Int? = null
    private var dropType: Int? = null
    private var flag: String? = null
    private var date: String? = null
    private var time: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulePickUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
        binding.toolbar.setNavigationOnClickListener { finish() }

        leadId = intent.getIntExtra("leadId", -1)
        dropType = intent.getIntExtra("dropType", -1)


        try {
            if (dropType == PICK_UP) {
                viewModel.getAssignedDriver(leadId!!)
            } else {
                viewModel.getAssignedDriverDrop(leadId!!)
            }
        } catch (m: NullPointerException) {
            //
        }

        setupDateAdapter()

        setupTimeAdapter(Date())

        binding.cardView.setOnClickListener {
            if (leadId != -1) {
                val intent = Intent(this, PickUpDropDriverActivity::class.java)
                intent.putExtra("leadId", leadId)
//                startActivity(intent)
                intentLauncher.launch(intent)
            }
        }
        binding.change.setOnClickListener {
            if (leadId != -1) {
                val intent = Intent(this, PickUpDropDriverActivity::class.java)
                intent.putExtra("leadId", leadId)
//                startActivity(intent)
                intentLauncher.launch(intent)
            }
        }

        binding.extendedFab.setOnClickListener {
            assignDriver()
        }

        if (dropType == PICK_UP) {
            viewModel.driver.observe(this, androidx.lifecycle.Observer {
                when (it) {
                    is Response.Loading -> {
                        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is Response.Success -> {
                        val driversItem = it.data
                        if (driversItem?.success == Constraints.TRUE_INT) {
                            driverId = driversItem.result?.driverId
                            binding.cardView.visibility = View.GONE
                            binding.constraintRequest.visibility = View.VISIBLE
                            binding.advisorName.text =
                                "Driver - ${driversItem.result?.firstName} ${driversItem.result?.lastName}"
                            Log.e("driverIddriverIddriverId", driverId.toString())
                            validate()
                        } else {
                            binding.cardView.visibility = View.VISIBLE
                            binding.constraintRequest.visibility = View.GONE
                        }

                    }
                    is Response.Failure -> {
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })

        } else {
            viewModel.driverDrop.observe(this, androidx.lifecycle.Observer {
                when (it) {
                    is Response.Loading -> {
                        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is Response.Success -> {
                        val driversItem = it.data
                        if (driversItem != null) {
                            if (driversItem.driverId != null) {
                                driverId = driversItem.driverId
                                binding.cardView.visibility = View.GONE
                                binding.constraintRequest.visibility = View.VISIBLE
                                binding.advisorName.text =
                                    "Driver - ${driversItem.firstName} ${driversItem.lastName}"
                                Log.e("driverIddriverIddriverId", driverId.toString())
                                validate()
                            } else {
                                binding.cardView.visibility = View.VISIBLE
                                binding.constraintRequest.visibility = View.GONE
                            }
                        } else {
                            binding.cardView.visibility = View.VISIBLE
                            binding.constraintRequest.visibility = View.GONE
                        }
                    }
                    is Response.Failure -> {
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })

        }

        viewModel.update.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {
                    val vItem = it.data
                    if (vItem != null) {
                        finish()
                    }
                    if (vItem != null) {
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

    override fun onResume() {
        super.onResume()

        showDriver()

    }

    private fun validate() {
        if (driverId == null || driverId == -1 || date == null || time == null) {
            binding.extendedFab.visibility = View.GONE
        } else {
            binding.extendedFab.visibility = View.VISIBLE
        }

    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == 101) {
                val driversItem = result.data?.getParcelableExtra<DriversItem>("driversItem")
                val flag = result.data?.getStringExtra("flag")
                if (driversItem != null) {
                    if (driversItem.driverId != -1) {
                        driverId = driversItem.driverId
                        this.flag = flag
                        binding.cardView.visibility = View.GONE
                        binding.constraintRequest.visibility = View.VISIBLE
                        binding.advisorName.text =
                            "Driver - ${driversItem.firstName} ${driversItem.lastName}"
                    }
                    Log.e("driversItemdriversItem", driversItem.toString())
                    Log.e("driverersItem", driversItem.toString())
                    Toast.makeText(this, "registerForActivityResult", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Log.e("driversItem driversItem", driversItem.toString())
                }

            }
        }

    private fun showDriver() {
        try {
            val outletsItems =
                ModelPreferencesManager.get<DriversItem>(Constraints.DRIVER_STORE)
            if (outletsItems != null) {
                if (outletsItems.driverId != -1) {
                    driverId = outletsItems.driverId
                    binding.cardView.visibility = View.GONE
                    binding.constraintRequest.visibility = View.VISIBLE
                    binding.advisorName.text =
                        "Driver - ${outletsItems.firstName} ${outletsItems.lastName}"
                    Log.e("driverId", driverId.toString())
                    validate()
                }
            } else {
                binding.cardView.visibility = View.VISIBLE
                binding.constraintRequest.visibility = View.GONE
            }
        } catch (e: NullPointerException) {

        }
    }

    private fun assignDriver() {
        try {
            if (leadId != -1) {
                leadId?.let {
                    if (dropType == PICK_UP) {
                        viewModel.setAssignDriver(
                            driverId!!,
                            leadId!!,
                            "1",
                            date!!,
                            time!!,
                            Constraints.Schedule_Pick_UP,
                            "Pick Up Scheduled"
                        )
                    } else if (dropType == DROP) {
                        viewModel.setAssignDropDriver(
                            driverId!!,
                            leadId!!,
                            "1",
                            date!!,
                            time!!,
                            Constraints.SCHEDULE_DROP_OFF,
                            "Drop Up Scheduled"
                        )
                    } else {

                    }
                }
            }

//            Log.e("driverId", driverId.toString())
//            Log.e("dropType", dropType.toString())
//            Log.e("leadId", leadId.toString())
//            Log.e("date", date.toString())
//            Log.e("time", time.toString())
        } catch (e: NullPointerException) {
            Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupDateAdapter() {
        val calendar: Calendar = Calendar.getInstance()
        val dStart = Date()
        calendar.time = dStart
        calendar.add(Calendar.DATE, 45)
        var dEnd = Date()
        dEnd = calendar.time

        val list = datesList(dStart, dEnd)
        dateListAdapter =
            DateListAdapter(this, list, this)
        binding.dateRecycleView.apply {
            setHasFixedSize(true)
            adapter = dateListAdapter
            layoutManager =
                LinearLayoutManager(
                    this@SchedulePickUpActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }
    }

    private fun setupTimeAdapter(_date: Date) {

        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = "${formatter.format(_date)}"
        Log.e("Date", date)
        val formatter2 = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())

        val dStart = formatter2.parse("$date 8:00 AM");
        val dEnd = formatter2.parse("$date 8:00 PM");
        val df = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val calendarStart = Calendar.getInstance()
        calendarStart.time = dStart

        val dates = ArrayList<Date>()
        while (calendarStart.time.before(dEnd)) {
//            println(calendarStart.time)
            dates.add(calendarStart.time)
            calendarStart.add(Calendar.HOUR, 1)
        }
        timeListAdapter =
            TimeListAdapter(this, dates, this)
        binding.timeRecycleView.apply {
            setHasFixedSize(true)
            adapter = timeListAdapter
        }

    }

    private fun datesList(start: Date, end: Date): ArrayList<Date> {
        val dates = ArrayList<Date>()
        val calendar = Calendar.getInstance();
        calendar.time = start;

        while (calendar.time.before(end)) {
            val result = calendar.time;
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates
    }

    override fun onItemClick(date: Date) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today: String = formatter.format(date)
        Log.e("today", today)
        this.date = today
        dateListAdapter.setNotifyBtn(date)
        time = null
        setupTimeAdapter(date)
        validate()

    }

    override fun onItemTimeClick(date: Date) {
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val today: String = formatter.format(date)
        Log.e("today", today)
        this.time = today
        validate()

        timeListAdapter.setNotifyBtn(date)
    }

    companion object {
        const val DROP = 2
        const val PICK_UP = 1
    }
}
package com.vendor.mastergarage.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.ActivityLoginBinding
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.di.AppModule.Companion.FLAG_URL
import com.vendor.mastergarage.ui.search.SearchOutletsActivity
import com.vendor.mastergarage.utlis.loadSvg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    //    private val loginViewModel: LoginViewModel by viewModels()
    lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    var mAuth: FirebaseAuth? = null
    private var storedVerificationId: String? = null

    //    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var countryCode: String? = null

    @Inject
    lateinit var vendorPreference: VendorPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.submit.setOnClickListener {
            if (!binding.otpText.text.isNullOrEmpty()) {
                showToast("Verifying...", this@LoginActivity)
                val credential = PhoneAuthProvider.getCredential(
                    // storedVerificationId!!,
                    "1234",
                    binding.otpText.text.toString()
                )
                signInWithPhoneAuthCredential(credential)
            } else {
                showToast("Enter OTP", this)
            }
        }
        binding.getOTP.setOnClickListener {
            sendData()
        }


//        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                signInWithPhoneAuthCredential(credential)
//            }
//
//            override fun onVerificationFailed(e: FirebaseException) {
//
//                if (e is FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                    showToast("FirebaseAuth Invalid Credentials Exception", this@LoginActivity)
//                } else if (e is FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                    showToast("SMS quota is full", this@LoginActivity)
//                }
//
//                // Show a message and update the UI
//            }
//
//            override fun onCodeSent(
//                verificationId: String,
//                token: PhoneAuthProvider.ForceResendingToken
//            ) {
//                showToast("Sent", this@LoginActivity)
//                binding.otpText.requestFocus()
//                storedVerificationId = verificationId
//                binding.otpText.isCursorVisible = true
//            }
//        }

        binding.countryFlag.setOnClickListener {
            intentLauncher.launch(Intent(this, CountryListActivity::class.java))
        }

        vendorPreference.getCountryFlag.asLiveData().observe(this, {
            binding.countryFlag.loadSvg(it)
        })
        vendorPreference.getCountryName.asLiveData().observe(this, Observer {
            binding.phoneNo.hint = it
        })
        vendorPreference.getCountryCode.asLiveData().observe(this, Observer {
            countryCode = it
        })


    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//            var code:String= credential.smsCode
//            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                showToast("FirebaseAuth Invalid Credentials Exception", this@LoginActivity)
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                showToast("SMS quota is full", this@LoginActivity)
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
//            showToast("Sent", this@LoginActivity)
            Toast.makeText(this@LoginActivity, "Sent", Toast.LENGTH_SHORT).show()
            binding.otpText.requestFocus()
            storedVerificationId = verificationId
//            binding.otpText.isCursorVisible = true
        }
    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == 101) {
                val name = result.data?.getStringExtra("name")
                val dialCode = result.data?.getStringExtra("dialCode")
                val code = result.data?.getStringExtra("code")

                val imageUri =
                    "${FLAG_URL}${code}.svg"
                binding.countryFlag.loadSvg(imageUri)

                binding.phoneNo.hint = "$name (+${dialCode})"
                if (dialCode != null) {
                    countryCode = dialCode
                }
            }
        }

    private fun sendData() {
        when {
            binding.phoneNo.text.isNullOrEmpty() -> {

                showToast("Enter Phone number", this)
            }
            countryCode.equals(null) -> {
                showToast(
                    "Select country",
                    this
                )
            }
            else -> {
                showToast(
                    "Please wait",
                    this
                )
                setEnabledButton()
                sendVerificationCode(binding.phoneNo.text.toString())
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        setEnabledButtonTrue()
                    },
                    5000
                )

            }
        }
    }

    private fun setEnabledButtonTrue() {
        binding.getOTP.isEnabled = true
        binding.getOTP.setTextColor(
            ContextCompat.getColorStateList(
                applicationContext,
                R.color.get_OtpColor
            )
        )
    }

    private fun setEnabledButton() {
        binding.getOTP.isEnabled = false
        binding.getOTP.setTextColor(
            ContextCompat.getColorStateList(
                applicationContext,
                R.color.gray
            )
        )
    }

    private fun sendVerificationCode(phoneNo: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setPhoneNumber("$countryCode$phoneNo")
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        Log.e("Phone", "$countryCode$phoneNo")

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    Log.d(TAG, "signInWithCredential:success${user?.uid}")
                    saveData(user)

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    showToast("Failed", this@LoginActivity)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        showToast("FirebaseAuthInvalidCredentialsException", this@LoginActivity)
                    }
                }
            }

    }

    private fun saveData(user: FirebaseUser?) {
        lifecycleScope.launch {
            if (user != null) {
                vendorPreference.setVid(user.uid)
                vendorPreference.setVendorPhone(user.phoneNumber.toString())
                val intent = Intent(this@LoginActivity, SearchOutletsActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                showToast("Failed", this@LoginActivity)
            }
        }
    }

    companion object {
        fun showToast(str: String, context: Context) =
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()

        private const val TAG = "LoginActivity"
    }

    override fun onStart() {
        super.onStart()
        if (mAuth?.currentUser != null) {
            val intent = Intent(this, SearchOutletsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
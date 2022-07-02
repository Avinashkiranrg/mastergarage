package com.vendor.mastergarage.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vendor.mastergarage.di.AppModule
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VendorPreference @Inject constructor(@ApplicationContext context: Context) {

    private val appContext = context.applicationContext

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "vendorPrefs")
        private val tokenId = stringPreferencesKey("tokenId")
        private val vendor_name = stringPreferencesKey("vendor_name")
        private val vendor_email = stringPreferencesKey("vendor_email")
        private val vendor_phone = stringPreferencesKey("vendor_phone")
        private val vendor_id = stringPreferencesKey("vid")
        private val city = stringPreferencesKey("city")
        private val state = stringPreferencesKey("state")
        private val countryFlag = stringPreferencesKey("countryFlag")
        private val countryCode = stringPreferencesKey("countryCode")
        private val countryName = stringPreferencesKey("countryName")
        private val isVendorlogin = booleanPreferencesKey("isVendorlogin")

        private val alertReminders = booleanPreferencesKey("alertReminders")
        private val notifyMeOffers = booleanPreferencesKey("notifyMeOffers")
        private val notifyViaEmail = booleanPreferencesKey("notifyViaEmail")
        private val whatsappNotify = booleanPreferencesKey("whatsappNotify")
        private val rating = booleanPreferencesKey("rating")
        private val updates = booleanPreferencesKey("updates")

    }

    val getVendorLogin: Flow<Boolean>
    get() = appContext.dataStore.data.map {
        it[isVendorlogin] ?: false
    }

    suspend fun setVendorLogin(value: Boolean){
        appContext.dataStore.edit { it[isVendorlogin] = value }
    }

    val getTokenId: Flow<String>
        get() = appContext.dataStore.data.map {
            it[tokenId] ?: ""
        }

    suspend fun setTokenId(value: String) {
        appContext.dataStore.edit { it[tokenId] = value }
    }

    val getVendorId: Flow<String>
        get() = appContext.dataStore.data.map {
            it[vendor_id] ?: ""
        }

    suspend fun setVendorId(value: String) {
        appContext.dataStore.edit { it[vendor_id] = value }
    }

    val getVendorName: Flow<String>
        get() = appContext.dataStore.data.map {
            it[vendor_name] ?: ""
        }

    suspend fun setVendorName(value: String) {
        appContext.dataStore.edit { it[vendor_name] = value }
    }

    val getVendorEmail: Flow<String>
        get() = appContext.dataStore.data.map {
            it[vendor_email] ?: ""
        }

    suspend fun setVendorEmail(value: String) {
        appContext.dataStore.edit { it[vendor_email] = value }
    }

    val getVendorPhone: Flow<String>
        get() = appContext.dataStore.data.map {
            it[vendor_phone] ?: ""
        }

    suspend fun setVendorPhone(value: String) {
        appContext.dataStore.edit { it[vendor_phone] = value }
    }

    val getCity: Flow<String>
        get() = appContext.dataStore.data.map {
            it[city] ?: "Mumbai"
        }

    suspend fun setCity(value: String) {
        appContext.dataStore.edit { it[city] = value }
    }

    val getState: Flow<String>
        get() = appContext.dataStore.data.map {
            it[state] ?: "Maharashtra"
        }

    suspend fun setState(value: String) {
        appContext.dataStore.edit { it[state] = value }
    }

    val getCountryFlag: Flow<String>
        get() = appContext.dataStore.data.map {
            it[countryFlag] ?: "${AppModule.FLAG_URL}IN.svg"
        }

    suspend fun setCountryFlag(value: String) {
        appContext.dataStore.edit { it[countryFlag] = value }
    }

    val getCountryCode: Flow<String>
        get() = appContext.dataStore.data.map {
            it[countryCode] ?: "+91"
        }

    suspend fun setCountryCode(value: String) {
        appContext.dataStore.edit { it[countryCode] = value }
    }

    val getCountryName: Flow<String>
        get() = appContext.dataStore.data.map {
            it[countryName] ?: "India (+91)"
        }

    suspend fun setCountryName(value: String) {
        appContext.dataStore.edit { it[countryName] = value }
    }

    val getAlerts: Flow<Boolean>
        get() = appContext.dataStore.data.map {
            it[alertReminders] ?: true
        }

    suspend fun setAlerts(value: Boolean) {
        appContext.dataStore.edit { it[alertReminders] = value }
    }

    val getNotifyMeOffers: Flow<Boolean>
        get() = appContext.dataStore.data.map {
            it[notifyMeOffers] ?: true
        }

    suspend fun setNotifyMeOffers(value: Boolean) {
        appContext.dataStore.edit { it[notifyMeOffers] = value }
    }

    val getNotifyViaEmail: Flow<Boolean>
        get() = appContext.dataStore.data.map {
            it[notifyViaEmail] ?: true
        }

    suspend fun setNotifyViaEmail(value: Boolean) {
        appContext.dataStore.edit { it[notifyViaEmail] = value }
    }

    val getWhatsappNotify: Flow<Boolean>
        get() = appContext.dataStore.data.map {
            it[whatsappNotify] ?: true
        }

    suspend fun setWhatsappNotify(value: Boolean) {
        appContext.dataStore.edit { it[whatsappNotify] = value }
    }

    val getRating: Flow<Boolean>
        get() = appContext.dataStore.data.map {
            it[rating] ?: false
        }

    suspend fun setRating(value: Boolean) {
        appContext.dataStore.edit { it[rating] = value }
    }

    val getUpdate: Flow<Boolean>
        get() = appContext.dataStore.data.map {
            it[updates] ?: false
        }

    suspend fun setUpdate(value: Boolean) {
        appContext.dataStore.edit { it[updates] = value }
    }


    suspend fun clear() {
        appContext.dataStore.edit {
            it.clear()
        }
    }
}
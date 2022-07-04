package com.vendor.mastergarage.repository

import com.vendor.mastergarage.constraints.Constraints.Companion.OUTLET_STORE
import com.vendor.mastergarage.constraints.Constraints.Companion.VENDOR_STORE
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import com.vendor.mastergarage.model.AcceptLeadsReq
import com.vendor.mastergarage.model.OutletsItem
import com.vendor.mastergarage.model.Vendors
import com.vendor.mastergarage.networkcall.ApiInterface
import com.vendor.mastergarage.networkcall.CityInterface
import com.vendor.mastergarage.networkcall.CountryInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val cityInterface: CityInterface,
    private val countryInterface: CountryInterface
) :
    BaseRepository() {

    suspend fun signUp(mobileNumber: String) = apiInterface.signUp(mobileNumber,"signin")

    suspend fun verifyOtp(mobileNumber: String,verifyOtp: String) = apiInterface.verifyOtp(mobileNumber,verifyOtp,"otpVerified")

    suspend fun dashBoardData(vendorId: String) = apiInterface.dashBoardData("2")

    suspend fun getOnGoingData(vendorId: String,action : String) = apiInterface.onGoingData("2",action)

    suspend fun getPendingData(vendorId: String,action : String) = apiInterface.onGoingData("2",action)

    suspend fun getDeliveredData(vendorId: String,action : String) = apiInterface.onGoingData("2",action)

    suspend fun getVendor(phone: String) = apiInterface.getVendorData(phone)

    suspend fun getVendorOutLets(vendorId: Int) = apiInterface.getVendorOutLets(vendorId)


//    suspend fun selectDefault(oldOutletId: String, newOutletId: String) =
//        apiInterface.selectDefault(oldOutletId, newOutletId)


    suspend fun getCountry() = countryInterface.getCountry()

    suspend fun getCity() = cityInterface.getCity()

    fun getStoredVendorObject(): Vendors? {
        return ModelPreferencesManager.get<Vendors>(VENDOR_STORE)
    }

    fun getStoredOutletObject(): OutletsItem? {
        return ModelPreferencesManager.get<OutletsItem>(OUTLET_STORE)
    }

    fun storeOutletObject(outletsItem: OutletsItem) {
        ModelPreferencesManager.put(outletsItem, OUTLET_STORE)
    }

    fun storeVendorObject(vendors: Vendors) {
        ModelPreferencesManager.put(vendors, VENDOR_STORE)
    }

    suspend fun getIsOperating(isOperating: String, outletId: Int) {
        apiInterface.getIsOperating(isOperating, outletId)
    }

    suspend fun getServiceList() = apiInterface.getServiceList()
    suspend fun getServicePackage() = apiInterface.getServicePackage()

    suspend fun getVehicleList() = apiInterface.getVehicleList()

    suspend fun getDrivers(outletId: Int) = apiInterface.getDrivers(outletId)
    suspend fun getCounts(outletId: Int) = apiInterface.getCounts(outletId)
    suspend fun getVehicles(outletId: Int) = apiInterface.getVehicles(outletId)
    suspend fun getVehicleModels(vehicleId: Int, outletId: Int) =
        apiInterface.getVehicleModels(vehicleId, outletId)

    suspend fun getServiceAdvisor(outletId: Int) = apiInterface.getServiceAdvisor(outletId)

    suspend fun getAssignedDriver(leadId: Int) = apiInterface.getAssignedDriver(leadId)
    suspend fun getAssignedDriverDrop(leadId: Int) = apiInterface.getAssignedDriverDrop(leadId)
    suspend fun getPaymentInfo(leadId: Int) = apiInterface.getPaymentInfo(leadId)

    suspend fun addServiceAdvisor(
        first_name: String,
        last_name: String,
        designation: String,
        imageUri: String,
        dob: String,
        mobile_no: String,
        outletId: Int
    ) = apiInterface.addServiceAdvisor(
        first_name,
        last_name,
        designation,
        imageUri,
        dob,
        mobile_no,
        outletId,
    )

    suspend fun acceptLeads(acceptLeadsReq: AcceptLeadsReq
    ) = apiInterface.acceptLeads(
        acceptLeadsReq.leadId.toString(),
        acceptLeadsReq.last_up_date,
        acceptLeadsReq.last_up_time,
        acceptLeadsReq.booking_date,
        acceptLeadsReq.booking_time,
        acceptLeadsReq.outletId,
        acceptLeadsReq.vehicleId,
        acceptLeadsReq.addressId

    )

    suspend fun addService(
        leadId: Int,
        outletId: Int,
        vendorId: Int,
        service_name: String,
        service_cost: Float,
        other_charges: Float,
        additionalInfo: String,
        attachmentUri: String,
        json_spare_string: String,
    ) = apiInterface.addService(
        leadId,
        outletId,
        vendorId,
        service_name,
        service_cost,
        other_charges,
        additionalInfo,
        attachmentUri,
        json_spare_string,
    )

    suspend fun editService(
        serviceId: Int,
        service_name: String,
        service_cost: Float,
        other_charges: Float,
        additionalInfo: String,
        attachmentUri: String?,
    ) = apiInterface.editService(
        serviceId,
        service_name,
        service_cost,
        other_charges,
        additionalInfo,
        attachmentUri,
    )

    suspend fun editServiceSpare(
        sparesId: Int,
        manufacturer_name: String,
        part_name: String,
        warranty: String,
        year: Int,
        part_type: Int,
    ) = apiInterface.editServiceSpare(
        sparesId,
        manufacturer_name,
        part_name,
        warranty,
        year,
        part_type,
    )


    suspend fun copyServicetoAnotherLead(
        leadId: Int,
        outletId: Int,
        vendorId: Int,
        service_name: String?,
        service_cost: Float?,
        other_charges: Float?,
        additionalInfo: String?,
        attachmentUri: String?,
        spares: String,

        ) = apiInterface.copyServicetoAnotherLead(
        leadId,
        outletId,
        vendorId,
        service_name,
        service_cost,
        other_charges,
        additionalInfo,
        attachmentUri,
        spares
    )

    suspend fun addDriver(
        first_name: String,
        last_name: String,
        licence_no: String,
        imageUri: String,
        licenceUri: String,
        dob: String,
        mobile_no: String,
        outletId: Int
    ) = apiInterface.addDriver(
        first_name,
        last_name,
        licence_no,
        imageUri,
        licenceUri,
        dob,
        mobile_no,
        outletId,
    )

    suspend fun deleteDriver(driverId: Int) = apiInterface.deleteDriver(driverId)

    suspend fun deleteAdvisor(advisorId: Int) = apiInterface.deleteAdvisor(advisorId)

    suspend fun updateDriver(
        driverId: Int,
        first_name: String,
        last_name: String,
        licence_no: String,
        imageUri: String?,
        licenceUri: String?,
        dob: String,
        mobile_no: String
    ) = apiInterface.updateDriver(
        driverId,
        first_name,
        last_name,
        licence_no,
        imageUri,
        licenceUri,
        dob,
        mobile_no
    )

    suspend fun updateServiceAdvisor(
        advisorId: Int,
        first_name: String,
        last_name: String,
        designation: String,
        imageUri: String?,
        dob: String,
        mobile_no: String
    ) = apiInterface.updateServiceAdvisor(
        advisorId,
        first_name,
        last_name,
        designation,
        imageUri,
        dob,
        mobile_no
    )

    suspend fun addRating(
        rating: Float,
        message: String,
        vendorId: Int
    ) = apiInterface.addRating(rating, message, vendorId)

    suspend fun getLeads(outletId: Int) = apiInterface.getLeads(outletId)
    suspend fun getOnGoing(outletId: Int) = apiInterface.getOnGoing(outletId)
    suspend fun getDelivered(outletId: Int) = apiInterface.getDelivered(outletId)
    suspend fun declineLeads(leadId: Int) = apiInterface.declineLeads(leadId)
    suspend fun getLeadsById(leadId: Int) = apiInterface.getLeadsById(leadId)
    suspend fun getAssignedInventory(leadId: Int) = apiInterface.getAssignedInventory(leadId)
    suspend fun getJobCard(leadId: Int) = apiInterface.getJobCard(leadId)
    suspend fun deleteService(leadId: Int) = apiInterface.deleteService(leadId)
    suspend fun deleteRcommServiceSpare(sparesId: Int) =
        apiInterface.deleteRcommServiceSpare(sparesId)

    suspend fun getAssignedDamaged(leadId: Int) = apiInterface.getAssignedDamaged(leadId)
    suspend fun getServiceInstruction(leadId: Int) = apiInterface.getServiceInstruction(leadId)
    suspend fun getServiceInstructionByOwner(leadId: Int) =
        apiInterface.getServiceInstructionByOwner(leadId)

    suspend fun getServiceRecomm(leadId: Int) = apiInterface.getServiceRecomm(leadId)
    suspend fun getServiceRecommByVendorId(vendorId: Int, leadId: Int) =
        apiInterface.getServiceRecommByVendorId(vendorId, leadId)

    suspend fun getInventory() = apiInterface.getInventory()
    suspend fun getDamaged() = apiInterface.getDamaged()
    suspend fun getServiceRequested(leadId: Int) = apiInterface.getServiceRequested(leadId)
    suspend fun getUpdateStatus(leadId: Int) = apiInterface.getUpdateStatus(leadId)
    suspend fun getAssignedAdvisor(leadId: Int) = apiInterface.getAssignedAdvisor(leadId)

//    suspend fun getOwnerDetails(ownerId: Int) = apiInterface.getOwnerDetails(ownerId)

    suspend fun setOnGoing(
        last_up_date: String,
        last_up_time: String,
        booking_date: String,
        booking_time: String,
        outletId: Int,
        vehicleId: Int,
        addressId: Int,
        leadId: Int
    ) =
        apiInterface.setOnGoing(
            last_up_date,
            last_up_time,
            booking_date,
            booking_time,
            outletId,
            vehicleId,
            addressId,
            leadId
        )

    suspend fun closedLeads(
        last_up_date: String,
        last_up_time: String,
        update_status: Int,
        update_remarks: String,
        outletId: Int,
        vehicleId: Int,
        addressId: Int,
        leadId: Int
    ) =
        apiInterface.closedLeads(
            last_up_date,
            last_up_time,
            update_status,
            update_remarks,
            outletId,
            vehicleId,
            addressId,
            leadId
        )

    suspend fun setAssignAdvisor(
        advisorId: Int,
        leadId: Int,
        status: String,
        last_up_date: String,
        last_up_time: String,
        update_status: Int,
        update_remarks: String
    ) =
        apiInterface.setAssignAdvisor(
            advisorId,
            leadId,
            status,
            last_up_date,
            last_up_time,
            update_status,
            update_remarks
        )

    suspend fun setAssignAdvisorChange(
        advisorId: Int,
        leadId: Int,
        status: String,
        last_up_date: String,
        last_up_time: String
    ) =
        apiInterface.setAssignAdvisorChange(
            advisorId,
            leadId,
            status,
            last_up_date,
            last_up_time
        )

    suspend fun setAssignDriverChange(
        driverId: Int,
        leadId: Int,
        status: String,
        last_up_date: String,
        last_up_time: String
    ) =
        apiInterface.setAssignDriverChange(
            driverId,
            leadId,
            status,
            last_up_date,
            last_up_time
        )


    suspend fun updateStatus(
        last_up_date: String,
        last_up_time: String,
        update_status: Int,
        update_remarks: String,
        leadId: Int
    ) = apiInterface.updateStatus(last_up_date, last_up_time, update_status, update_remarks, leadId)

    suspend fun setAssignDriver(
        driverId: Int,
        leadId: Int,
        status: String,
        last_up_date: String,
        last_up_time: String,
        update_status: Int,
        update_remarks: String
    ) =
        apiInterface.setAssignDriver(
            driverId,
            leadId,
            status,
            last_up_date,
            last_up_time,
            update_status,
            update_remarks
        )

    suspend fun setAssignDropDriver(
        driverId: Int,
        leadId: Int,
        status: String,
        last_up_date: String,
        last_up_time: String,
        update_status: Int,
        update_remarks: String
    ) =
        apiInterface.setAssignDropDriver(
            driverId,
            leadId,
            status,
            last_up_date,
            last_up_time,
            update_status,
            update_remarks
        )

    suspend fun getOwnerDetails(leadId: Int) = apiInterface.getOwnerDetails(leadId)
    suspend fun getOwnerAddress(leadId: Int) = apiInterface.getOwnerAddress(leadId)
    suspend fun getScheduledService(leadId: Int) = apiInterface.getScheduledService(leadId)

    suspend fun getFaq() = apiInterface.getFaq()
    suspend fun getContactus() = apiInterface.getContactus()
    suspend fun getDiscounts(outletId: Int) = apiInterface.getDiscounts(outletId)
    suspend fun getServiceProvide(outletId: Int) = apiInterface.getServiceProvide(outletId)
    suspend fun getServicePackageProvide(outletId: Int, serviceId: Int) =
        apiInterface.getServicePackageProvide(outletId, serviceId)

    suspend fun disablePackage(vpId: Int, outletId: Int, packageId: Int, flag: String) =
        apiInterface.disablePackage(vpId, outletId, packageId, flag)

    suspend fun disableService(vaId: Int, outletId: Int, serviceId: Int, flag: String) =
        apiInterface.disableService(vaId, outletId, serviceId, flag)

    suspend fun disableVariants(vpId: Int, flag: String) =
        apiInterface.disableVariants(vpId, flag)

    suspend fun disableVehicle(vpId: Int, flag: String) =
        apiInterface.disableVehicle(vpId, flag)

    suspend fun disableModel(vpId: Int, flag: String) =
        apiInterface.disableModel(vpId, flag)


    suspend fun addDiscounts(
        applicable_on: String,
        end_date: String,
        start_date: String,
        type_discount: String,
        min_order_value: Int,
        discount_code: String,
        outletId: Int,
        max_discount: Int,
        discount_value: Int,
        apply_users: String
    ) = apiInterface.addDiscounts(
        applicable_on,
        end_date,
        start_date,
        type_discount,
        min_order_value,
        discount_code,
        outletId,
        max_discount,
        discount_value,
        apply_users
    )

    suspend fun uploadJob(
        leadId: Int,
        damagedString: String,
        inventoryString: String,
        lastUpDate: String,
        lastUpTime: String,
        updateStatus: Int,
        updateRemark: String,
        kmsDriven: Int,
        fuel: Int,
        instruction: String
    ) = apiInterface.uploadJob(
        leadId,
        damagedString,
        inventoryString,
        lastUpDate,
        lastUpTime,
        updateStatus,
        updateRemark,
        kmsDriven,
        fuel,
        instruction
    )

    suspend fun uploadJobCardImage(
        leadId: Int,
        imageString: String,
    ) = apiInterface.uploadJobCardImage(
        leadId,
        imageString,
    )


    suspend fun getJobCardImage(
        leadId: Int,
    ) = apiInterface.getJobCardImage(
        leadId,
    )

    suspend fun deleteImage(
        imageId: Int,
    ) = apiInterface.deleteImage(
        imageId,
    )

    private fun reqBody(leadId: Float): RequestBody {
        val name: RequestBody = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(), leadId.toString()
        )
        return name
    }

    private fun reqBody(leadId: Int): RequestBody {
        val name: RequestBody = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(), leadId.toString()
        )
        return name
    }


    private fun reqBody(filename: String): RequestBody {
        val name: RequestBody = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(), filename
        )
        return name
    }
}
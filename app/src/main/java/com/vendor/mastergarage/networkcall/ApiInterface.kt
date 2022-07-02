package com.vendor.mastergarage.networkcall

import com.example.uidesign.model.Discounts
import com.vendor.mastergarage.model.*
import retrofit2.Response
import retrofit2.http.*
import java.net.ResponseCache


interface ApiInterface {

    @FormUrlEncoded
    @POST("signin.php")
    suspend fun signUp (
        @Field("mobileNumber") mobileNumber: String,
        @Field("action") action: String
    ) : Response<SignUpRespon>

    @FormUrlEncoded
    @POST("signin.php")
    suspend fun verifyOtp (
        @Field("mobileNumber") mobileNumber: String,
        @Field("otpCode") otpCode: String,
        @Field("action") action: String
    ) : Response<VerifyOtpRespon>


    @GET("getoutlet-counts.php?")
    suspend fun dashBoardData (
        @Query("vendorId") vendorId: String
    ) : Response<DashBoardRespon>


    @GET("getAllLeads.php?")
    suspend fun onGoingData (
        @Query("vendorId") vendorId: String,
        @Query("action") action: String
    ) : Response<OnGoingRespo>


    @GET("getVendor.php")
    suspend fun getVendorData(@Query("phone") phone: String): Response<Vendors>

    @GET("getVendorOutLets.php")
    suspend fun getVendorOutLets(@Query("vendorId") vendorId: Int): Response<Outlets>

    @FormUrlEncoded
    @POST("updateIsOperating.php")
    suspend fun getIsOperating(
        @Field("isOperating") isOperating: String,
        @Field("outletId") outletId: Int
    ): Response<String>

    @GET("getServiceList.php")
    suspend fun getServiceList(): Response<ServiceList>

    @GET("getVehicleList.php")
    suspend fun getVehicleList(): Response<Vehicles>

    @GET("getDrivers.php")
    suspend fun getDrivers(@Query("outletId") outletId: Int): Response<Drivers>

    @GET("getCounts.php")
    suspend fun getCounts(@Query("outletId") outletId: Int): Response<TotalRequests>

    @GET("getVehicles.php")
    suspend fun getVehicles(@Query("outletId") outletId: Int): Response<VehicleProvide>

    @GET("getVehicleModels.php")
    suspend fun getVehicleModels(
        @Query("vehicleId") vehicleId: Int,
        @Query("outletId") outletId: Int
    ): Response<VehicleModel>

    @GET("getServiceAdvisor.php")
    suspend fun getServiceAdvisor(@Query("outletId") outletId: Int): Response<ServiceAdvisor>

    @GET("getServicePackage.php")
    suspend fun getServicePackage(): Response<ServicePackage>

//    @POST("addUser.php")
//    @Multipart
//    fun addServiceAdvisor(
//        @Part("first_name") first_name: String,
//        @Part("last_name") last_name: String,
//        @Part("designation") designation: String,
////        @Part("imageUri") imageUri: String,
//        @Part("dob") dob: String,
//        @Part("mobile_no") mobile_no: String,
//        @Part("outletId") outletId: String,
//        @Part("picPath") picPath: MultipartBody.Part
//    ): Response<String>

    @FormUrlEncoded
    @POST("AddServiceAdvisor.php")
    suspend fun addServiceAdvisor(
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("designation") designation: String,
        @Field("imageUri") imageUri: String,
        @Field("dob") dob: String,
        @Field("mobile_no") mobile_no: String,
        @Field("outletId") outletId: Int
    ): Response<ServiceAdvisorResponse>


//    @FormUrlEncoded
//    @POST("addService.php")
//    suspend fun addService(
//        @Field("leadId") leadId: Int,
//        @Field("outletId") outletId: Int,
//        @Field("vendorId") vendorId: Int,
//        @Field("service_name") service_name: String,
//        @Field("service_cost") service_cost: Float,
//        @Field("other_charges") other_charges: Float,
//        @Field("additionalInfo") additionalInfo: String,
//        @Field("attachmentUri") attachmentUri: String,
//        @Field("json_spare_string") json_spare_string: String,
//    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("addService.php")
    suspend fun addService(
        @Field("leadId") leadId: Int,
        @Field("outletId") outletId: Int,
        @Field("vendorId") vendorId: Int,
        @Field("service_name") service_name: String,
        @Field("service_cost") service_cost: Float,
        @Field("other_charges") other_charges: Float,
        @Field("additionalInfo") additionalInfo: String,
        @Field("attachmentUri") attachmentUri: String,
        @Field("json_spare") json_spare: String
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("EditService.php")
    suspend fun editService(
        @Field("serviceId") serviceId: Int,
        @Field("service_name") service_name: String,
        @Field("service_cost") service_cost: Float,
        @Field("other_charges") other_charges: Float,
        @Field("additionalInfo") additionalInfo: String,
        @Field("attachmentUri") attachmentUri: String?,
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("EditServiceSpare.php")
    suspend fun editServiceSpare(
        @Field("sparesId") sparesId: Int,
        @Field("manufacturer_name") manufacturer_name: String,
        @Field("part_name") part_name: String,
        @Field("warranty") warranty: String,
        @Field("year") year: Int,
        @Field("part_type") part_type: Int,
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("copyServicetoAnotherLead.php")
    suspend fun copyServicetoAnotherLead(
        @Field("leadId") leadId: Int,
        @Field("outletId") outletId: Int,
        @Field("vendorId") vendorId: Int,
        @Field("service_name") service_name: String?,
        @Field("service_cost") service_cost: Float?,
        @Field("other_charges") other_charges: Float?,
        @Field("additionalInfo") additionalInfo: String?,
        @Field("attachmentUri") attachmentUri: String?,
        @Field("json_spare") json_spare: String
    ): Response<UploadResponse>

    @GET("getServiceRecomm.php")
    suspend fun getServiceRecomm(
        @Query("leadId") leadId: Int
    ): Response<ServiceDetailsList>

    @GET("getServiceInstruction.php")
    suspend fun getServiceInstruction(
        @Query("leadId") leadId: Int
    ): Response<ServiceInstruction>

    @GET("getServiceInstructionByOwner.php")
    suspend fun getServiceInstructionByOwner(
        @Query("leadId") leadId: Int
    ): Response<Instructions>


    @GET("getServiceRecommByVendorId.php")
    suspend fun getServiceRecommByVendorId(
        @Query("vendorId") vendorId: Int,
        @Query("leadId") leadId: Int
    ): Response<ServiceDetailsList>

    @GET("deleteRcommService.php")
    suspend fun deleteService(
        @Query("leadId") leadId: Int
    ): Response<UploadResponse>

    @GET("deleteRcommServiceSpare.php")
    suspend fun deleteRcommServiceSpare(
        @Query("sparesId") sparesId: Int
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("addDriver.php")
    suspend fun addDriver(
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("licence_no") licence_no: String,
        @Field("imageUri") imageUri: String,
        @Field("licenceUri") licenceUri: String,
        @Field("dob") dob: String,
        @Field("mobile_no") mobile_no: String,
        @Field("outletId") outletId: Int
    ): Response<UploadResponse>

    @GET("deleteDriver.php")
    suspend fun deleteDriver(@Query("driverId") driverId: Int): Response<UploadResponse>

    @GET("deleteAdvisor.php")
    suspend fun deleteAdvisor(@Query("advisorId") advisorId: Int): Response<UploadResponse>

    @FormUrlEncoded
    @POST("updateServiceAdvisor.php")
    suspend fun updateServiceAdvisor(
        @Field("advisorId") advisorId: Int,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("designation") designation: String,
        @Field("imageUri") imageUri: String?,
        @Field("dob") dob: String,
        @Field("mobile_no") mobile_no: String
    ): Response<ServiceAdvisorResponse>

    @FormUrlEncoded
    @POST("updateDriver.php")
    suspend fun updateDriver(
        @Field("driverId") driverId: Int,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("licence_no") licence_no: String,
        @Field("imageUri") imageUri: String?,
        @Field("licenceUri") licenceUri: String?,
        @Field("dob") dob: String,
        @Field("mobile_no") mobile_no: String
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("addRating.php")
    suspend fun addRating(
        @Field("rating") rating: Float,
        @Field("message") message: String,
        @Field("vendorId") vendorId: Int
    ): Response<UploadResponse>

    @GET("getLeads.php")
    suspend fun getLeads(
        @Query("outletId") outletId: Int
    ): Response<Leads>

    @GET("getLeadsById.php")
    suspend fun getLeadsById(
        @Query("leadId") leadId: Int
    ): Response<LeadItemData>

    @GET("getServiceRequested.php")
    suspend fun getServiceRequested(
        @Query("leadId") leadId: Int
    ): Response<ServiceRequested>

    @GET("getAssignedInventory.php")
    suspend fun getAssignedInventory(
        @Query("leadId") leadId: Int
    ): Response<JobCardInventory>

    @GET("getJobCard.php")
    suspend fun getJobCard(
        @Query("leadId") leadId: Int
    ): Response<JobCard>

    @GET("getAssignedDamaged.php")
    suspend fun getAssignedDamaged(
        @Query("leadId") leadId: Int
    ): Response<JobCardDamaged>

    @GET("getInventory.php")
    suspend fun getInventory(): Response<Inventory>

    @GET("getDamaged.php")
    suspend fun getDamaged(): Response<Damaged>

//    @GET("getOwnerDetails.php")
//    suspend fun getOwnerDetails(ownerId: Int): Response<VehicleOwner>

    @GET("declineLeads.php")
    suspend fun declineLeads(
        @Query("leadId") leadId: Int
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("acceptLeads.php")
    suspend fun setOnGoing(
        @Field("last_up_date") last_up_date: String,
        @Field("last_up_time") last_up_time: String,
        @Field("booking_date") booking_date: String,
        @Field("booking_time") booking_time: String,
        @Field("outletId") outletId: Int,
        @Field("vehicleId") vehicleId: Int,
        @Field("addressId") addressId: Int,
        @Field("leadId") leadId: Int
    ): Response<UpdateStatus>

    @FormUrlEncoded
    @POST("closedLeads.php")
    suspend fun closedLeads(
        @Field("last_up_date") last_up_date: String,
        @Field("last_up_time") last_up_time: String,
        @Field("update_status") update_status: Int,
        @Field("update_remarks") update_remarks: String,
        @Field("outletId") outletId: Int,
        @Field("vehicleId") vehicleId: Int,
        @Field("addressId") addressId: Int,
        @Field("leadId") leadId: Int
    ): Response<UploadResponse>


    @GET("getUpdateStatus.php")
    suspend fun getUpdateStatus(
        @Query("leadId") leadId: Int
    ): Response<StatusUpdate>

    @GET("getAssignedAdvisor.php")
    suspend fun getAssignedAdvisor(
        @Query("leadId") leadId: Int
    ): Response<ServiceAdvisorAssigned>

    @GET("getAssignedDriver.php")
    suspend fun getAssignedDriver(
        @Query("leadId") leadId: Int
    ): Response<DriverAssigned>

    @GET("getAssignedDriverDrop.php")
    suspend fun getAssignedDriverDrop(
        @Query("leadId") leadId: Int
    ): Response<DriverDrop>

    @GET("getPaymentInfo.php")
    suspend fun getPaymentInfo(
        @Query("leadId") leadId: Int
    ): Response<PaymentInfo>


    @GET("getOnGoingAll.php")
    suspend fun getOnGoing(
        @Query("outletId") outletId: Int
    ): Response<OnGoingData>

    @GET("getDelivered.php")
    suspend fun getDelivered(
        @Query("outletId") outletId: Int
    ): Response<OnDelivered>

    @FormUrlEncoded
    @POST("setAssignAdvisor.php")
    suspend fun setAssignAdvisor(
        @Field("advisorId") advisorId: Int,
        @Field("leadId") leadId: Int,
        @Field("status") status: String,
        @Field("last_up_date") last_up_date: String,
        @Field("last_up_time") last_up_time: String,
        @Field("update_status") update_status: Int,
        @Field("update_remarks") update_remarks: String,
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("setAssignDriver.php")
    suspend fun setAssignDriver(
        @Field("driverId") driverId: Int,
        @Field("leadId") leadId: Int,
        @Field("status") status: String,
        @Field("last_up_date") last_up_date: String,
        @Field("last_up_time") last_up_time: String,
        @Field("update_status") update_status: Int,
        @Field("update_remarks") update_remarks: String,
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("setAssignDropDriver.php")
    suspend fun setAssignDropDriver(
        @Field("driverId") driverId: Int,
        @Field("leadId") leadId: Int,
        @Field("status") status: String,
        @Field("last_up_date") last_up_date: String,
        @Field("last_up_time") last_up_time: String,
        @Field("update_status") update_status: Int,
        @Field("update_remarks") update_remarks: String,
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("setAssignAdvisorChange.php")
    suspend fun setAssignAdvisorChange(
        @Field("advisorId") advisorId: Int,
        @Field("leadId") leadId: Int,
        @Field("status") status: String,
        @Field("last_up_date") last_up_date: String,
        @Field("last_up_time") last_up_time: String
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("setAssignDriverChange.php")
    suspend fun setAssignDriverChange(
        @Field("driverId") driverId: Int,
        @Field("leadId") leadId: Int,
        @Field("status") status: String,
        @Field("last_up_date") last_up_date: String,
        @Field("last_up_time") last_up_time: String
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("updateStatus.php")
    suspend fun updateStatus(
        @Field("last_up_date") last_up_date: String,
        @Field("last_up_time") last_up_time: String,
        @Field("update_status") update_status: Int,
        @Field("update_remarks") update_remarks: String,
        @Field("leadId") leadId: Int
    ): Response<UploadResponse>

    @GET("getFaq.php")
    suspend fun getFaq(): Response<Faq>

    @GET("getContactus.php")
    suspend fun getContactus(): Response<ContactusItem>

    @FormUrlEncoded
    @POST("addDiscounts.php")
    suspend fun addDiscounts(
        @Field("applicable_on") applicable_on: String,
        @Field("end_date") end_date: String,
        @Field("start_date") start_date: String,
        @Field("type_discount") type_discount: String,
        @Field("min_order_value") min_order_value: Int,
        @Field("discount_code") discount_code: String,
        @Field("outletId") outletId: Int,
        @Field("max_discount") max_discount: Int,
        @Field("discount_value") discount_value: Int,
        @Field("apply_users") apply_users: String
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("uploadJobCard.php")
    suspend fun uploadJob(
        @Field("leadId") leadId: Int,
        @Field("damaged_string") damaged_string: String,
        @Field("inventory_string") inventory_string: String,
        @Field("last_up_date") last_up_date: String,
        @Field("last_up_time") last_up_time: String,
        @Field("update_status") update_status: Int,
        @Field("update_remarks") update_remarks: String,
        @Field("kms_driven") kms_driven: Int,
        @Field("fuel") fuel: Int,
        @Field("instruction") instruction: String
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("uploadJobCardImage.php")
    suspend fun uploadJobCardImage(
        @Field("leadId") leadId: Int,
        @Field("json_image") json_image: String,
    ): Response<UploadResponse>

    @GET("getJobCardImage.php")
    suspend fun getJobCardImage(
        @Query("leadId") leadId: Int,
    ): Response<Images>

    @GET("deleteImage.php")
    suspend fun deleteImage(
        @Query("imageId") imageId: Int,
    ): Response<UploadResponse>


    @GET("getDiscounts.php")
    suspend fun getDiscounts(
        @Query("outletId") outletId: Int
    ): Response<Discounts>

    @GET("getOwnerDetailsByLeadId.php")
    suspend fun getOwnerDetails(@Query("leadId") leadId: Int): Response<OwnerInfo>

    @GET("getOwnerAddressByLeadId.php")
    suspend fun getOwnerAddress(@Query("leadId") leadId: Int): Response<UserAddress>

    @GET("getScheduledServiceByLeadId.php")
    suspend fun getScheduledService(@Query("leadId") leadId: Int): Response<BookingIds>


    @GET("getServicePackageProvideOutlet.php")
    suspend fun getServicePackageProvide(
        @Query("outletId") outletId: Int,
        @Query("serviceId") serviceId: Int
    ): Response<ServicePackageProvide>

    @GET("getServiceProvide.php")
    suspend fun getServiceProvide(
        @Query("outletId") outletId: Int
    ): Response<ServiceList>

    @GET("disablePackage.php")
    suspend fun disablePackage(
        @Query("vpId") vpId: Int,
        @Query("outletId") outletId: Int,
        @Query("packageId") packageId: Int,
        @Query("status") flag: String
    ): Response<UploadResponse>

    @GET("disableService.php")
    suspend fun disableService(
        @Query("vsId") vsId: Int,
        @Query("outletId") outletId: Int,
        @Query("serviceId") serviceId: Int,
        @Query("status") flag: String
    ): Response<UploadResponse>

    @GET("disableModel.php")
    suspend fun disableModel(
        @Query("vmId") vmId: Int,
        @Query("status") flag: String
    ): Response<UploadResponse>

    @GET("disableVariants.php")
    suspend fun disableVariants(
        @Query("vvId") vvId: Int,
        @Query("status") flag: String
    ): Response<UploadResponse>

    @GET("disableVehicle.php")
    suspend fun disableVehicle(
        @Query("vcId") vcId: Int,
        @Query("status") flag: String
    ): Response<UploadResponse>
}



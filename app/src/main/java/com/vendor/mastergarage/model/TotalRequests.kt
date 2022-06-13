package com.vendor.mastergarage.model


data class TotalRequests(
    val deliveredRows: Int,
    val leadsRows: Int,
    val message: String,
    val ongoingRows: Int,
    val success: Int
)
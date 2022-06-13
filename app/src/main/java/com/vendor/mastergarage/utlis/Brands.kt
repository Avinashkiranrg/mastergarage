package com.vendor.mastergarage.utlis

import com.vendor.mastergarage.model.CarBrands

fun brandsData(): ArrayList<CarBrands> {
    val userList: ArrayList<CarBrands> = ArrayList()
    userList.add(CarBrands(1, "Maruti suzuki", "1 Maruti.jpeg"))
    userList.add(CarBrands(2,  "Hyundai", "2 Hyundai.jpeg"))
    userList.add(CarBrands(3,  "Honda", "3 Honda.jpeg"))
    userList.add(CarBrands(4,  "Fiat", "4 Fiat.jpeg"))
    userList.add(CarBrands(5,  "Datsun", "5 Datsun.jpeg"))
    userList.add(CarBrands(6, "Chevrolet", "6 Chevrolet.jpeg"))
    userList.add(CarBrands(7, "Ford", "7 ford.jpeg"))
    userList.add(CarBrands(8,  "Mahindra", "8 Mahindra.jpeg"))
    userList.add(CarBrands(9, "Mitubishi", "9 Mitubishi.jpeg"))
    userList.add(CarBrands(10, "Nissan", "10 Nissan.jpeg"))
    userList.add(CarBrands(11, "Renault", "11 Renault.jpeg"))
    userList.add(CarBrands(12,  "Skoda", "12 Skoda.jpeg"))
    userList.add(CarBrands(13, "TATA", "13 TATA.jpeg"))
    userList.add(CarBrands(14, "Toyota", "14 Toyota.jpeg"))
    userList.add(CarBrands(15, "Volkswagen", "15 Volkswagen.jpeg"))
    userList.add(CarBrands(16,  "Aston Martin", "16 Aston Martin.jpeg"))
    userList.add(CarBrands(17,  "Audi", "17 Audi.jpeg"))
    userList.add(CarBrands(18,  "Bentley", "18 Bentley.jpeg"))
    userList.add(CarBrands(19, "BMW", "19 BMW.jpeg"))
    userList.add(CarBrands(20, "Ferrari", "20 Ferrari.jpeg"))
    userList.add(CarBrands(21,  "Jaguar", "21 Jaguar.jpeg"))
    userList.add(CarBrands(22, "Lamborgini", "22 Lamborgini.jpeg"))
    userList.add(CarBrands(23, "Land Rover", "23 Land Rover.jpeg"))
    userList.add(CarBrands(24,  "Maserati", "24 Maserati.jpeg"))
    userList.add(CarBrands(25, "Mercedes", "25 Mercedes.jpeg"))
    userList.add(CarBrands(26, "Mini", "26 Mini.jpeg"))
    userList.add(CarBrands(27,  "Porsche", "27 Porsche.jpeg"))
    userList.add(CarBrands(28,  "Rolls Royce", "28 Rolls Royce.jpeg"))
    userList.add(CarBrands(29, "Volvo", "29 Volvo.jpeg"))
    userList.add(CarBrands(30, "Force", "30 Force.jpeg"))
    userList.add(CarBrands(31, "ISUZU", "31 ISUZU.jpeg"))
    userList.add(CarBrands(32, "Jeep", "32 Jeep.jpeg"))
    userList.add(CarBrands(33,  "SsangYong", "33 SsangYong.jpeg"))
    userList.add(CarBrands(34, "KIA", "34 KIA.jpeg"))
    userList.add(CarBrands(35, "Morris Garage", "35 Morris Garage.jpeg"))
    userList.add(CarBrands(36,  "Foton", "36 Foton.jpeg"))
    userList.add(CarBrands(37, "Lexus", "37 Lexus.jpeg"))
    userList.add(CarBrands(38, "Citroen", "38 Citroen.jpeg"))

    return userList
}

//fun item(brandId: Int, brand_code: String, brand_name: String, imageUri: String) {
//    val prefix = "brands/"
//
//    val carBrands = CarBrands(brandId, brand_code, brand_name, "$prefix$imageUri")
//    userList?.add(carBrands)
//    vehicleAdapter.setFilter(userList!!)
//}
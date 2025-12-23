package com.example.ticket_kotlin.builder

class Ticket private constructor(
    val userName : String,
    val seatNumber : Int,
    val type : String,
    val price : Double
){
    class Builder{
            private var userName = ""
            private var seatNumber = 0
            private var type = ""
            private var price = 0.0

        fun setUserName(value:String)= apply{this.userName = value}
        fun setType (value:String) = apply{this.type = value}
        fun setSeatNumber (value:Int) = apply {this.seatNumber = value}
        fun setPrice (value:Double) = apply{this.price = value}

        fun build() = Ticket(userName,seatNumber,type,price)
    }
}
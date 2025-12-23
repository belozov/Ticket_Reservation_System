    package com.example.ticket_kotlin.strategy
    data class Seat (
         val number : Int,
         val type : String,
         val priceBonus : Double,
         private var booked : Boolean = false
    )
    {
        fun isBooked() = booked
        fun setBooked(value: Boolean) {booked = value}
        fun unbook() {
            booked = false
        }

    }
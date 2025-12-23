package com.example.ticket_kotlin.observer
import android.util.Log

import com.example.ticket_kotlin.strategy.Seat

class SeatBookingManager(
    val observers : MutableList<SeatObserver> = mutableListOf()
){
    fun attach(observer : SeatObserver){ observers.add(observer) }

    fun detach(observer : SeatObserver){observers.remove(observer)}

    fun bookSeat(seat: Seat){
        if(!seat.isBooked()){
           seat.setBooked(true)
            Log.d("Booking..." , "Booked: " + seat);
            notifySeatReleased(seat)
      } else {
          Log.d("Booking...", "Already free: " + seat)
        }
    }

    private fun notifySeatBooked(seat: Seat){
        observers.forEach { it.onSeatBooked(seat) }
    }
    private fun notifySeatReleased (seat: Seat){
        observers.forEach { it.onSeatReleased(seat) }
    }
}




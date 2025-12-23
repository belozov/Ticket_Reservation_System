package com.example.ticket_kotlin.observer

import com.example.ticket_kotlin.strategy.Seat

interface SeatObserver {
    fun onSeatBooked(seat: Seat);

    fun onSeatReleased(seat: Seat);
}
package com.example.ticket_kotlin.strategy

class FrontToBackSeatingStrategy : SeatingStrategy {
    override fun chooseSeat(seat: Array<Seat>): Seat? {
        return seat.firstOrNull{!it.isBooked()}
    }
}
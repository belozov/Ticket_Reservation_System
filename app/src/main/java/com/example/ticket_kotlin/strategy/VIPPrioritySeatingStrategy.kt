package com.example.ticket_kotlin.strategy

class VIPPrioritySeatingStrategy : SeatingStrategy{
    override fun chooseSeat(seat: Array<Seat>): Seat? {
        val vipZone = seat.size / 4
        return seat
            .take(vipZone)
            .firstOrNull{ !it.isBooked() }
            ?: seat
                .drop(vipZone)
                .firstOrNull{ !it.isBooked() }
    }
}
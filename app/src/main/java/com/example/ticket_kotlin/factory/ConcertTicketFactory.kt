package com.example.ticket_kotlin.factory

import com.example.ticket_kotlin.builder.Ticket
import com.example.ticket_kotlin.strategy.Seat

class ConcertTicketFactory : TicketFactory
{
    override val capacity = 40

    override fun createSeats(): Array<Seat> =
        Array(capacity){i->
            Seat(
                i+1,
                if(i <= 10) "VIP" else "Standart",
                if(i <= 10) 2000.0 else 0.0
            )
        }

    override fun create(
        userName: String,
        seatNumber: Int,
        bonus: Double
    ): Ticket {
        return Ticket.Builder()
            .setType(getEventType())
            .setPrice(getBasePrice() + bonus)
            .setUserName(userName)
            .setSeatNumber(seatNumber)
            .build()
    }

    override fun getBasePrice(): Double = 10000.0

    override fun getEventType(): String = "Concert"
}
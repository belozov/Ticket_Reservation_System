package com.example.ticket_kotlin.factory


import com.example.ticket_kotlin.builder.Ticket
import com.example.ticket_kotlin.strategy.Seat


class BusTicketFactory : TicketFactory{
    override val capacity = 20

    override fun createSeats(): Array<Seat> =
        Array(capacity){i ->
            Seat(
                i+1,
                if(i <=5) "VIP" else "Standart",
                if(i<=5) 20.0 else 0.0
            )
        }


    override fun create(
        userName: String,
        seatNumber: Int,
        bonus: Double
    ): Ticket {
        return Ticket.Builder()
            .setType(getEventType())
            .setUserName(userName)
            .setSeatNumber(seatNumber)
            .setPrice(getBasePrice() + bonus)
            .build()
    }
    override fun getBasePrice(): Double = 100.0
    override fun getEventType(): String = "Bus"
}
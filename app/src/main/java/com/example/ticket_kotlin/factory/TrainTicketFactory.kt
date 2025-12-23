package com.example.ticket_kotlin.factory

import com.example.ticket_kotlin.builder.Ticket
import com.example.ticket_kotlin.strategy.Seat

class TrainTicketFactory : TicketFactory
{
    override val capacity = 60
    override fun createSeats(): Array<Seat> =
        Array(capacity) { i ->
            Seat(
                number = i + 1,
                type = if (i < 10) "VIP" else "Standart",
                priceBonus = if (i < 10) 1000.0 else 0.0
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

    override fun getBasePrice(): Double = 5000.0
    override fun getEventType(): String = "Train"
}


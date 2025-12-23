package com.example.ticket_kotlin.factory

import com.example.ticket_kotlin.builder.Ticket
import com.example.ticket_kotlin.strategy.Seat

class MovieTicketFactory : TicketFactory {
    override val capacity = 30

    override fun createSeats(): Array<Seat> =
        Array(capacity){ i ->
            Seat(
                i+1,
                if(i <= 20) "VIP" else "Standart",
                if (i <= 20 ) 1000.0 else 0.0
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
            .setSeatNumber(seatNumber)
            .setUserName(userName)
            .build()
    }
    override fun getBasePrice(): Double = 1800.0
    override fun getEventType(): String = "Movie"

}
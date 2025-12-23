package com.example.ticket_kotlin.factory
import com.example.ticket_kotlin.builder.Ticket
import com.example.ticket_kotlin.strategy.Seat

class PlaneTicketFactory : TicketFactory {
    override val capacity = 200
    override fun createSeats(): Array<Seat> =
        Array(capacity){ i ->
            Seat(
                number= i + 1,
                type = if (i < 19) "VIP" else "Standart",
                priceBonus = if (i < 19) 2500.0 else 0.0
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
    override fun getBasePrice(): Double = 10000.0
    override fun getEventType(): String = "Plane"
}
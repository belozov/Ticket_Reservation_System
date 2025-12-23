package com.example.ticket.strategy

import com.example.ticket_kotlin.decorator.PaymentProcessor
import com.example.ticket_kotlin.builder.Ticket
import com.example.ticket_kotlin.factory.TicketFactory
import com.example.ticket_kotlin.strategy.Seat
import com.example.ticket_kotlin.strategy.SeatingStrategy


class TicketReservationSystem (
    seatCount: Int,
    private var strategy: SeatingStrategy,
    private val factory: TicketFactory,
    private val seats: Array<Seat>,
    private var payment: PaymentProcessor
)
{
    private var count = 0
    private var lastSeat : Seat? = null

    fun setStrategy(strategy: SeatingStrategy){
        this.strategy = strategy
    }
    fun setPaymentProcessor(payment: PaymentProcessor){
        this.payment = payment
    }
    fun reserve (user: String):Ticket?{
        val seat = strategy.chooseSeat(seats) ?: return null
        lastSeat = seat

        seat.setBooked(true)
        count++

        val finalPrice = factory.getBasePrice() + seat.priceBonus
        val paid = payment.pay(finalPrice, "KZT")

        if (!paid) {
            seat.setBooked(false)
            count--
            return null
        }
        return factory.create(user, seat.number, finalPrice)
    }
    fun getLastSeat():Seat? = lastSeat
    fun getCount() : Int = count
    fun getSeats() : Array<Seat> = seats
    fun cancel(seatNumber : Int){
        val seat = seats.firstOrNull{it.number == seatNumber} ?: return
        seat.unbook()
        count--
    }

}
package com.example.ticket_kotlin.facade

import com.example.ticket.strategy.TicketReservationSystem
import com.example.ticket_kotlin.builder.Ticket
import com.example.ticket_kotlin.decorator.CurrencyDecorator
import com.example.ticket_kotlin.decorator.DiscountDecorator
import com.example.ticket_kotlin.decorator.KZTPaymentDecorator
import com.example.ticket_kotlin.decorator.PaymentProcessor
import com.example.ticket_kotlin.factory.TicketFactory
import com.example.ticket_kotlin.strategy.Seat
import com.example.ticket_kotlin.strategy.SeatingStrategy

class BookingFacade(
    private val ticketFactory: TicketFactory,
    private val seatingStrategy: SeatingStrategy,
    private val seats: Array<Seat>
) {
    private val reservationSystem: TicketReservationSystem = TicketReservationSystem(
        seats.size,
        seatingStrategy,
        ticketFactory,
        seats,
        KZTPaymentDecorator()
    )
    fun bookTicket (
        userName : String,
        category : String,
        currency:String
    ) : Ticket? {
        reservationSystem.setPaymentProcessor(
            configurePayment(category, currency)
        )
        return reservationSystem.reserve(userName)
    }
    private fun configurePayment(
        category : String,
        currency : String
    ) : PaymentProcessor{
        var processor : PaymentProcessor = KZTPaymentDecorator()

        if(!category.equals("adult", ignoreCase = true)){
                processor = DiscountDecorator(processor, category)
        }
        if (currency.equals("USD", true)){
            processor = CurrencyDecorator(processor)
        }
        return processor

    }
    fun cancelTicket(ticket: Ticket) {
        reservationSystem.cancel(ticket.seatNumber)
    }


    fun getLastBookedSeat() : Seat? = reservationSystem.getLastSeat()
    fun getBookedCount() : Int = reservationSystem.getCount()
    fun getAllSeats() : Array<Seat> = seats
    fun setSeatingStrategy(strategy: SeatingStrategy) = reservationSystem.setStrategy(strategy)
}
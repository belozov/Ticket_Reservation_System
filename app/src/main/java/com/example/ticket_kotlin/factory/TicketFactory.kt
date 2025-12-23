package com.example.ticket_kotlin.factory

import com.example.ticket_kotlin.builder.Ticket
import com.example.ticket_kotlin.strategy.Seat

interface TicketFactory {

    val capacity: Int

    fun createSeats() : Array<Seat>
    fun create(userName: String, seatNumber: Int, bonus: Double) : Ticket
    fun getBasePrice() : Double
    fun getEventType() : String
}

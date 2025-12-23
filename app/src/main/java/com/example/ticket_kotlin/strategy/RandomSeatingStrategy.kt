package com.example.ticket_kotlin.strategy

import kotlin.random.Random

class RandomSeatingStrategy : SeatingStrategy {
    private val random = Random
    override fun chooseSeat(seat: Array<Seat>) : Seat? {
        val free = seat.filter{!it.isBooked()}.toMutableList()
        if(free.isEmpty()) return null
        return free[random.nextInt(free.size)]
    }
}
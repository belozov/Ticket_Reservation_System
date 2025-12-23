package com.example.ticket_kotlin.strategy

interface SeatingStrategy {
    fun chooseSeat(seat: Array<Seat>): Seat?

    fun printInfo() {
        println("Using: " + "${this::class.simpleName}")
    }
}
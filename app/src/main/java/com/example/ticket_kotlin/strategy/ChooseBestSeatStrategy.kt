package com.example.ticket_kotlin.strategy

class ChooseBestSeatStrategy : SeatingStrategy {
    override fun chooseSeat(seat: Array<Seat>): Seat? {
        val mid = seat.size / 2
        for (offset in 0 until seat.size){
            val left = mid - offset
            val right = mid + offset
            if (left >= 0 && !seat[left].isBooked()) return seat[left]
            if(right < seat.size && !seat[right].isBooked()) return seat[right]
        }
        return null
    }
}






















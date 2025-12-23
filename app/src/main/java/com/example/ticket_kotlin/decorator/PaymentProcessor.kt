package com.example.ticket_kotlin.decorator

interface PaymentProcessor {
    fun pay (amount: Double, currency:String) : Boolean
}
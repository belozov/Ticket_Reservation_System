package com.example.ticket_kotlin.decorator

class KZTPaymentDecorator  : PaymentProcessor  {
    override fun pay (amount : Double, currency : String): Boolean{
        println("Paying $amount $currency")
        return true
    }
}
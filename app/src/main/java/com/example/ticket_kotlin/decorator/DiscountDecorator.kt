package com.example.ticket_kotlin.decorator

class DiscountDecorator(
    private val wrappee: PaymentProcessor,
    category: String
) : PaymentProcessor {
    private val category = category.lowercase()

    override fun pay (amount:Double, currency:String) : Boolean{
        val discountRate = getDiscountRate()
        val discountAmount = amount * (1 - discountRate)
        println("Discount $category -> ${discountRate * 100}% off -> $discountAmount $currency")
        return wrappee.pay(discountAmount, currency)
    }


    private fun getDiscountRate(): Double =
        when(category){
            "child" -> 0.6
            "student","pensioner" -> 0.4
            else -> 0.0
        }
}
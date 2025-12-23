package com.example.ticket_kotlin.decorator

class CurrencyDecorator(private val wrappee: PaymentProcessor) : PaymentProcessor{
    companion object{
        private const val USD_TO_KZT = 520.0
    }

    override fun pay(amount: Double, currency: String): Boolean {
        val amountKZT : Double
        val logCurrency = currency.uppercase()

        if(logCurrency == "USD"){
            amountKZT = amount * USD_TO_KZT
            println("$amount USD -> $amountKZT KZT")
        }
        else {
            amountKZT = amount
            val usd = amount / USD_TO_KZT
            println("$amount KZT → ${"%.2f".format(usd)} USD")
        }
        return wrappee.pay(amountKZT,"KZT")
    }
}
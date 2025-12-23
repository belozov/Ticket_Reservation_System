package com.example.ticket_kotlin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticket_kotlin.builder.Ticket
import com.example.ticket_kotlin.decorator.KZTPaymentDecorator
import com.example.ticket_kotlin.facade.BookingFacade
import com.example.ticket_kotlin.factory.*
import com.example.ticket_kotlin.strategy.*
import com.example.ticket_kotlin.ui.theme.Ticket_KotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Ticket_KotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TicketBookingScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TicketBookingScreen(modifier: Modifier = Modifier) {

    var userName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("adult") }
    var selectedCurrency by remember { mutableStateOf("KZT") }
    var selectedFactory by remember { mutableStateOf("Concert") }
    var selectedStrategy by remember { mutableStateOf("FrontToBack") }

    var bookedTickets by remember { mutableStateOf(listOf<Ticket>()) }
    var lastTicket by remember { mutableStateOf<Ticket?>(null) }

    val factories = mapOf(
        "Concert" to ConcertTicketFactory(),
        "Movie" to MovieTicketFactory(),
        "Bus" to BusTicketFactory(),
        "Plane" to PlaneTicketFactory(),
        "Train" to TrainTicketFactory()
    )

    val strategies = mapOf(
        "FrontToBack" to FrontToBackSeatingStrategy(),
        "VIPPriority" to VIPPrioritySeatingStrategy(),
        "Random" to RandomSeatingStrategy(),
        "ChooseBest" to ChooseBestSeatStrategy()
    )


    val seats = remember(selectedFactory) {
        mutableStateListOf<Seat>().apply {
            addAll(factories[selectedFactory]!!.createSeats())
        }
    }

    
    val bookingFacade = remember(selectedFactory, selectedStrategy) {
        BookingFacade(
            ticketFactory = factories[selectedFactory]!!,
            seatingStrategy = strategies[selectedStrategy]!!,
            seats = seats.toTypedArray()
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Ticket Booking System", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("User Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        DropdownSelector("Category", listOf("adult", "child", "student", "pensioner"), selectedCategory) {
            selectedCategory = it
        }

        Spacer(Modifier.height(8.dp))
        DropdownSelector("Currency", listOf("KZT", "USD"), selectedCurrency) {
            selectedCurrency = it
        }

        Spacer(Modifier.height(8.dp))
        DropdownSelector("Event Type", factories.keys.toList(), selectedFactory) {
            selectedFactory = it
            bookedTickets = emptyList()
            lastTicket = null
        }

        Spacer(Modifier.height(8.dp))
        DropdownSelector("Seating Strategy", strategies.keys.toList(), selectedStrategy) {
            selectedStrategy = it
        }

        Spacer(Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val ticket = bookingFacade.bookTicket(
                    userName,
                    selectedCategory,
                    selectedCurrency
                )
                ticket?.let {
                    bookedTickets = bookedTickets + it
                    lastTicket = it
                }
            }
        ) {
            Text("Book Ticket")
        }
        LazyColumn {
            items(bookedTickets, key = { it.seatNumber }) { ticket ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("User: ${ticket.userName}", fontWeight = FontWeight.Bold)
                            Text("Seat: ${ticket.seatNumber}")
                            Text("Type: ${ticket.type}")
                            Text("Price: ${ticket.price}₸")
                        }

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            ),
                            onClick = {
                                bookingFacade.cancelTicket(ticket)

                                bookedTickets =
                                    bookedTickets.filterNot { it.seatNumber == ticket.seatNumber }

                                if (lastTicket?.seatNumber == ticket.seatNumber) {
                                    lastTicket = null
                                }
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
        lastTicket?.let { ticket ->

            val basePrice =
                factories[ticket.type]?.getBasePrice() ?: ticket.price

            val seatBonus =
                seats.first { it.number == ticket.seatNumber }.priceBonus

            val subtotal = basePrice + seatBonus

            val discountRate = when (selectedCategory.lowercase()) {
                "child" -> 0.6
                "student", "pensioner" -> 0.4
                else -> 0.0
            }

            val discountAmount = subtotal * discountRate
            val finalPrice = subtotal - discountAmount

            val displayPrice =
                if (selectedCurrency.equals("USD", true)) {
                    String.format("%.2f USD", finalPrice / 520.0)
                } else {
                    "$finalPrice KZT"
                }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD1C4E9)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Last Ticket Price", fontWeight = FontWeight.Bold)
                    Text("Base price: $basePrice ₸")
                    Text("Seat bonus: $seatBonus ₸")
                    Text("Discount: $discountAmount ₸")
                    Divider()
                    Text(
                        "Final price: $displayPrice",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }


        Spacer(Modifier.height(16.dp))

        Text("Available Seats", fontWeight = FontWeight.Bold)

        LazyColumn {
            items(seats, key = { it.number }) { seat ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .border(
                            1.dp,
                            if (seat.isBooked()) Color.Red else Color.Green,
                            RoundedCornerShape(4.dp)
                        )
                        .background(if (seat.isBooked()) Color(0xFFFFCDD2) else Color(0xFFC8E6C9))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Seat ${seat.number} (${seat.type})")
                    Text(if (seat.isBooked()) "Booked" else "Free", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Booked Tickets", fontWeight = FontWeight.Bold)

        LazyColumn {
            items(bookedTickets) { ticket ->
                Text(
                    "Seat ${ticket.seatNumber} | ${ticket.type} | ${ticket.price}₸",
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

enum class Coolness(val percent: Double) {
    SUCKS(0.3),
    FINE(0.6),
    COOL(1.0)
}

enum class IndividualTransportType(val coolness: Coolness) {
    CLASSIC(Coolness.SUCKS),
    STUNT(Coolness.COOL),
    SPORT(Coolness.COOL)
}


sealed interface Transport {
    val coolness: Coolness

    val avgSpeed: Int
}


sealed class IndividualTransport(val type: IndividualTransportType): Transport {
    override val coolness = if (type.coolness == Coolness.COOL) {
        Coolness.FINE
    } else {
        Coolness.SUCKS
    }

    protected val speedBoost: Int = if (type == IndividualTransportType.SPORT) {
        5
    } else {
        0
    }

    fun getTimeOfRide(length: Int): Double = length.toDouble() / avgSpeed
}



class Scooter(type: IndividualTransportType): IndividualTransport(type) {
    init {
        println("Scooter is created")
    }

    override val avgSpeed: Int = 15 + speedBoost
}

class Bike(type: IndividualTransportType): IndividualTransport(type) {
    init {
        println("Bike is created")
    }

    override val avgSpeed: Int = 20 + speedBoost
}



sealed class PublicTransport(private val routeLength: Int): Transport {
    protected abstract val capacity: Int

    protected abstract val avgFare: Int

    override val coolness = Coolness.FINE



    fun getTimeOfRide() = routeLength.toDouble() / avgSpeed

    protected fun getProfitFromOneRide() = avgFare * capacity
}

class Bus(routeLength: Int): PublicTransport(routeLength) {

    init {
        println("Bus is created")
    }

    override val capacity: Int = 70

    override val avgFare: Int = 60

    override val avgSpeed: Int = 40

}

class Train(routeLength: Int): PublicTransport(routeLength) {

    init {
        println("Train is created")
    }

    override val capacity: Int = 300

    override val avgFare: Int = 1000

    override val avgSpeed: Int = 70
}

class Airplane(routeLength: Int): PublicTransport(routeLength) {

    init {
        println("Airplane is created")
    }

    override val capacity: Int = 1500

    override val avgFare: Int = 5000

    override val avgSpeed: Int = 800
}


fun logIndividualTransport(transport: IndividualTransport, distance: Int) = when(transport) {
    is Scooter -> println("Travel time: " +
            "${String.format("%.3f", transport.getTimeOfRide(distance))} hours on the scooter")
    is Bike -> println("Travel time: " +
            "${String.format("%.3f", transport.getTimeOfRide(distance))} hours on the bike")
}

fun logPublicTransport(transport: PublicTransport) = when(transport) {
    is Bus -> println("Travel time: " +
            "${String.format("%.3f", transport.getTimeOfRide())} hours on the bus")
    is Train -> println("Travel time: " +
            "${String.format("%.3f", transport.getTimeOfRide())} hours on the train")
    is Airplane -> println("Travel time: " +
            "${String.format("%.3f", transport.getTimeOfRide())} hours on the plane")
}

fun logMain(transport: Transport, distance: Int) = when(transport) {
    is IndividualTransport -> logIndividualTransport(transport, distance)
    is PublicTransport -> logPublicTransport(transport)
}

fun showMenu(menu: List<String>) {
    var index = 0
    while (index < menu.size) {
        println("${index + 1}. ${menu[index++]}")
    }
}

fun getOption(maxVal: Int, minVal: Int = 0): Int {
    while (true) {
        val option = readln().toIntOrNull()
        if (option != null && option > minVal && option <= maxVal) {
            return option
        }
        println("Input error! Enter value between $minVal and $maxVal")
    }
}

fun main() {
    val typeOfTransport = listOf("Public transport", "Individual transport")
    val typeOfPublicTransport = listOf("Bus", "Train", "plane")
    val typeOfIndividualTransport = listOf("Scooter", "Bike")
    val typeOf = listOf("Classic", "Sport", "Stunt")

    println("Enter distance you want to move:")
    val distance: Int = getOption(Int.MAX_VALUE)

    var transport: Transport = Bike(IndividualTransportType.CLASSIC)

    println("Choose your type of transport:")
    showMenu(typeOfTransport)


    when(getOption(2)) {
        1 -> {
            println("Choose type of public transport:")
            showMenu(typeOfPublicTransport)

            when (getOption(3)) {
                1 -> transport = Bus(distance)
                2 -> transport = Train(distance)
                3 -> transport = Airplane(distance)
            }
        }
        2 -> {
            println("Choose type:")
            showMenu(typeOf)

            val optionType: Int = getOption(3)

            println("Choose type of individual transport:")
            showMenu(typeOfIndividualTransport)

            when (getOption(2)) {
                1 -> transport = Scooter(IndividualTransportType.values()[optionType - 1])
                2 -> transport = Bike(IndividualTransportType.values()[optionType - 1])
            }
        }
    }
    logMain(transport, distance)
}


package ufc.comp.ed

import java.util.*

fun interface OrderedFileMaintenance : (Command) -> String?

object Main {
    private val refImplementation = StateManager<TreeSet<Int>, String?> { command ->
        println("WARNING: The data structure used here is a TreeSet from Java's standard library.")

        when (command) {
            is Command.Insert -> { state -> state.add(command.number); null }
            is Command.Remove -> { state -> state.remove(command.number); null }
            is Command.Successor -> { state ->
                val result = state.higher(command.number)

                requireNotNull(result) { "No successor for ${command.number}" }

                result.let(Int::toString)
            }

            is Command.Show -> { state -> state.joinToString(" ") }
        }
    }[TreeSet()].let(::OrderedFileMaintenance)

    @JvmStatic
    fun main(args: Array<String>) {
        val inputFileName = args[1]
        val outputFileName = args[2]

        val orderedFileMaintenance = when (val implementation = args[0]) {
            "homework" -> TODO("Implement the homework")
            "ref" -> refImplementation
            else -> throw IllegalArgumentException("Invalid implementation: $implementation")
        }

        IO(inputFileName, outputFileName, orderedFileMaintenance)
    }
}
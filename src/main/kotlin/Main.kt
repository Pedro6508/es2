package ufc.comp.ed

import java.util.*

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputFileName = args[1]
        val outputFileName = args[2]

        val (stateManager, state) = when (val implementation = args[0]) {
            "homework" -> TODO("Implement the homework")
            "ref" -> {
                println("WARNING: The data structure used here is a TreeSet from Java's standard library.")

                StateManagerJavaUtilsTreeSetImpl to TreeSet<Int>()
            }

            else -> throw IllegalArgumentException("Invalid implementation: $implementation")
        }

        IO(inputFileName, outputFileName, stateManager[state])
    }
}
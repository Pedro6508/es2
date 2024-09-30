package ufc.comp.ed

import java.util.*

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputFileName = args[1]
        val outputFileName = args[2]

        when (val implementation = args[0]) {
            "homework" -> {
                val (stateManager, state) = OrderedFileMaintenanceStateManagerImpl to OrderedFileMaintenance(10)

                IO(inputFileName, outputFileName, stateManager[state])
            }

            "ref" -> {
                println("WARNING: The data structure used here is a TreeSet from Java's standard library.")

                val (stateManager, state) = StateManagerJavaUtilsTreeSetImpl to TreeSet<Int>()

                IO(inputFileName, outputFileName, stateManager[state])
            }

            else -> throw IllegalArgumentException("Invalid implementation: $implementation")
        }

    }
}
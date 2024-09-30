package ufc.comp.ed

import java.util.*

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputFileName = args[1]
        val outputFileName = args[2]
        val stateManager = OrderedFileMaintenanceStateManagerImpl

        when (val tag = args[0]) {
            "homework" -> {
                val state = OrderedFileMaintenance.Homework(10)

                IO(inputFileName, outputFileName, tag, stateManager[state])
            }

            "ref" -> {
                println("WARNING: The data structure used here is a TreeSet from Java's standard library.")
                val state = OrderedFileMaintenance.JavaStdLib(TreeSet<Int>())

                IO(inputFileName, outputFileName, tag, stateManager[state])
            }

            "dev" -> {
                println("WARNING: The output will not follow the expected format.")
                val reference = OrderedFileMaintenance.JavaStdLib(TreeSet<Int>())
                val homework = OrderedFileMaintenance.Homework(10)

                IO(inputFileName, outputFileName, tag) { index, command ->
                    val refResult = stateManager[reference](command) to reference.getElements()
                    val hwResult = stateManager[homework](command) to homework.getElements()
                    val result = hwResult.run { refResult.first == first && refResult.second == second.filterNotNull() }

                    """
                        | ----------------------------------------
                        | [Tag: $tag] Command #$index
                        | Operation: $command
                        | Reference:
                        |   output: ${refResult.first}
                        |   elements: ${refResult.second}
                        | Homework: 
                        |   output: ${hwResult.first}
                        |   elements: ${hwResult.second}
                        | result: ${if (result) "OK" else "FAIL"}
                        | ----------------------------------------
                    """.replaceIndentByMargin(
                        newIndent = if (result) "" else (">".repeat(5)),
                        marginPrefix = "|"
                    )
                }
            }

            else -> throw IllegalArgumentException("Invalid tag: $tag")
        }

    }
}
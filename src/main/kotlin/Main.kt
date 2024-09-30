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
                println("WARNING: The output does not follow the expected format.")
                val reference = OrderedFileMaintenance.JavaStdLib(TreeSet<Int>())
                val homework = OrderedFileMaintenance.Homework(10)

                IO(inputFileName, outputFileName, tag) { index, command ->
                    val refResult = stateManager[reference](command)
                    val hwResult = stateManager[homework](command)
                    val result = refResult == hwResult

                    """
                        | ----------------------------------------
                        | [Tag: $tag] Command #$index
                        | Operation: $command
                        | Reference: $refResult
                        | Homework: $hwResult
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
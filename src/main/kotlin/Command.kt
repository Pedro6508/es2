package ufc.comp.ed

sealed interface Command {
    data class Insert(val number: Int) : Command
    data class Remove(val number: Int) : Command
    data class Successor(val number: Int) : Command
    data object Show : Command

    companion object {
        private val incPattern = Regex("""^INC (\d+)$""")
        private val remPattern = Regex("""^REM (\d+)$""")
        private val sucPattern = Regex("""^SUC (\d+)$""")
        private val impPattern = Regex("""^IMP$""")

        fun fromLine(line: String): Command {
            return when {
                incPattern.matches(line) -> {
                    val number = incPattern.find(line)!!.groupValues[1].toInt()
                    Insert(number)
                }

                remPattern.matches(line) -> {
                    val number = remPattern.find(line)!!.groupValues[1].toInt()
                    Remove(number)
                }

                sucPattern.matches(line) -> {
                    val number = sucPattern.find(line)!!.groupValues[1].toInt()
                    Successor(number)
                }

                impPattern.matches(line) -> Show
                else -> throw IllegalArgumentException("Invalid command: $line")
            }
        }
    }
}
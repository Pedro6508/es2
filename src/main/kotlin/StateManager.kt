package ufc.comp.ed

import java.util.*

fun interface StateManager<State, SideEffect> : (Command) -> (State) -> SideEffect {
    operator fun get(state: State): (Command) -> SideEffect = { command -> this(command)(state) }
}

val StateManagerJavaUtilsTreeSetImpl = StateManager<TreeSet<Int>, String?> { command ->
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
}

val OrderedFileMaintenanceStateManagerImpl = StateManager<OrderedFileMaintenance, String?> { command ->
    when (command) {
        is Command.Insert -> { state -> state.insert(command.number); null }
        is Command.Remove -> { state -> state.remove(command.number); null }
        is Command.Successor -> { state ->
            val result = state.next(command.number)

            requireNotNull(result) { "No successor for ${command.number}" }

            result.let(Int::toString)
        }

        is Command.Show -> { state -> state.getElements().filterNotNull().joinToString(" ") }
    }
}
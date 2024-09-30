package ufc.comp.ed

fun interface StateManager<State, SideEffect> : (Command) -> (State) -> SideEffect {
    operator fun get(state: State): (Command) -> SideEffect = { command -> this(command)(state) }
}
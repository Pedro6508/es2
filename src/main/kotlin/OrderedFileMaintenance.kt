package ufc.comp.ed

interface OrderedFileMaintenance {
    fun insert(element: Int)
    fun remove(element: Int)
    fun next(element: Int): Int?
    fun getElements(): List<Int?>
}

fun OrderedFileMaintenance(capacity: Int) = object : OrderedFileMaintenance {
    private val elements = mutableListOf<Int?>()
        .apply { repeat(capacity) { add(null) } }
    private val maxDensity = 0.75
    private val minDensity = 0.25

    override fun insert(element: Int) {
        val index = elements.binarySearch(element)
        if (index >= 0) return // Element already exists

        val insertIndex = -index - 1
        if (elements[insertIndex] == null) {
            elements[insertIndex] = element
        } else {
            redistribute(insertIndex, element)
        }
    }

    // TODO: "Adjust this implementation"
    override fun remove(element: Int) {
        val index = elements.binarySearch(element)
        if (index < 0) return // Element does not exist

        elements[index] = null
    }

    // TODO: "Adjust this implementation"
    override fun next(element: Int): Int? = element.let(elements::binarySearch).let { index ->
        if (index >= 0) element
        else elements.subList(-index - 1, elements.size).firstOrNull { it != null }
    }

    private fun redistribute(insertIndex: Int, element: Int) {
        // Find the nearest empty spot within O(log n) range
        var left = insertIndex
        var right = insertIndex

        while (left >= 0 && elements[left] != null) left--
        while (right < elements.size && elements[right] != null) right++

        when {
            left >= 0 && elements[left] == null -> elements[left] = element
            right < elements.size && elements[right] == null -> elements[right] = element
            else -> {
                // If no space found, perform table doubling and redistribute
                tableDoubling()
                insert(element)
            }
        }
    }

    private fun tableDoubling() {
        val newElements = MutableList(elements.size * 2) { null as Int? }
        var j = 0
        for (i in elements.indices)
            if (elements[i] != null) {
                newElements[j] = elements[i]
                j++
            }

        elements.clear()
        elements.addAll(newElements)
    }

    override fun getElements(): List<Int?> = elements.toList()
}
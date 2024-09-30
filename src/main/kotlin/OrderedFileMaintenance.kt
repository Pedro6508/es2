package ufc.comp.ed

interface OrderedFileMaintenance {
    fun insert(element: Int)
    fun remove(element: Int)
    fun next(element: Int): Int?
    fun getElements(): List<Int?>
}

fun OrderedFileMaintenance(capacity: Int): OrderedFileMaintenance =
    object : OrderedFileMaintenance, MutableList<Int?> by mutableListOf<Int?>().apply({
        repeat(capacity) { add(null) }
    }) {
        private val maxDensity = 0.75
        private val minDensity = 0.25

        fun binarySearch(element: Int) = binarySearch(element, fromIndex = 0, toIndex = capacity - 1)

        override fun insert(element: Int) {
            val index = binarySearch(element)
            if (index >= 0) return // Element already exists

            val insertIndex = -index - 1
            if (this[insertIndex] == null) {
                this[insertIndex] = element
            } else {
                redistribute(insertIndex, element)
            }
        }

        // TODO: "Adjust this implementation"
        override fun remove(element: Int) {
            val index = binarySearch(element)
            if (index < 0) return // Element does not exist

            this[index] = null
        }

        // TODO: "Adjust this implementation"
        override fun next(element: Int): Int? = element.let(::binarySearch).let { index ->
            if (index >= 0) element
            else subList(-index - 1, size).firstOrNull { it != null }
        }

        private fun redistribute(insertIndex: Int, element: Int) {
            // Find the nearest empty spot within O(log n) range
            var left = insertIndex
            var right = insertIndex

            while (left >= 0 && this[left] != null) left--
            while (right < this.size && this[right] != null) right++

            when {
                left >= 0 && this[left] == null -> this[left] = element
                right < this.size && this[right] == null -> this[right] = element
                else -> {
                    // If no space found, perform table doubling and redistribute
                    tableDoubling()
                    insert(element)
                }
            }
        }

        private fun tableDoubling() {
            val newElements = MutableList(this.size * 2) { null as Int? }
            var j = 0
            for (i in this.indices)
                if (this[i] != null) {
                    newElements[j] = this[i]
                    j++
                }

            this.clear()
            this.addAll(newElements)
        }

        override fun getElements(): List<Int?> = this.toList()
    }
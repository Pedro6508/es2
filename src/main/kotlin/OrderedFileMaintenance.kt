package ufc.comp.ed

import java.util.*

sealed interface OrderedFileMaintenance {
    fun insert(element: Int)
    fun remove(element: Int)
    fun next(element: Int): Int?
    fun getElements(): List<Int?>

    class JavaStdLib(private val treeSet: TreeSet<Int>) : OrderedFileMaintenance {
        override fun insert(element: Int) {
            treeSet.add(element)
        }

        override fun remove(element: Int) {
            treeSet.remove(element)
        }

        override fun next(element: Int): Int? = treeSet.higher(element)

        override fun getElements(): List<Int?> = treeSet.toList()
    }

    data class Homework(private var capacity: Int) : OrderedFileMaintenance {
        private var elements = arrayOfNulls<Int?>(capacity)

        override fun insert(element: Int) {
            val index = binarySearch(element)
            if (index >= 0) return // Element already exists

            val insertIndex = -index - 1

            redistribute(insertIndex, element)
        }

        override fun getElements(): List<Int?> = elements.toList()

        // TODO: "Adjust elements implementation"
        override fun remove(element: Int) {
            val index = binarySearch(element)
            if (index < 0) return // Element does not exist

            elements[index] = null
        }

        // TODO: "Adjust elements implementation"
        override fun next(element: Int): Int? = element.let(::binarySearch).let { index ->
            if (index >= 0) element
            else {
                val insertIndex = -index - 1
                if (insertIndex < capacity) elements[insertIndex]
                else null
            }
        }

        private val maxDensity = 0.75
        private val minDensity = 0.25

        private fun binarySearch(element: Int): Int {
            var low = 0
            var high = capacity - 1

            while (low <= high) {
                val mid = (low + high) ushr 1
                val midVal = elements[mid]

                when {
                    midVal == null -> high = mid - 1
                    midVal < element -> low = mid + 1
                    midVal > element -> high = mid - 1
                    else -> return mid // element found
                }
            }
            return -(low + 1) // element not found
        }

        private fun redistribute(insertIndex: Int, element: Int) {
            // Find the nearest empty spot within O(log n) range
            var left = insertIndex
            var right = insertIndex

            while (left >= 0 && elements[left] != null) left--
            while (right < capacity && elements[right] != null) right++

            when {
                left >= 0 && elements[left] == null -> elements[left] = element
                right < capacity && elements[right] == null -> elements[right] = element
                else -> {
                    // If no space found, perform table doubling and redistribute
                    tableDoubling()
                    insert(element)
                }
            }
        }

        private fun tableDoubling() {
            capacity *= 2

            val newElements = MutableList(capacity) { null as Int? }
            var j = 0
            for (i in elements.indices)
                if (elements[i] != null) {
                    newElements[j] = elements[i]
                    j++
                }

            elements = elements.copyOf(capacity)
        }

    }
}
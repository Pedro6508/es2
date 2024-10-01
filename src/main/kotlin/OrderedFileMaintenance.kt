package ufc.comp.ed

import java.util.*
import kotlin.math.log2

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
        private var elements = Array<Int>(capacity) { Int.MIN_VALUE }
        private var chunkSize = log2(capacity.toDouble()).toInt()

        override fun insert(element: Int) {
            val index = binarySearch(element)
            if (index >= 0) return // Element already exists

            val insertIndex = -index - 1
            if (elements[insertIndex] == Int.MIN_VALUE) {
                elements[insertIndex] = element
                // TODO: Adjust density
            } else {
                redistribute(insertIndex, element)
            }

        }

        override fun getElements(): List<Int?> = elements.map { num -> num.takeIf { num > Int.MIN_VALUE } }

        // TODO: "Adjust elements implementation"
        override fun remove(element: Int) {
            val index = binarySearch(element)
            if (index < 0) return // Element does not exist

            elements[index] = Int.MIN_VALUE
        }

        // TODO: "Adjust elements implementation"
        override fun next(element: Int): Int? = binarySearch(element).let { index ->
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
            val low = 0
            val high = capacity - 1

            elements.binarySearch(element, low, high)

            return -(low + 1) // element not found
        }

        private val List<Int>.density get() = count { it != Int.MIN_VALUE }.toDouble() / this.size

        private fun redistribute(insertIndex: Int, element: Int) {
            // Find the nearest empty spot within O(log n) range

            elements = elements.asSequence().toList()
                .chunked(chunkSize)
                .mapIndexed { index, chunk ->
                    if (insertIndex / chunkSize in index..index + 1) chunk + element
                    else chunk
                }.flatMap { chunk ->
                    if (chunk.density >= maxDensity)
                        chunk.chunked(chunk.size)
                    else listOf(chunk)
                }.reduce { chunkA, chunkB ->
                    if (chunkA.density + chunkB.density in minDensity..maxDensity)
                        chunkA + chunkB
                    else chunkA
                }.also { it.density in minDensity..maxDensity }
                .toTypedArray().let { newElements ->
                    capacity = log2(newElements.size.toDouble()).toInt() + 1

                    Array(capacity) { i -> newElements.getOrNull(i).takeUnless(Int.MIN_VALUE::equals) ?: Int.MIN_VALUE }
                }
        }

        private fun tableDoubling() {
            capacity *= 2

            elements = Array(capacity) { i ->
                elements.getOrNull(i)
                    .takeUnless(Int.MIN_VALUE::equals)
                    ?: Int.MIN_VALUE
            }
        }

    }
}
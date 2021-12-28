package day06

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day06Test {

    @Test
    fun run80() {
        assertEquals(5934, Day06.countLanternfishIndividually(80))
    }

    @Disabled("Too slow!")
    @Test
    fun run256() {
        assertEquals(26984457539, Day06.countLanternfishIndividually(256))
    }

    @Test
    fun run80Parallelized() {
        assertEquals(5934, Day06.countLanternfishIndividuallyParallelized(80))
    }

    @Disabled("Still too slow!")
    @Test
    fun run256Parallelized() {
        assertEquals(26984457539, Day06.countLanternfishIndividuallyParallelized(256))
    }

    @Test
    fun run80Fast() {
        assertEquals(5934, Day06.countLanternfishGroups(80))
    }

    @Test
    fun run256Fast() {
        assertEquals(26984457539, Day06.countLanternfishGroups(256))
    }
}
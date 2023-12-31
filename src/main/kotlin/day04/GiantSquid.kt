package day04

import PuzzleData

data class Coordinate(val x: Int, val y: Int) {

    private fun north(): Coordinate = Coordinate(x + 1, y)
    private fun east(): Coordinate = Coordinate(x, y + 1)
    private fun south(): Coordinate = Coordinate(x - 1, y)
    private fun west(): Coordinate = Coordinate(x, y - 1)
    fun directNeighbours(): Set<Coordinate> = setOf(north(), east(), south(), west())

    private fun northWest(): Coordinate = Coordinate(x + 1, y - 1)
    private fun northEast(): Coordinate = Coordinate(x + 1, y + 1)
    private fun southEast(): Coordinate = Coordinate(x - 1, y + 1)
    private fun southWest(): Coordinate = Coordinate(x - 1, y - 1)
    fun allNeighbours(): Set<Coordinate> = setOf(
        north(), east(), south(), west(), northWest(), northEast(), southEast(), southWest()
    )
}

class Board(private val state: Map<Coordinate, Int>, numbers: List<Int>, private val size: Int) {

    val bingo: Boolean
    val usedNumbers: List<Int>
    private val markedCoordinates: Set<Coordinate>

    init {
        val (c, n, b) = process(numbers, setOf(), listOf())
        markedCoordinates = c
        usedNumbers = n
        bingo = b
    }

    fun sumOfUnmarkedNumbers(): Int =
        state.filterKeys { !markedCoordinates.contains(it) }.values.sum()

    private fun process(
        remainingNumbers: List<Int>, marked: Set<Coordinate>, usedNumbers: List<Int>
    ): Triple<Set<Coordinate>, List<Int>, Boolean> =
        if (bingo(marked)) Triple(marked, usedNumbers, true)
        else if (remainingNumbers.isEmpty()) Triple(marked, usedNumbers, false)
        else {
            val nextNumber = remainingNumbers[0]
            val filterValues = state.filterValues { it == nextNumber }.keys
            process(remainingNumbers.drop(1), marked + filterValues, usedNumbers + nextNumber)
        }

    private fun bingo(coordinates: Set<Coordinate>): Boolean = (0 until size).any { n ->
        coordinates.count { c -> c.x == n } == size || coordinates.count { c -> c.y == n } == size
    }
}

object GiantSquid : Runnable {

    private const val boardSize = 5
    private val boards = loadBoards()

    private fun loadBoards(): List<Board> {
        val numbers = PuzzleData.load("/day04/bingo-numbers.txt") { parseNumbers(it) }
        return PuzzleData.load("/day04/bingo-boards.txt") { parseBoards(it, numbers) }
    }

    fun getScoreOfFirstWinningBoard(): Int =
        getScore(boards.filter { it.bingo }.minByOrNull { it.usedNumbers.count() }!!)

    fun getScoreOfLastWinningBoard(): Int =
        getScore(boards.filter { it.bingo }.maxByOrNull { it.usedNumbers.count() }!!)

    private fun getScore(board: Board): Int =
        board.sumOfUnmarkedNumbers() * board.usedNumbers.last()

    private fun parseBoards(lines: List<String>, numbers: List<Int>): List<Board> =
        lines.filter { it.isNotEmpty() }.chunked(boardSize).map { linesToBoardInput(it) }.map { Board(it, numbers, boardSize) }

    private fun linesToBoardInput(lines: List<String>): Map<Coordinate, Int> {
        val values: List<List<Int>> = lines.map { line ->
            line.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        }
        return values.mapIndexed { x, vs -> vs.mapIndexed { y, v -> Coordinate(x, y) to v } }.flatten().toMap()
    }

    private fun parseNumbers(lines: List<String>): List<Int> = lines[0].split(",").map { it.toInt() }

    override fun run() {
        println("Day 4, bingo first: ${getScoreOfFirstWinningBoard()}")
        println("Day 4, bingo last: ${getScoreOfLastWinningBoard()}")
    }
}

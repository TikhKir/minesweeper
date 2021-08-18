package minesweeper

import minesweeper.Cell.*
import kotlin.random.Random


class Engine(
    private val weight: Int,
    private val height: Int
) {
    private val bombMatrix: Array<Array<Bomb>> = Array(height) { Array(weight) { CLEAR } }
    private val markMatrix: Array<Array<Mark>> = Array(height) { Array(weight) { CLEAR } }
    private val hintMatrix: Array<Array<Hint>> = Array(height) { Array(weight) { HIDDEN } }
    private var bombsCount: Int = 1
    private var isAlive = true

    fun generateBombs(bombsCount: Int, startX: Int, startY: Int) {
        this.bombsCount = bombsCount
        val cellCount = weight * height
        val actualBombCount = if (bombsCount > cellCount) cellCount - 1 else bombsCount
        val wrongFirstBomb = Pair(startX, startY)
        val bombsCoordList = mutableListOf<Pair<Int, Int>>()

        while (bombsCoordList.size < actualBombCount) {
            val newBombCoords = Pair(Random.nextInt(weight), Random.nextInt(height))
            if (newBombCoords !in bombsCoordList && newBombCoords != wrongFirstBomb)
                bombsCoordList.add(newBombCoords)
        }

        bombsCoordList.forEach { bombMatrix[it.first][it.second] = BOMBED }
    }

    fun markCell(x: Int, y: Int): Boolean =
        if (hintMatrix[x][y] != HIDDEN) false
        else {
            markMatrix[x][y] = if (markMatrix[x][y] == CLEAR) MARKED else CLEAR
            true
        }

    fun openCell(x: Int, y: Int): Boolean =
        if (bombMatrix[x][y] == BOMBED) {
            isAlive = false
            false
        } else {
            recursiveOpenCells(x, y)
            true
        }

    private fun recursiveOpenCells(x: Int, y: Int) {
        val topOffset = if (x == 0) 0 else 1
        val bottomOffset = if (x == height - 1) 0 else 1
        val leftOffset = if (y == 0) 0 else 1
        val rightOffset = if (y == weight - 1) 0 else 1

        var bombCounter = 0
        for (tempX in x - topOffset..x + bottomOffset)
            for (tempY in y - leftOffset..y + rightOffset)
                if (bombMatrix[tempX][tempY] == BOMBED) bombCounter++

        if (bombCounter > 0) {
            hintMatrix[x][y] = bombCounter.toHintDigit()
            markMatrix[x][y] = CLEAR
        } else {
            hintMatrix[x][y] = CLEAR
            markMatrix[x][y] = CLEAR
            for (tempX in x - topOffset..x + bottomOffset)
                for (tempY in y - leftOffset..y + rightOffset)
                    if (hintMatrix[tempX][tempY] == HIDDEN && bombMatrix[tempX][tempY] != BOMBED)
                        recursiveOpenCells(tempX, tempY)
        }
    }

    fun isWin(): Boolean {
        var closedCellsCount = 0
        var wrongMarksCount = 0

        bombMatrix.forMatrixIndices { x, y ->
            if (bombMatrix[x][y] == BOMBED && markMatrix[x][y] != MARKED) wrongMarksCount++
            if (bombMatrix[x][y] != BOMBED && markMatrix[x][y] == MARKED) wrongMarksCount++
            if (hintMatrix[x][y] == HIDDEN) closedCellsCount++
        }

        return closedCellsCount == bombsCount || wrongMarksCount == 0
    }

    fun getMergedMatrix(): Array<Array<Cell>> {
        val tempMatrix: Array<Array<Cell>> = Array(height) { Array(weight) { CLEAR } }

        tempMatrix.forMatrixIndices { x, y ->
            tempMatrix[x][y] = hintMatrix[x][y] as Cell
        }

        tempMatrix.forMatrixIndices { x, y ->
            if (markMatrix[x][y] == MARKED) tempMatrix[x][y] = MARKED
            if (!isAlive && bombMatrix[x][y] == BOMBED) tempMatrix[x][y] = BOMBED
        }

        return tempMatrix
    }


}
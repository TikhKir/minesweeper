package minesweeper

import kotlin.random.Random

class Engine(
    private val weight: Int,
    private val height: Int
) {
    private val bombMatrix: Array<Array<Char>> = Array(height) { Array(weight) { '.' } }
    private val markMatrix: Array<Array<Char>> = Array(height) { Array(weight) { '.' } }
    private val hintMatrix: Array<Array<Char>> = Array(height) { Array(weight) { '.' } }
    private var bombsCount: Int = 1
    private var isAlive = true

    fun generateBombs(bombsCount: Int, startX: Int, startY: Int) {
        this.bombsCount = bombsCount
        val count = if (bombsCount > weight * height) (weight * height) - 1 else bombsCount
        val wrongFirstBomb = Pair(startX, startY)
        val bombsCoordList = mutableListOf<Pair<Int, Int>>()

        while (bombsCoordList.size < count) {
            val newBombCoords = Pair(Random.nextInt(weight), Random.nextInt(height))
            if (!bombsCoordList.contains(newBombCoords) && newBombCoords != wrongFirstBomb)
                bombsCoordList.add(newBombCoords)
        }

        bombsCoordList.forEach { bombMatrix[it.first][it.second] = 'X' }
    }

    fun drawField() {
        val matrixWithHintAndMarks = mergeMatrixLayers()
        val newSizeX = hintMatrix.size + 3
        val newSizeY = hintMatrix.first().size + 3

        val bigMatrix = Array(newSizeX) { Array(newSizeY) { ' ' } }

        bigMatrix[1] = Array(newSizeX) { '-' }
        bigMatrix[newSizeX - 1] = Array(newSizeX) { '-' }

        bigMatrix.forEach { line ->
            line[1] = '|'
            line[newSizeX - 1] = '|'
        }

        for (x in 2 until newSizeX - 1) bigMatrix[x][0] = (x - 1).digitToChar()
        for (x in 2 until newSizeY - 1) bigMatrix[0][x] = (x - 1).digitToChar()

        for (x in 2 until newSizeX - 1)
            for (y in 2 until newSizeY - 1)
                bigMatrix[x][y] = matrixWithHintAndMarks[x - 2][y - 2]

        bigMatrix.forEach { row ->
            row.forEach {
                print(it)
            }
            println()
        }
    }

    fun markCell(x: Int, y: Int): Boolean {
        return if (hintMatrix[x][y] != '.') false
        else {
            markMatrix[x][y] = if (markMatrix[x][y] == '.') '*' else '.'
            true
        }
    }

    fun openCell(x: Int, y: Int): Boolean {
        return if (bombMatrix[x][y] == 'X') {
            isAlive = false
            false
        } else {
            recursiveOpenCells(x, y)
            true
        }
    }

    private fun recursiveOpenCells(x: Int, y: Int) {
        val topOffset = if (x == 0) 0 else 1
        val bottomOffset = if (x == height - 1) 0 else 1
        val leftOffset = if (y == 0) 0 else 1
        val rightOffset = if (y == weight - 1) 0 else 1

        var bombCounter = 0
        for (tempX in x - topOffset..x + bottomOffset)
            for (tempY in y - leftOffset..y + rightOffset)
                if (bombMatrix[tempX][tempY] == 'X') bombCounter++

        if (bombCounter > 0) {
            hintMatrix[x][y] = bombCounter.digitToChar()
            markMatrix[x][y] = '.'
        } else {
            hintMatrix[x][y] = '/'
            markMatrix[x][y] = '.'
            for (tempX in x - topOffset..x + bottomOffset)
                for (tempY in y - leftOffset..y + rightOffset)
                    if (hintMatrix[tempX][tempY] == '.' && bombMatrix[tempX][tempY] != 'X')
                        recursiveOpenCells(tempX, tempY)
        }
    }

    private fun mergeMatrixLayers(): Array<Array<Char>> {
        val tempMatrix = Array(height) { Array(weight) { '.' } }
        for (x in tempMatrix.indices) tempMatrix[x] = hintMatrix[x].copyOf()

        for (x in hintMatrix.indices)
            for (y in hintMatrix.indices) {
                if (markMatrix[x][y] == '*') tempMatrix[x][y] = '*'
                if (!isAlive && bombMatrix[x][y] == 'X') tempMatrix[x][y] = 'X'
            }

        return tempMatrix
    }

    fun isWin(): Boolean {
        var closedCellsCount = 0
        var wrongMarksCount = 0
        for (x in bombMatrix.indices)
            for (y in bombMatrix.indices) {
                if (bombMatrix[x][y] == 'X' && markMatrix[x][y] != '*') wrongMarksCount++
                if (bombMatrix[x][y] != 'X' && markMatrix[x][y] == '*') wrongMarksCount++
                if (hintMatrix[x][y] == '.') closedCellsCount++
            }

        return closedCellsCount == bombsCount || wrongMarksCount == 0
    }

}
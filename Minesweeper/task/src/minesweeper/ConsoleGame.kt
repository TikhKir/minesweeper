package minesweeper

class ConsoleGame(
    private val bombsCount: Int,
    fieldHeight: Int,
    fieldWeight: Int,
) {
    private val engine = Engine(fieldHeight, fieldWeight)
    private var isAlive = true
    private var bombsNotGenerated = true

    fun runGame() {
        drawWithBorders()

        while (bombsNotGenerated) {
            playersTurnIteration { x, y, isActionMark ->
                if (isActionMark) engine.markCell(x, y)
                else {
                    bombsNotGenerated = false
                    engine.generateBombs(bombsCount, x, y)
                    engine.openCell(x, y)
                }
            }
        }

        while (isAlive && !engine.isWin()) {
            playersTurnIteration { x, y, actionMark ->
                if (actionMark) engine.markCell(x, y)
                else isAlive = engine.openCell(x, y)
            }
        }

        if (engine.isWin()) println("Congratulations! You found all the mines!")
        else println("You stepped on a mine and failed!")
    }

    private fun drawWithBorders() {
        val matrixWithHintAndMarks = engine.getMergedMatrix()
        val newSizeX = matrixWithHintAndMarks.size + 3
        val newSizeY = matrixWithHintAndMarks.first().size + 3
        val borderedMatrix: Array<Array<Char>> = Array(newSizeX) { Array(newSizeY) { ' ' } }

        //create boards
        borderedMatrix[1] = Array(newSizeX) { '-' }
        borderedMatrix[newSizeX - 1] = Array(newSizeX) { '-' }
        borderedMatrix.forEach { line ->
            line[1] = '|'
            line[newSizeX - 1] = '|'
        }

        //create board numbers
        for (x in 2 until newSizeX - 1) borderedMatrix[x][0] = (x - 1).digitToChar()
        for (x in 2 until newSizeY - 1) borderedMatrix[0][x] = (x - 1).digitToChar()

        //paste merged game matrix inside bordered
        for (x in 2 until newSizeX - 1)
            for (y in 2 until newSizeY - 1)
                borderedMatrix[x][y] = matrixWithHintAndMarks[x - 2][y - 2].sym

        borderedMatrix.forEach { row ->
            row.forEach { print(it) }
            println()
        }
    }

    private inline fun playersTurnIteration(action: (x: Int, y: Int, actionMark: Boolean) -> Unit) {
        print("Set/unset mines marks or claim a cell as free: ")
        val input = readLine()!!
        val inputX = input.last { it.isDigit() }.digitToInt() - 1
        val inputY = input.first { it.isDigit() }.digitToInt() - 1
        val actionMark = "mine" in input

        action.invoke(inputX, inputY, actionMark)
        drawWithBorders()
    }
}
package minesweeper

class Game(
    private val bombsCount: Int,
    fieldHeight: Int,
    fieldWeight: Int,
) {
    private val engine = Engine(fieldHeight, fieldWeight)
    private var isAlive = true
    private var bombsNotGenerated = true

    fun runGame() {
        engine.drawWithBorders()

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

    private inline fun playersTurnIteration(action: (x: Int, y: Int, actionMark: Boolean) -> Unit) {
        print("Set/unset mines marks or claim a cell as free: ")
        val input = readLine()!!
        val inputX = input.last { it.isDigit() }.digitToInt() - 1
        val inputY = input.first { it.isDigit() }.digitToInt() - 1
        val actionMark = input.contains("mine")

        action.invoke(inputX, inputY, actionMark)
        engine.drawWithBorders()
    }
}
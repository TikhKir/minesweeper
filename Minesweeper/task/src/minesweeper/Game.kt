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
        engine.drawField()

        while (bombsNotGenerated) {
            print("Set/unset mines marks or claim a cell as free: ")
            val input = readLine()!!
            val inputX = input.last { it.isDigit() }.digitToInt() - 1
            val inputY = input.first { it.isDigit() }.digitToInt() - 1
            val actionMark = input.contains("mine")

            if (actionMark) {
                engine.markCell(inputX, inputY)
            } else {
                bombsNotGenerated = false
                engine.generateBombs(bombsCount, inputX, inputY)
                engine.openCell(inputX, inputY)
            }

            engine.drawField()
        }

        while (isAlive && !engine.isWin()) {
            print("Set/unset mines marks or claim a cell as free: ")
            val input = readLine()!!
            val inputX = input.last { it.isDigit() }.digitToInt() - 1
            val inputY = input.first { it.isDigit() }.digitToInt() - 1
            val actionMark = input.contains("mine")

            if (actionMark) engine.markCell(inputX, inputY)
            else isAlive = engine.openCell(inputX, inputY)
            engine.drawField()
        }

        if (engine.isWin()) println("Congratulations! You found all the mines!")
        else println("You stepped on a mine and failed!")
    }

}
package minesweeper

fun main() {
    print("How many mines do you want on the field? ")
    val minesCount = readLine()?.toInt() ?: 1

    val game = ConsoleGame(minesCount, 9, 9)
    game.runGame()
}











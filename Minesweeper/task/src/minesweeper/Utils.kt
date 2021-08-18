package minesweeper

sealed interface Hint
sealed interface Bomb
sealed interface Mark

sealed class Cell(val sym: Char) {
    object CLEAR : Cell('/'), Hint, Bomb, Mark
    object BOMBED : Cell('X'), Bomb
    object MARKED : Cell('*'), Mark
    object HIDDEN : Cell('.'), Hint
    object ONE : Cell('1'), Hint
    object TWO : Cell('2'), Hint
    object THREE : Cell('3'), Hint
    object FOUR : Cell('4'), Hint
    object FIVE : Cell('5'), Hint
    object SIX : Cell('6'), Hint
    object SEVEN : Cell('7'), Hint
    object EIGHT : Cell('8'), Hint
}

fun Int.toHintDigit(): Hint = when (this) {
    1 -> Cell.ONE
    2 -> Cell.TWO
    3 -> Cell.THREE
    4 -> Cell.FOUR
    5 -> Cell.FIVE
    6 -> Cell.SIX
    7 -> Cell.SEVEN
    8 -> Cell.EIGHT
    else -> throw IllegalArgumentException("wrong number")
}






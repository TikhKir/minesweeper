package minesweeper

enum class Nums(val value: Int) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8)
}

sealed class Cell {
    object Hidden

    sealed class BombCell : Cell() {
        object Bombed : BombCell()
    }

    sealed class MarkCell : Cell() {
        object Marked : MarkCell()
    }

    sealed class HintCell : Cell() {
        object Clear
        class Hinted(val number: Nums)
    }
}


package app

data class Position(
        val coll: Char,
        val row: Int
)

data class CellModel(
        val expression: String,
        val value: String,
        val hasError: Boolean
)

data class Expression(
        val col1: Char,
        val row1: Int,
        val col2: Char,
        val row2: Int,
        val operand: Char
)
package app

val emptyCell = CellModel(expression = "", value = "", hasError = false)

fun parseExpressionToModel(value: String): Expression {
    //убираем все пробелы
    val withoutSpaces = value.replace("\\s", "")

    //убираем знак равно
    val withoutEqually = withoutSpaces.substring(1)

    //по тупому берем каждый знак :)
    val col1 = withoutEqually[0].toUpperCase()
    val row1 = withoutEqually[1].toString().toIntOrNull()
    val operand = withoutEqually[2]
    val col2 = withoutEqually[3].toUpperCase()
    val row2 = withoutEqually[4].toString().toIntOrNull()
    return Expression(
            col1 = col1,
            row1 = row1 ?: Int.MIN_VALUE,
            col2 = col2,
            row2 = row2 ?: Int.MIN_VALUE,
            operand = operand
    )
}

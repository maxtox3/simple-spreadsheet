package app

import react.*
import react.dom.div
import react.dom.h1

interface AppState : RState {
    var rows: List<Int>
    var cols: List<Char>
    //key - position and value - value of cell
    var cells: MutableMap<Position, CellModel>
}

class App : RComponent<RProps, AppState>() {

    override fun AppState.init() {
        rows = (1..5).toList()
        cols = ('A'..'E').toList()
        cells = generateCells(rows, cols)
    }

    override fun RBuilder.render() {
        div("container") {
            h1("text-center") {
                +"SpreadSheet"
            }
            table(
                    cols = state.cols,
                    rows = state.rows,
                    cells = state.cells,
                    onExpressionEntered = ::onExpressionEntered
            )
        }
    }

    private fun onExpressionEntered(cell: CellModel, position: Position) {
        val map = state.cells

        //Если мы можем распарсить введенныое как число - ничего не надо делать, оставляем как число
        if (cell.expression.toIntOrNull() != null) {
            map[position] = cell.copy(value = cell.expression, expression = cell.expression)
            console.log(map[position])
            setState {
                cells = map
            }
            return
        }

        //если введено выражение начинающееся с =, то находим его значение
        if (cell.expression.indexOf("=") == 0) {

            var resultValue = 0.0
            val expressionModel = parseExpressionToModel(cell.expression)

            val cells = state.cells

            val value1 = cells.entries.firstOrNull {
                it.key == Position(expressionModel.col1, expressionModel.row1)
            }?.value?.value?.toDoubleOrNull()

            val value2 = cells.entries.firstOrNull {
                it.key == Position(expressionModel.col2, expressionModel.row2)
            }?.value?.value?.toDoubleOrNull()

            if (value1 != null && value2 != null) {
                resultValue = when (expressionModel.operand) {
                    '+' -> value1.plus(value2)
                    '-' -> value1.minus(value2)
                    '*' -> value1.times(value2)
                    '/' -> value1.div(value2)
                    else -> {
                        Double.MAX_VALUE
                    }
                }
            }
            console.log(resultValue)
            map[position] = if (resultValue != Double.MAX_VALUE) {
                cell.copy(hasError = false, value = resultValue.toString())
            } else {
                cell.copy(hasError = true)
            }

        } else {
            map[position] = cell.copy(hasError = true)
        }

        setState {
            cells = map
        }
    }

    private fun generateCells(rows: List<Int>, cols: List<Char>): MutableMap<Position, CellModel> {
        val cells = mutableMapOf<Position, CellModel>()
        rows.forEach { row ->
            cols.forEach { col ->
                cells[Position(col, row)] = emptyCell
            }
        }
        return cells
    }

}

fun RBuilder.app() = child(App::class) {}

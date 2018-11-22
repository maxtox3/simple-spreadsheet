package app

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

interface TableProps : RProps {
    var term: String
    var cols: List<Char>
    var rows: List<Int>
    var cells: Map<Position, CellModel>
    var onExpressionEntered: (CellModel, Position) -> Any

}

interface TableState : RState {
    var term: String
    var cols: List<Char>
    var rows: List<Int>
    var cells: Map<Position, CellModel>
}

class TableBar(props: TableProps) : RComponent<TableProps, TableState>(props) {

    override fun TableState.init(props: TableProps) {
        term = props.term
        cols = props.cols
        rows = props.rows
        cells = props.cells
    }

    override fun RBuilder.render() {
        console.log("render table")
        table(classes = "excel") {
            tbody {
                tr {
                    th {}
                    state.cols.forEach { column ->
                        th {
                            +column.toString()
                        }
                    }
                }

                state.rows.forEach { row ->
                    tr {
                        td {
                            +row.toString()
                        }
                        state.cols.forEach { col ->
                            cell(
                                    position = Position(coll = col, row = row),
                                    cell = state.cells[Position(col, row)] ?: emptyCell,
                                    onExpressionEntered = props.onExpressionEntered
                            )
                        }
                    }
                }
            }
        }
    }
}


fun RBuilder.table(
        cols: List<Char>,
        rows: List<Int>,
        cells: Map<Position, CellModel>,
        onExpressionEntered: (CellModel, Position) -> (Any)
) = child(TableBar::class) {
    attrs.cells = cells
    attrs.cols = cols
    attrs.rows = rows
    attrs.onExpressionEntered = onExpressionEntered
}

package app

import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.input
import react.dom.jsStyle
import react.dom.td

interface CellProps : RProps {
    var position: Position
    var cell: CellModel
    var onExpressionEntered: (CellModel, Position) -> (Any)
}

interface CellState : RState {
    var inputText: String
    var isInput: Boolean
}

class Cell(props: CellProps) : RComponent<CellProps, CellState>(props) {

    override fun CellState.init(props: CellProps) {
        inputText = props.cell.expression
        isInput = false
    }

    override fun RBuilder.render() {
        td {
            attrs.onClickFunction = { setState { isInput = true } }

            if (state.isInput) {
                input(classes = "cell_input") {
                    attrs.value = state.inputText
                    attrs.onChangeFunction = ::onInputChange
                    attrs.onBlurFunction = ::onBlurFromInput
                    attrs.autoFocus = true
                }
            }

            if (!state.isInput) {
                if (props.cell.hasError) {
                    attrs.jsStyle {
                        background = "rgb(255, 176, 176)"
                    }
                    +"ERR!!!"
                } else {
                    +props.cell.value
                }
            }

        }
    }

    private fun onInputChange(event: Event) {
        val target = event.target as HTMLInputElement
        val expression = target.value
        setState {
            isInput = true
            inputText = expression
        }
    }

    private fun onBlurFromInput(event: Event) {
        val target = event.target as HTMLInputElement
        val expression = target.value

        if (expression.isNotEmpty()) {
            val resultCell = props.cell.copy(expression = expression)
            this.props.onExpressionEntered(resultCell, props.position)
        }
        setState {
            isInput = false
        }
    }
}


fun RBuilder.cell(
        position: Position,
        cell: CellModel,
        onExpressionEntered: (CellModel, Position) -> (Any)
) = child(Cell::class) {
    attrs.position = position
    attrs.cell = cell
    attrs.onExpressionEntered = onExpressionEntered
}
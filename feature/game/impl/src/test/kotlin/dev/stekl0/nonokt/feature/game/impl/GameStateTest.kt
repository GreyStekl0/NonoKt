package dev.stekl0.nonokt.feature.game.impl

import dev.stekl0.nonokt.feature.game.api.GameLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameStateTest {
    @Test
    fun `state create uses square size for board hints and solving`() {
        val initialState = GameState.create(SquareLevel)

        assertEquals(SquareLevel.size, initialState.board.size)
        assertTrue(initialState.board.all { row -> row.size == SquareLevel.size })
        assertEquals(SquareLevel.size, initialState.rowHints.size)
        assertEquals(SquareLevel.size, initialState.columnHints.size)

        val solvedState =
            (0 until SquareLevel.size).fold(initialState) { state, row ->
                (0 until SquareLevel.size).fold(state) { currentState, column ->
                    if (SquareLevel.solution[row][column] == '1') {
                        currentState.onCellPressed(row = row, column = column)
                    } else {
                        currentState
                    }
                }
            }

        assertTrue(solvedState.isSolved)
    }

    @Test
    fun `correct fill marks the cell without adding an error`() {
        val state = GameState.create(DiagonalLevel).onCellPressed(row = 0, column = 0)

        assertEquals(PlayerCellState.FILLED, state.board[0][0])
        assertEquals(0, state.errorCount)
    }

    @Test
    fun `undo clears the board state but keeps the accumulated error count`() {
        val withError = GameState.create(DiagonalLevel).onCellPressed(row = 0, column = 1)
        val undone = withError.undo()
        val redone = undone.redo()

        assertEquals(PlayerCellState.ERROR, withError.board[0][1])
        assertEquals(1, withError.errorCount)

        assertEquals(PlayerCellState.EMPTY, undone.board[0][1])
        assertEquals(1, undone.errorCount)

        assertEquals(PlayerCellState.ERROR, redone.board[0][1])
        assertEquals(1, redone.errorCount)
    }

    @Test
    fun `generated hints keep grouped values and highlight fully filled lines`() {
        val state = GameState.create(CrossLevel)

        assertEquals(listOf(3), state.rowHints[0].values)
        assertTrue(state.rowHints[0].isFullyFilled)

        assertEquals(listOf(1), state.rowHints[1].values)
        assertFalse(state.rowHints[1].isFullyFilled)

        assertEquals(listOf(3), state.columnHints[0].values)
        assertTrue(state.columnHints[0].isFullyFilled)

        assertEquals(listOf(1, 1), state.columnHints[1].values)
        assertFalse(state.columnHints[1].isFullyFilled)
    }

    @Test
    fun `filling every required cell solves the puzzle`() {
        val solved =
            GameState
                .create(DiagonalLevel)
                .onCellPressed(row = 0, column = 0)
                .onCellPressed(row = 1, column = 1)

        assertTrue(solved.isSolved)
        assertFalse(solved.isFailed)
    }

    @Test
    fun `four mistakes stop further cell interaction`() {
        val failed =
            GameState
                .create(SparseLevel)
                .onCellPressed(row = 0, column = 1)
                .onCellPressed(row = 0, column = 2)
                .onCellPressed(row = 1, column = 0)
                .onCellPressed(row = 1, column = 1)

        val afterBlockedTap = failed.onCellPressed(row = 0, column = 0)

        assertTrue(failed.isFailed)
        assertEquals(GameLimits.MAX_ERROR_COUNT, failed.errorCount)
        assertEquals(failed.board, afterBlockedTap.board)
        assertEquals(failed.errorCount, afterBlockedTap.errorCount)
    }

    private companion object {
        val DiagonalLevel =
            GameLevel(
                id = "diagonal",
                solution =
                    listOf(
                        "10",
                        "01",
                    ),
            )

        val CrossLevel =
            GameLevel(
                id = "cross",
                solution =
                    listOf(
                        "111",
                        "100",
                        "111",
                    ),
            )

        val SparseLevel =
            GameLevel(
                id = "sparse",
                solution =
                    listOf(
                        "100",
                        "000",
                        "000",
                    ),
            )

        val SquareLevel =
            GameLevel(
                id = "square_4",
                solution =
                    listOf(
                        "1001",
                        "0110",
                        "0110",
                        "1001",
                    ),
            )
    }
}

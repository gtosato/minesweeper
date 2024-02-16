package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Game {
	private static final int BOARD_SIZE = 10;
	private static final int NUMBER_OF_MINES = 10;

	private Board board;
	private boolean gameover;

	public Game() {
		board = new Board(BOARD_SIZE);
		positionMines();
		gameover = false;
	}

	private void positionMines() {
		Random random = new Random();

		// loop until the number of mines to be planted is reached
		for (int i = 0; i < NUMBER_OF_MINES; i++) {
			int row, col;

			row = random.nextInt(BOARD_SIZE);
			col = random.nextInt(BOARD_SIZE);

			// continue looping each time until a spot is located
			// which doesn't already contain a mine
			while (board.isMine(row, col)) {
				row = random.nextInt(BOARD_SIZE);
				col = random.nextInt(BOARD_SIZE);
			}

			// set a mine in a valid location on the board
			board.setMine(row, col);
		}
	}

	private void printBoard(boolean showMines) {
		// ********* Display board header *************
		System.out.print("   ");
		for (int i = 0; i < board.getSize(); i++) {
			System.out.print(" " + i);
		}
		System.out.println();

		System.out.print("   ");
		for (int i = 0; i < board.getSize(); i++) {
			System.out.print("--");
		}
		System.out.println();

		// ************* Display board body **************
		// ROWS
		for (int i = 0; i < board.getSize(); i++) {
			System.out.print(i + " |");
			// COLUMNS
			for (int j = 0; j < board.getSize(); j++) {
				// if the current location is not visible, just show a square
				// unless user landed on mine or completed game
				if (!board.isVisible(i, j)) {

					// display all mines if user landed on mine or completed game
					// otherwise just show hidden cell
					if (board.isMine(i, j) && showMines) {
						System.out.print(" *");
					} else {
						System.out.print(" ⎕");
					}
				} else {
					// User selected mine which made it visible
					if (board.isMine(i, j) && showMines) {
						System.out.print(" *");
					} else {
						// Not a mine. Calculate mines around perimeter
						int count = countPerimeterMines(i, j);
						System.out.print(" " + count);
					}
				}
			}
			System.out.println();
		}
	}

	// This function checks the perimeter of location selected by the user.
	// Loop starts from 1 row above, 1 column before and ends
	// 1 row below, 1 column after selected location to run a total count of
	// the surrounding mines
	private int countPerimeterMines(int row, int col) {
		int count = 0;

		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {

				// this condition checks valid board spaces,
				// disregarding invalid spaces outside of the bounds of the board
				if (i >= 0 && i < board.getSize() && j >= 0 && j < board.getSize() && board.isMine(i, j)) {
					count++;
				}
			}
		}
		return count;
	}

	// This function shows the cell entered by the user if valid.
	// Checks if location contains mine, if not iterate around the perimeter
	private void showCell(int row, int col) {

		// Check if user entered valid values for row and column.
		// Also check selected location hasn't already been made visible.
		if (row >= 0 && row < board.getSize() && col >= 0 && col < board.getSize() && !board.isVisible(row, col)) {

			// set location visible to true
			board.setVisible(row, col);

			if (board.isMine(row, col)) {
				gameover = true;
			} else {
				int count = countPerimeterMines(row, col);

				// No mines surrounding current location.
				if (count == 0) {
					// *** RECURSIVE *** Open up board until mines are located in perimeter
					// locations
					for (int i = row - 1; i <= row + 1; i++) {
						for (int j = col - 1; j <= col + 1; j++) {
							showCell(i, j);
						}
					}
				}
			}
		}
	}

	private void startGame() {
		try (Scanner scanner = new Scanner(System.in)) {
			while (!gameover) {
				// print board to screen - false means the game is active and not to show the
				// mines
				// Mines are shown once the user lands on one, or wins the game.
				printBoard(false);

				System.out.println();
				System.out.print("First enter row, then column (4 6): ");
				int row = scanner.nextInt();
				int col = scanner.nextInt();

				// Display the location selected by user
				showCell(row, col);

				if (gameover) {
					// User landed on bomb - true means show all mines -> game is over
					System.out.println("\nBoom!!! Game Over!!!\n");
					printBoard(true);

				} else {
					// If this condition is true after loop, user has won
					boolean allCellsVisible = true;

					// Iterate through the board, checking if any safe locations are remaining
					// that haven't been made visible. If all safe locations are visible, user has
					// won
					for (int i = 0; i < board.getSize(); i++) {
						for (int j = 0; j < board.getSize(); j++) {
							// There are still safe moves which are not visible, continue play
							if (!board.isMine(i, j) && !board.isVisible(i, j)) {
								allCellsVisible = false;
								break;
							}
						}
					}
					if (allCellsVisible) {
						System.out.println("\nCongratulations!!! You won!!!\n");
						printBoard(true);
						break;
					}
				}
			}
		}
	}

	public void playGame() {
		startGame();
	}
}

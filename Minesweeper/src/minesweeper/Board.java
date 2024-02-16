package minesweeper;

public class Board {
	private final int size;
	private boolean[][] mines;
	private boolean[][] visible;

	public Board(int size) {
		this.size = size;
		createBoard();
	}

	private void createBoard() {
		mines = new boolean[size][size];
		visible = new boolean[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				mines[i][j] = false;
				visible[i][j] = false;
			}
		}
	}

	public int getSize() {
		return size;
	}

	public boolean isMine(int row, int col) {
		return mines[row][col];
	}

	public void setMine(int row, int col) {
		mines[row][col] = true;
	}

	public boolean isVisible(int row, int col) {
		return visible[row][col];
	}

	public void setVisible(int row, int col) {
		visible[row][col] = true;
	}
}

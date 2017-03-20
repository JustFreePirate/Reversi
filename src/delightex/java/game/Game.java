package delightex.java.game;

public class Game {
  public static final int EMPTY = 0;
  public static final int WHITE = 1;
  public static final int BLACK = -1;

  public static boolean INIT_STATE_WHITE = true;

  public final int n;
  private final int grid[][];
  private final int scores[][];

  private int whiteScore;
  private int blackScore;

  private int moveColor;
  private boolean gameOver;
  private final Player whitePlayer;
  private final Player blackPlayer;
  private Cell prevMove;

  public Game(int n, Player whitePlayer, Player blackPlayer) {
    if (n < 4 || n % 2 != 0) {
      throw new RuntimeException("wrong sizes of Game!");
    }
    this.n = n;

    grid = new int[n][];
    scores = new int[n][];
    for (int i = 0; i < n; i++) {
      grid[i] = new int[n];
      scores[i] = new int[n];
    }

    this.whitePlayer = whitePlayer;
    this.blackPlayer = blackPlayer;

    whitePlayer.init(this);
    blackPlayer.init(this);
    reset();
  }

  public void reset() {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        grid[i][j] = EMPTY;
      }
    }

    int sign = INIT_STATE_WHITE ? +1 : -1;
    grid[n / 2 - 1][n / 2 - 1] = BLACK * sign; //up left
    grid[n / 2][n / 2] = BLACK * sign; //down right
    grid[n / 2 - 1][n / 2] = WHITE * sign;
    grid[n / 2][n / 2 - 1] = WHITE * sign;

    whiteScore = 2;
    blackScore = 2;

    moveColor = BLACK;
    gameOver = false;

    prevMove = new Cell(n / 2, n / 2 - 1);
  }

  public int get(int i, int j) {
    return grid[i][j];
  }

  public int getWhiteScore() {
    return whiteScore;
  }

  public int getBlackScore() {
    return blackScore;
  }

  public int[][] getMoveScores(int[][] moveScores, int myColor) {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        moveScores[i][j] = moveScore(i, j, myColor);
      }
    }
    return moveScores;
  }

  public int[][] getMoveScores(int[][] moveScores) {
    return getMoveScores(moveScores, moveColor);
  }

  private int moveScore(int i, int j, int myColor) {
    if (grid[i][j] != EMPTY) {
      return 0;
    }

    int score = 0;
    score += scoreDirection(i, j, 0, 1, myColor);
    score += scoreDirection(i, j, 1, 0, myColor);
    score += scoreDirection(i, j, 1, 1, myColor);
    score += scoreDirection(i, j, 0, -1, myColor);
    score += scoreDirection(i, j, -1, 0, myColor);
    score += scoreDirection(i, j, -1, -1, myColor);
    return score;
  }

  private int scoreDirection(int x, int y, int dx, int dy, int myColor) {
    int score = 0;
    x += dx;
    y += dy;
    while (true) {
      if (!inGrid(x, y) || grid[x][y] == EMPTY) {
        return 0;
      }
      if (grid[x][y] == myColor) {
        return score;
      } else {
        score++;
      }
      x += dx;
      y += dy;
    }
  }

  private void flipDirection(int x, int y, int dx, int dy) {
    int count = scoreDirection(x, y, dx, dy, moveColor);

    x += dx;
    y += dy;
    for (int i = 0; i < count; i++) {
      grid[x][y] *= -1;
      x += dx;
      y += dy;
    }
  }

  private void flip(int x, int y) {
    flipDirection(x, y, 1, 0);
    flipDirection(x, y, 1, 1);
    flipDirection(x, y, 0, 1);
    flipDirection(x, y, -1, 0);
    flipDirection(x, y, -1, -1);
    flipDirection(x, y, 0, -1);
  }

  private boolean inGrid(int i, int j) {
    return (0 <= i) && (i < n) && (0 <= j) && (j < n);
  }

  public void processMove() {
    if (gameOver) {
      return;
    }

    Cell cell;
    if (moveColor == BLACK) {
      cell = blackPlayer.processMove(this);
    } else {
      cell = whitePlayer.processMove(this);
    }

    if (cell == null) { //no move
      if (prevMove == null) {
        gameOver = true;
        return;
      }

      boolean hasEmpty = false;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          hasEmpty = hasEmpty || grid[i][j] == EMPTY;
        }
      }

      if (!hasEmpty) {
        gameOver = true;
        return;
      }

      moveColor *= -1;
      prevMove = null;
      return;
    }

    int i = cell.x;
    int j = cell.y;
    int[][] moveScores = getMoveScores(scores);
    int score = moveScores[i][j];
    if (inGrid(i, j) && score > 0) {
      grid[i][j] = moveColor;
      if (moveColor == WHITE) {
        whiteScore += score + 1;
        blackScore -= score;
      } else {
        blackScore += score + 1;
        whiteScore -= score;
      }
      flip(i, j);

      moveColor *= -1;
      prevMove = cell;
    }
  }


  public boolean isGameOver() {
    return gameOver;
  }

  public int getMoveColor() {
    return moveColor;
  }
}

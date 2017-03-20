package delightex.java.game;

public class GreedPlayer implements Player {

  int[][] moveScores;
  private int n;

  public GreedPlayer() {
  }

  @Override
  public void init(Game game) {
    n = game.n;
    moveScores = new int[n][];
    for (int i = 0; i < n; i++) {
      moveScores[i] = new int[n];
    }
  }

  @Override
  public Cell processMove(Game game) {
    game.getMoveScores(moveScores);
    int max = 0;
    Cell cell = new Cell(-1, -1);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (moveScores[i][j] > max) {
          max = moveScores[i][j];
          cell.set(i, j);
        }
      }
    }
    if (max == 0) {
      return null;
    } else {
      return cell;
    }
  }
}

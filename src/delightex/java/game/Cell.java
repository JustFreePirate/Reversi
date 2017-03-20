package delightex.java.game;

public class Cell {
  public int x, y;
  public Cell(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void set(int i, int j) {
    x = i;
    y = j;
  }
}

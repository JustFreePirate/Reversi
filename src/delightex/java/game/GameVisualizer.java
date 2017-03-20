package delightex.java.game;

import javax.swing.*;
import java.awt.*;

public class GameVisualizer extends JPanel {
  public static final String title = "Reversi";
  public static final Color backgroudColor = new Color(255, 153, 102);
  public static final Color tileColor = new Color(255, 204, 153);
  public static final Color menuColor = new Color(242, 219, 176);
  public static final int MARGIN = 2;
  public static final int FIELD_SIZE = 600;
  public static final int MENU_WIDTH = 150;
  public static final int TITLE_SIZE = 22;
  public static final Font SCORE_FONT = new Font("Arial", Font.BOLD, 24);

  private final Font MOVE_SCORE_FONT;
  private final Game myGame;
  private final int TILE_SIZE;

  private final int[][] moveScores;

  public static void main(String[] args) {
//    playGame(new Game(8, new GreedPlayer(), new GreedPlayer()));
    /*
    int countPattern = 0;
    int count = 0;
    for (int i = 4; i < 120; i += 2) {
      Game game = new Game(i, new GreedPlayer(), new GreedPlayer());
      while (!game.isGameOver()) {
        game.processMove();
      }
      int bs = game.getBlackScore();
      int ws = game.getWhiteScore();
      if (i % 4 == 0  && bs > ws || i % 4 == 2 && ws > bs) {
        countPattern++;
      }
      count++;
      String winner = bs > ws ? "Black" : bs == ws ? "Draw" : "White";
      System.out.println("Game: " + i + " x " + i + " white: " + ws + " black: " + bs + " win: " + winner);
    }

    System.out.println("success prob: " + (double) countPattern / count);*/


    Game game = new Game(8, new GreedPlayer(), new GreedPlayer());
    GameVisualizer gv = createGameVisualizer(game);

    gv.repaint();
    while (!game.isGameOver()) {
      try {
        Thread.sleep(500);
      } catch (Exception e) {}
      game.processMove();
      gv.repaint();
    }
  }

  private GameVisualizer(Game game, int tile_size) {
    myGame = game;
    setFocusable(true);
    TILE_SIZE = tile_size;
    MOVE_SCORE_FONT = new Font("Arial", Font.BOLD, tile_size / 2);

    moveScores = new int[myGame.n][];
    for (int i = 0; i < myGame.n; i++) {
      moveScores[i] = new int[myGame.n];
    }
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    int width = getWidth();
    int height = getHeight();
    g.setColor(backgroudColor);
    g.fillRect(0, 0, width, height);

    myGame.getMoveScores(this.moveScores);

    for (int i = 0; i < myGame.n; i++) {
      for (int j = 0; j < myGame.n; j++) {
        drawTile(g, i, j, moveScores[i][j]);
      }
    }

    g.setColor(menuColor);
    int size = myGame.n * TILE_SIZE;
    g.fillRect(size, 0, size + MENU_WIDTH, size);

    g.setFont(SCORE_FONT);
    g.setColor(Color.black);
    g.drawString("Black: " + myGame.getBlackScore(), size + 20, 50);
    g.drawString("White: " + myGame.getWhiteScore(), size + 20, 100);
    g.drawString("Move:", size + 20, 250);

    if (myGame.getMoveColor() == myGame.WHITE) {
      g.setColor(Color.WHITE);
    } else {
      g.setColor(Color.black);
    }
    g.fillRect(size + 100, 230, 20, 20);

    if (myGame.isGameOver()) {
      g.setColor(Color.black);
      int bs = myGame.getBlackScore();
      int ws = myGame.getWhiteScore();

      String winner = bs > ws ? "Black" : bs == ws ? "Draw" : "White";
      g.drawString("Win: " + winner, size + 20, 350);
    }
  }

  private Color getStoneColor(int stone) {
    if (stone == Game.WHITE) {
      return Color.white;
    } else {
      return Color.black;
    }
  }

  private void drawTile(Graphics g, int i, int j, int score) {
    int tileHeight = TILE_SIZE - MARGIN * 2;
    int tileWidth = TILE_SIZE - MARGIN * 2;
    int x0 = j * TILE_SIZE + MARGIN;
    int y0 = i * TILE_SIZE + MARGIN;
    g.setColor(tileColor);
    g.fillRect(x0, y0, tileWidth, tileHeight);

    int stone = myGame.get(i, j);
    if (stone != 0) {
      g.setColor(getStoneColor(stone));
      g.fillOval(x0 , y0, tileWidth, tileHeight);
    } else if (score != 0) {
      g.setColor(Color.black);
      g.drawOval(x0, y0, tileWidth, tileHeight);
      g.setFont(MOVE_SCORE_FONT);
      g.drawString(Integer.toString(score), x0 + (int) (tileWidth * 0.35), y0 + (int) (tileHeight * 0.67));
    }
  }

  public static GameVisualizer createGameVisualizer(Game game) {
    JFrame jFrame = new JFrame(title);
    jFrame.setVisible(true);
    int tile_size = FIELD_SIZE / game.n;
    jFrame.setSize(FIELD_SIZE + MENU_WIDTH, FIELD_SIZE + TITLE_SIZE); //title size = 22
    jFrame.setResizable(false);
    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GameVisualizer gv = new GameVisualizer(game, tile_size);
    jFrame.add(gv);
    jFrame.setLocationRelativeTo(null);
    return gv;
  }

  public static void playGame(Game game) {
    GameVisualizer gameVisualizer = createGameVisualizer(game);
  }
}

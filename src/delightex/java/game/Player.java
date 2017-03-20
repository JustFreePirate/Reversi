package delightex.java.game;

public interface Player {
  Cell processMove(Game game);

  void init(Game game);
}

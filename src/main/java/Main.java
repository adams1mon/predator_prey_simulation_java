import entities.GameObject;

import java.awt.event.KeyEvent;

public class Main {

  public static void main(String[] args) {
    final var player = GameObject.createPlayer();
    final var npc = GameObject.createNpc();

    System.out.println("Player Update:");
    player.update(KeyEvent.KEY_LOCATION_LEFT);

    System.out.println("NPC Update:");
    npc.demoUpdate();
  }
}

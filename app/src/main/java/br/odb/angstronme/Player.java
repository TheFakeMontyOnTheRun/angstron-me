package br.odb.angstronme;

/**
 * Created by monty on 6/6/15.
 */
public class Player {

    public void updatePosition() {
        switch (direction) {
            case N:
                position.y--;
                break;
            case E:
                position.x--;
                break;
            case S:
                position.y++;
                break;
            case W:
                position.x++;
                break;
        }
    }

    public enum Team {NOTHING, PLAYER, Team, CPU}

    public enum Directions {N, E, S, W}

    public final Team team;
    public Directions direction;
    public final Vec3 position = new Vec3(0, 0, 0);

    public Player(Team team) {
        this.team = team;
    }
}

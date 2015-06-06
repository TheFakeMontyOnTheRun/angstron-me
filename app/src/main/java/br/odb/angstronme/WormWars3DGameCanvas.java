package br.odb.angstronme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by monty on 6/5/15.
 */
public class WormWars3DGameCanvas extends View implements View.OnTouchListener {

    public final int LEVEL_SIZE_X = 80;
    public final int LEVEL_SIZE_Y = 30;
    final Player.Team[][] map = new Player.Team[LEVEL_SIZE_X][LEVEL_SIZE_Y];

    final Player player = new Player(Player.Team.PLAYER);
    final Player bot = new Player(Player.Team.CPU);

    final Vec3 cameraPosition = new Vec3(0, 0, 0);

    final Paint paint = new Paint();
    final SimpleGestureDetector gestureDetector = new SimpleGestureDetector();

    public WormWars3DGameCanvas(Context context) {
        super(context);

        init();
    }

    public WormWars3DGameCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public WormWars3DGameCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    void setColor(int r, int g, int b) {
        paint.setColor(Color.argb(255, r, g, b));
    }

    void drawRect(Canvas g, float x0, float y0, float x1, float y1) {
        g.drawRect(x0, y0, x1, y1, paint);
    }

    void drawLine(Canvas g, float x0, float y0, float x1, float y1) {
        g.drawLine(x0, y0, x1, y1, paint);
    }

    @Override
    protected void onDraw(Canvas g) {
        super.onDraw(g);

        clearDirtyScreenArea(g);
        drawBackground(g);
        drawHorizonLine(g);
        drawTrails(g);
        drawMap(g);
        drawPlayer(g, player.position);
        drawPlayer(g, bot.position);
        drawArena(g);
    }

    private void clearDirtyScreenArea(Canvas g) {
        setColor(0, 0, 0);
        drawRect(g, 0, 0, getWidth(), getHeight());
    }

    private void drawPlayer(Canvas g, Vec3 player) {

        Vec2[] plane1 = new Vec2[4];
        Vec2[] plane2 = new Vec2[4];

        float x;
        float y;
        x = player.x;
        y = player.y;

        plane1[0] = Project(Grid(x + 1, y + 1));
        plane1[1] = Project(Grid(x, y + 1));
        plane1[2] = Project(Grid(x, y));
        plane1[3] = Project(Grid(x + 1, y));

        plane2[0] = Project(Grid(x + 1, y + 1), 0, -50, 0);
        plane2[1] = Project(Grid(x, y + 1), 0, -50, 0);
        plane2[2] = Project(Grid(x, y), 0, -50, 0);
        plane2[3] = Project(Grid(x + 1, y), 0, -50, 0);

        drawCube(g, plane1, plane2);
    }

    private void drawCube(Canvas g, Vec2[] plane1, Vec2[] plane2) {
        setColor(255, 255, 255);
        drawLine(g, plane1[0].x, plane1[0].y, plane1[1].x, plane1[1].y);
        drawLine(g, plane1[1].x, plane1[1].y, plane1[2].x, plane1[2].y);
        drawLine(g, plane1[2].x, plane1[2].y, plane1[3].x, plane1[3].y);
        drawLine(g, plane1[3].x, plane1[3].y, plane1[0].x, plane1[0].y);

        drawLine(g, plane2[0].x, plane2[0].y, plane2[1].x, plane2[1].y);
        drawLine(g, plane2[1].x, plane2[1].y, plane2[2].x, plane2[2].y);
        drawLine(g, plane2[2].x, plane2[2].y, plane2[3].x, plane2[3].y);
        drawLine(g, plane2[3].x, plane2[3].y, plane2[0].x, plane2[0].y);

        drawLine(g, plane1[0].x, plane1[0].y, plane2[0].x, plane2[0].y);
        drawLine(g, plane1[1].x, plane1[1].y, plane2[1].x, plane2[1].y);
        drawLine(g, plane1[2].x, plane1[2].y, plane2[2].x, plane2[2].y);
        drawLine(g, plane1[3].x, plane1[3].y, plane2[3].x, plane2[3].y);
    }

    private void drawTrails(Canvas g) {

        Vec2[] p = new Vec2[4];

        for (float x = 0; x < LEVEL_SIZE_X; x++) {
            for (float y = 0; y < LEVEL_SIZE_Y; y++) {

                if (getMap(x, y) == Player.Team.PLAYER) {
                    setColor(255, 0, 0);
                } else if (getMap(x, y) == Player.Team.CPU) {
                    setColor(0, 0, 255);
                } else {
                    continue;
                }

                p[0] = Project(Grid(x + 1, y + 1));
                p[1] = Project(Grid(x, y + 1));
                p[2] = Project(Grid(x, y));
                p[3] = Project(Grid(x + 1, y));

                drawLine(g, p[1].x, p[1].y, p[2].x, p[2].y);
                drawLine(g, p[2].x, p[2].y, p[3].x, p[3].y);
                drawLine(g, p[0].x, p[0].y, p[1].x, p[1].y);
                drawLine(g, p[3].x, p[3].y, p[0].x, p[0].y);
            }
        }
    }

    private void drawMap(Canvas g) {
        for (float x = 0; x < LEVEL_SIZE_X; x++) {
            for (float y = 0; y < LEVEL_SIZE_Y; y++) {

                if (getMap(x, y) == Player.Team.PLAYER) {
                    setColor(255, 0, 0);
                } else if (getMap(x, y) == Player.Team.CPU) {
                    setColor(0, 0, 255);
                } else {
                    continue;
                }

                drawRect(g, (int) (LEVEL_SIZE_X - x), (int) (2 * (LEVEL_SIZE_Y - y)), 1, 2);
            }
        }
    }

    private void drawArena(Canvas g) {

        Vec2[] p = new Vec2[4];

        p[0] = Project(Grid(0, 0));
        p[1] = Project(Grid(LEVEL_SIZE_X, 0));
        p[2] = Project(Grid(LEVEL_SIZE_X, LEVEL_SIZE_Y));
        p[3] = Project(Grid(0, LEVEL_SIZE_Y));

        setColor(0, 255, 0);
        drawLine(g, p[0].x, p[0].y, p[1].x, p[1].y);
        setColor(0, 255, 0);
        drawLine(g, p[1].x, p[1].y, p[2].x, p[2].y);
        setColor(0, 255, 0);
        drawLine(g, p[2].x, p[2].y, p[3].x, p[3].y);
        setColor(0, 255, 0);
        drawLine(g, p[3].x, p[3].y, p[0].x, p[0].y);
    }

    private void drawHorizonLine(Canvas g) {
        setColor(127, 0, 0);
        drawLine(g, 0, getHeight() / 2, getWidth(), getHeight() / 2);
    }

    private void drawBackground(Canvas g) {
        int y2;
        float ratio;
        float _y;
        ratio = 256.0f / ((float) getHeight() / 2.0f);
        y2 = 0;

        for (_y = 255.0f; _y > 0.0f; y2++, _y -= ratio) {
            setColor((int) _y, 0, 0);
            drawLine(g, 0, y2, getWidth(), y2);
        }
    }

    public Player.Team getMap(float aX, float aY) {
        if (aX <= 0 || aX >= LEVEL_SIZE_X || aY >= LEVEL_SIZE_Y || aY <= 0) {
            return Player.Team.PLAYER;
        }
        return map[((int) aX)][((int) aY)];
    }

    public void setMap(float aX, float aY, Player.Team aValue) {
        if (aX <= 0 || aX >= LEVEL_SIZE_X || aY >= LEVEL_SIZE_Y || aY <= 0) {
            return;
        }
        map[((int) aX)][((int) aY)] = aValue;
    }

    public void init() {
        RestartGame();
        this.setOnTouchListener(this);
    }

    public Vec3 Grid(int x, int y) {
        return new Vec3(((600 / LEVEL_SIZE_X) * x), 5, (y) + 3);
    }

    public Vec3 Grid(float x, float y) {
        return new Vec3(((600 / LEVEL_SIZE_X) * x), 5, (y) + 3);
    }

    public void RestartGame() {

        player.direction = Player.Directions.S;
        player.position.set(1, 1, 0);

        bot.direction = Player.Directions.N;
        bot.position.set(LEVEL_SIZE_X / 2, LEVEL_SIZE_Y / 2, 0);

        updateCameraPositionFromPlayer();
        resetMap();
    }

    private void resetMap() {
        for (int x = 0; x < LEVEL_SIZE_X; x++) {
            for (int y = 0; y < LEVEL_SIZE_Y; y++) {
                setMap(x, y, Player.Team.NOTHING);
            }
        }
    }

    public Vec2 Project(Vec3 aVec, float offX, float offY, float offZ) {
        return Project(aVec.x + offX, aVec.y + offY, aVec.z + offZ);
    }

    public Vec2 Project(Vec3 aVec) {
        return Project(aVec.x, aVec.y, aVec.z);
    }

    public Vec2 Project(float x, float y, float z) {

        Vec2 v = new Vec2(0, 0);
        v.x = (getWidth() / 2) - (x - cameraPosition.x) * 5 / (z - cameraPosition.z);
        v.y = (getHeight() / 2) + (y - cameraPosition.y) * 5 / (z - cameraPosition.z);
        return v;
    }

    public void Update() {

        enclosePlayer(player.position);
        enclosePlayer(bot.position);

        updateCameraPositionFromPlayer();

        setMap(player.position.x, player.position.y, Player.Team.PLAYER);
        setMap(bot.position.x, bot.position.y, Player.Team.CPU);

        player.updatePosition();
        bot.updatePosition();

        switch (bot.direction) {
            case N:
                if (getMap(bot.position.x, bot.position.y - 1) != Player.Team.NOTHING) {
                    bot.direction = Player.Directions.W;
                }
                return;

            case E:
                if (getMap(bot.position.x - 1, bot.position.y) != Player.Team.NOTHING) {
                    bot.direction = Player.Directions.N;
                }
                return;

            case S:
                if (getMap(bot.position.x, bot.position.y + 1) != Player.Team.NOTHING) {
                    bot.direction = Player.Directions.E;
                }
                return;

            case W:
                if (getMap(bot.position.x + 1, bot.position.y) != Player.Team.NOTHING) {
                    bot.direction = Player.Directions.S;
                }
                return;
        }
    }

    private void enclosePlayer(Vec3 player) {
        if (player.x >= LEVEL_SIZE_X) {
            player.x = LEVEL_SIZE_X - 1;
        }
        if (player.y >= LEVEL_SIZE_Y) {
            player.y = LEVEL_SIZE_Y - 1;
        }
        if (player.x < 0) {
            player.x = 0;
        }
        if (player.y < 0) {
            player.y = 0;
        }
    }

    private void updateCameraPositionFromPlayer() {
        cameraPosition.y = -player.position.y - 70;
        cameraPosition.x = player.position.x * 600 / LEVEL_SIZE_X;
        cameraPosition.z = player.position.y / 20;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyPressed(keyCode);
    }

    boolean keyPressed(int gameCode) {

        Player.Directions d = Controls.directionFrom(gameCode);

        if (d != null) {
            player.direction = d;
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            gestureDetector.acc.x += (motionEvent.getX() - gestureDetector.touch.x);
            gestureDetector.acc.y += (motionEvent.getY() - gestureDetector.touch.y);
        } else {

            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                if (gestureDetector.isHorizontal()) {
                    if (gestureDetector.acc.x > 0) {
                        keyPressed(KeyEvent.KEYCODE_DPAD_RIGHT);
                    } else {
                        keyPressed(KeyEvent.KEYCODE_DPAD_LEFT);
                    }
                } else {
                    if (gestureDetector.acc.y < 0) {
                        keyPressed(KeyEvent.KEYCODE_DPAD_DOWN);
                    } else {
                        keyPressed(KeyEvent.KEYCODE_DPAD_UP);
                    }
                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                gestureDetector.touch.x = (int) motionEvent.getX();
                gestureDetector.touch.y = (int) motionEvent.getY();
            }

            gestureDetector.reset();
        }


        return true;
    }

    enum Controls {
        Up(KeyEvent.KEYCODE_DPAD_UP, Player.Directions.N),
        Down(KeyEvent.KEYCODE_DPAD_DOWN, Player.Directions.S),
        Left(KeyEvent.KEYCODE_DPAD_LEFT, Player.Directions.W),
        Right(KeyEvent.KEYCODE_DPAD_RIGHT, Player.Directions.E);

        public final int keycode;
        public final Player.Directions direction;

        Controls(int keycode, Player.Directions direction) {
            this.direction = direction;
            this.keycode = keycode;
        }

        public static Player.Directions directionFrom(int keycode) {

            for (Controls c : values()) {
                if (c.keycode == keycode) {
                    return c.direction;
                }
            }
            return null;
        }
    }

}

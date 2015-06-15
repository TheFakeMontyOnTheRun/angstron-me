package br.odb.angstronme;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class PlayGameActivity extends Activity {


    private static final String DEBUG_TAG = "WW3D";
    Angtron3DGameCanvas canvas;

    Runnable updateRunnable = new Runnable() {
        public void run() {
            canvas.invalidate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_game);

        canvas = (Angtron3DGameCanvas) findViewById(R.id.gameView);
        canvas.RestartGame(getIntent().getIntExtra( "level", 0 ) * 5 );
    }

    void updateOnMainThread() {
        runOnUiThread(updateRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    updateOnMainThread();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    canvas.update();
                }
            }
        }).start();

        canvas.requestFocus();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
            return true;
        }

        return canvas.onKeyDown(keyCode, event);
    }
}

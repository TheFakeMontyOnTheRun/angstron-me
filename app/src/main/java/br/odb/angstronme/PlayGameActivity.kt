package br.odb.angstronme

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_play_game.*

class PlayGameActivity : Activity() {
    var updateRunnable = Runnable { gameView.invalidate() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)
        gameView.RestartGame(intent.getIntExtra("level", 0) * 5)
    }

    fun updateOnMainThread() {
        runOnUiThread(updateRunnable)
    }

    override fun onResume() {
        super.onResume()
        Thread(Runnable {
            while (true) {
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                updateOnMainThread()
            }
        }).start()
        Thread(Runnable {
            while (true) {
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                gameView.update()
            }
        }).start()
        gameView.requestFocus()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return gameView.onKeyDown(keyCode, event)
    }

    companion object {
        private const val DEBUG_TAG = "WW3D"
    }
}
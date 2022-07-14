package br.odb.angstronme

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_play_game.*

class PlayGameActivity : AppCompatActivity() {
    private var updateRunnable = Runnable { gameView.invalidate() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)
        gameView.RestartGame(intent.getIntExtra("level", 0) * 5)
    }

    private fun updateOnMainThread() {
        runOnUiThread(updateRunnable)
    }

    override fun onResume() {
        super.onResume()
        Thread {
            while (true) {
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                updateOnMainThread()
            }
        }.start()
        Thread {
            while (true) {
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                gameView.update()
            }
        }.start()
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
    }
}
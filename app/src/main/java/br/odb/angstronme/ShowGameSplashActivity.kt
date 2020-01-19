package br.odb.angstronme

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class ShowGameSplashActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_game_splash)
        findViewById<View>(R.id.btnStartGame).setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_show_game_splash, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        val intent = Intent(this, PlayGameActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var requestCode = requestCode
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode >= 5) {
        } else if (resultCode == 2) {
            val intent = Intent(this, PlayGameActivity::class.java)
            intent.putExtra("level", ++requestCode)
            startActivityForResult(intent, requestCode)
        }
    }
}
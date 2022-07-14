package br.odb.angstronme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import br.odb.angstronme.Player.Team

/**
 * Created by monty on 6/5/15.
 */
class Angtron3DGameCanvas : View, OnTouchListener {
	val player = Player(Team.PLAYER)
	val bot = Player(Team.CPU)
	val cameraPosition = Vec3(0.0f, 0.0f, 0.0f)
	val paint = Paint()
	val gestureDetector = SimpleGestureDetector()
	var LEVEL_SIZE = 50
	val map = Array(LEVEL_SIZE) { arrayOfNulls<Team>(LEVEL_SIZE) }

	constructor(context: Context?) : super(context) {
		init()
	}

	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
		init()
	}

	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
		context,
		attrs,
		defStyleAttr
	) {
		init()
	}

	fun setColor(r: Int, g: Int, b: Int) {
		paint.color = Color.argb(255, r, g, b)
	}

	fun drawRect(g: Canvas, x0: Float, y0: Float, x1: Float, y1: Float) {
		g.drawRect(x0, y0, x1, y1, paint)
	}

	fun drawLine(g: Canvas, x0: Float, y0: Float, x1: Float, y1: Float) {
		g.drawLine(x0, y0, x1, y1, paint)
	}

	override fun onDraw(g: Canvas) {
		super.onDraw(g)
		clearDirtyScreenArea(g)
		drawBackground(g)
		drawHorizonLine(g)
		drawTrails(g)
		drawPlayer(g, player.position)
		drawPlayer(g, bot.position)
		drawArena(g)
		drawMap(g)
	}

	private fun clearDirtyScreenArea(g: Canvas) {
		setColor(0, 0, 0)
		drawRect(g, 0f, 0f, width.toFloat(), height.toFloat())
	}

	private fun drawPlayer(g: Canvas, player: Vec3) {
		val plane1 = arrayOfNulls<Vec2>(4)
		val plane2 = arrayOfNulls<Vec2>(4)
		val x: Float
		val y: Float
		x = player.x
		y = player.y
		plane1[0] = project3DInto2D(Grid(x + 1, y + 1))
		plane1[1] = project3DInto2D(Grid(x, y + 1))
		plane1[2] = project3DInto2D(Grid(x, y))
		plane1[3] = project3DInto2D(Grid(x + 1, y))
		plane2[0] = project3DInto2DWithOffset(Grid(x + 1, y + 1), 0f, -50f, 0f)
		plane2[1] = project3DInto2DWithOffset(Grid(x, y + 1), 0f, -50f, 0f)
		plane2[2] = project3DInto2DWithOffset(Grid(x, y), 0f, -50f, 0f)
		plane2[3] = project3DInto2DWithOffset(Grid(x + 1, y), 0f, -50f, 0f)
		drawCube(g, plane1, plane2)
	}

	private fun drawCube(g: Canvas, plane1: Array<Vec2?>, plane2: Array<Vec2?>) {
		setColor(255, 255, 255)
		drawLine(g, plane1[0]!!.x, plane1[0]!!.y, plane1[1]!!.x, plane1[1]!!.y)
		drawLine(g, plane1[1]!!.x, plane1[1]!!.y, plane1[2]!!.x, plane1[2]!!.y)
		drawLine(g, plane1[2]!!.x, plane1[2]!!.y, plane1[3]!!.x, plane1[3]!!.y)
		drawLine(g, plane1[3]!!.x, plane1[3]!!.y, plane1[0]!!.x, plane1[0]!!.y)
		drawLine(g, plane2[0]!!.x, plane2[0]!!.y, plane2[1]!!.x, plane2[1]!!.y)
		drawLine(g, plane2[1]!!.x, plane2[1]!!.y, plane2[2]!!.x, plane2[2]!!.y)
		drawLine(g, plane2[2]!!.x, plane2[2]!!.y, plane2[3]!!.x, plane2[3]!!.y)
		drawLine(g, plane2[3]!!.x, plane2[3]!!.y, plane2[0]!!.x, plane2[0]!!.y)
		drawLine(g, plane1[0]!!.x, plane1[0]!!.y, plane2[0]!!.x, plane2[0]!!.y)
		drawLine(g, plane1[1]!!.x, plane1[1]!!.y, plane2[1]!!.x, plane2[1]!!.y)
		drawLine(g, plane1[2]!!.x, plane1[2]!!.y, plane2[2]!!.x, plane2[2]!!.y)
		drawLine(g, plane1[3]!!.x, plane1[3]!!.y, plane2[3]!!.x, plane2[3]!!.y)
	}

	private fun drawTrails(g: Canvas) {
		val p = arrayOfNulls<Vec2>(4)
		for (x in 0 until LEVEL_SIZE) {
			for (y in 0 until LEVEL_SIZE) {
				if (getMap(x.toFloat(), y.toFloat()) === Team.PLAYER) {
					setColor(255, 0, 0)
				} else if (getMap(x.toFloat(), y.toFloat()) === Team.CPU) {
					setColor(0, 0, 255)
				} else {
					continue
				}
				p[0] = project3DInto2D(Grid(x + 1, y + 1))
				p[1] = project3DInto2D(Grid(x, y + 1))
				p[2] = project3DInto2D(Grid(x, y))
				p[3] = project3DInto2D(Grid(x + 1, y))
				drawLine(g, p[1]!!.x, p[1]!!.y, p[2]!!.x, p[2]!!.y)
				drawLine(g, p[2]!!.x, p[2]!!.y, p[3]!!.x, p[3]!!.y)
				drawLine(g, p[0]!!.x, p[0]!!.y, p[1]!!.x, p[1]!!.y)
				drawLine(g, p[3]!!.x, p[3]!!.y, p[0]!!.x, p[0]!!.y)
			}
		}
	}

	private fun drawMap(g: Canvas) {
		val mapPositionX = -LEVEL_SIZE / 2 + width / 2
		val mapPositionY = -LEVEL_SIZE + height
		var team: Team?
		for (x in 0 until LEVEL_SIZE) {
			for (y in 0 until LEVEL_SIZE) {
				team = getMap((LEVEL_SIZE - x).toFloat(), (LEVEL_SIZE - y).toFloat())
				if (team === Team.PLAYER) {
					setColor(255, 0, 0)
				} else if (team === Team.CPU) {
					setColor(0, 0, 255)
				} else {
					setColor(0, 255, 0)
				}
				drawRect(
					g,
					(mapPositionX + x).toFloat(),
					(mapPositionY + y).toFloat(),
					(mapPositionX + x + 1).toFloat(),
					(mapPositionY + y + 1).toFloat()
				)
			}
		}
	}

	private fun drawArena(g: Canvas) {
		val p = arrayOfNulls<Vec2>(4)
		p[0] = project3DInto2D(Grid(0, 0))
		p[1] = project3DInto2D(Grid(LEVEL_SIZE - 1, 0))
		p[2] = project3DInto2D(Grid(LEVEL_SIZE - 1, LEVEL_SIZE - 1))
		p[3] = project3DInto2D(Grid(0, LEVEL_SIZE - 1))
		setColor(0, 255, 0)
		drawLine(g, p[0]!!.x, p[0]!!.y, p[1]!!.x, p[1]!!.y)
		setColor(0, 255, 0)
		drawLine(g, p[1]!!.x, p[1]!!.y, p[2]!!.x, p[2]!!.y)
		setColor(0, 255, 0)
		drawLine(g, p[2]!!.x, p[2]!!.y, p[3]!!.x, p[3]!!.y)
		setColor(0, 255, 0)
		drawLine(g, p[3]!!.x, p[3]!!.y, p[0]!!.x, p[0]!!.y)
		var p2D: Vec2
		var x = 0f
		while (x < LEVEL_SIZE) {
			var y = 0f
			while (y < LEVEL_SIZE) {
				p2D = project3DInto2D(Grid(x, y))
				g.drawPoint(p2D.x, p2D.y, paint)
				y += 3f
			}
			x += 3f
		}
	}

	private fun drawHorizonLine(g: Canvas) {
		setColor(127, 0, 0)
		drawLine(g, 0f, (height / 2).toFloat(), width.toFloat(), (height / 2).toFloat())
	}

	private fun drawBackground(g: Canvas) {
		var y2: Int
		val ratio: Float
		var _y: Float
		ratio = 256.0f / (height.toFloat() / 2.0f)
		y2 = 0
		_y = 255.0f
		while (_y > 0.0f) {
			setColor(_y.toInt(), 0, 0)
			drawLine(g, 0f, y2.toFloat(), width.toFloat(), y2.toFloat())
			y2++
			_y -= ratio
		}
	}

	fun getMap(aX: Float, aY: Float): Team? {
		return if (aX <= 0 || aX >= LEVEL_SIZE || aY >= LEVEL_SIZE || aY <= 0) {
			Team.NOTHING
		} else map[aX.toInt()][aY.toInt()]
	}

	fun setMap(aX: Float, aY: Float, aValue: Team?) {
		if (aX <= 0 || aX >= LEVEL_SIZE || aY >= LEVEL_SIZE || aY <= 0) {
			return
		}
		map[aX.toInt()][aY.toInt()] = aValue
	}

	fun init() {
		setOnTouchListener(this)
	}

	fun Grid(x: Int, y: Int): Vec3 {
		return Vec3((600 / LEVEL_SIZE * x).toFloat(), 5.0f, (y + 3).toFloat())
	}

	fun Grid(x: Float, y: Float): Vec3 {
		return Vec3(600 / LEVEL_SIZE * x, 5.0f, y + 3)
	}

	fun RestartGame(sizeOffset: Int) {
		LEVEL_SIZE -= sizeOffset
		player.direction = Player.Directions.S
		player.position[1f, 1f] = 0f
		bot.direction = Player.Directions.N
		bot.position[(LEVEL_SIZE / 2).toFloat(), (LEVEL_SIZE / 2).toFloat()] = 0f
		updateCameraPositionFromPlayer()
		resetMap()
	}

	private fun resetMap() {
		for (x in 0 until LEVEL_SIZE) {
			for (y in 0 until LEVEL_SIZE) {
				setMap(x.toFloat(), y.toFloat(), Team.NOTHING)
			}
		}
	}

	fun project3DInto2DWithOffset(aVec: Vec3, offX: Float, offY: Float, offZ: Float): Vec2 {
		return project3DInto2D(aVec.x + offX, aVec.y + offY, aVec.z + offZ)
	}

	fun project3DInto2D(aVec: Vec3): Vec2 {
		return project3DInto2D(aVec.x, aVec.y, aVec.z)
	}

	fun project3DInto2D(x: Float, y: Float, z: Float): Vec2 {
		val ratio = (width / height).toFloat()
		val v = Vec2(0.0f, 0.0f)
		v.x = width / 2 - (x - cameraPosition.x) * 10 * 4 / (z - cameraPosition.z)
		v.y = height / 2 + (y - cameraPosition.y) * 5 / (z - cameraPosition.z)
		return v
	}

	fun update() {
		enclosePlayer(player.position)
		enclosePlayer(bot.position)
		updateCameraPositionFromPlayer()
		setMap(player.position.x, player.position.y, Team.PLAYER)
		setMap(bot.position.x, bot.position.y, Team.CPU)
		player.updatePosition()
		bot.updatePosition()
		if (getMap(player.position.x, player.position.y) !== Team.NOTHING) {
			(context as AppCompatActivity).setResult(1)
			(context as AppCompatActivity).finish()
		}
		if (getMap(bot.position.x, bot.position.y) !== Team.NOTHING) {
			(context as AppCompatActivity).setResult(2)
			(context as AppCompatActivity).finish()
		}
		when (bot.direction) {
			Player.Directions.N -> {
				if (getMap(bot.position.x, bot.position.y - 1) !== Team.NOTHING) {
					bot.direction = Player.Directions.W
				}
				return
			}
			Player.Directions.E -> {
				if (getMap(bot.position.x - 1, bot.position.y) !== Team.NOTHING) {
					bot.direction = Player.Directions.N
				}
				return
			}
			Player.Directions.S -> {
				if (getMap(bot.position.x, bot.position.y + 1) !== Team.NOTHING) {
					bot.direction = Player.Directions.E
				}
				return
			}
			Player.Directions.W -> {
				if (getMap(bot.position.x + 1, bot.position.y) !== Team.NOTHING) {
					bot.direction = Player.Directions.S
				}
				return
			}
			else -> {}
		}
	}

	private fun enclosePlayer(player: Vec3) {
		if (player.x >= LEVEL_SIZE) {
			player.x = (LEVEL_SIZE - 1).toFloat()
		}
		if (player.y >= LEVEL_SIZE) {
			player.y = (LEVEL_SIZE - 1).toFloat()
		}
		if (player.x < 0) {
			player.x = 0.0f
		}
		if (player.y < 0) {
			player.y = 0.0f
		}
	}

	private fun updateCameraPositionFromPlayer() {
		cameraPosition.y = -player.position.y - 100 * 5 * (player.position.y / LEVEL_SIZE)
		cameraPosition.x = player.position.x * 600 / LEVEL_SIZE
		cameraPosition.z = 1.0f
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		return keyPressed(keyCode)
	}

	fun keyPressed(gameCode: Int): Boolean {
		val d = Controls.directionFrom(gameCode, player.direction)
		if (d != null) {
			player.direction = d
			return true
		}
		return false
	}

	override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
		if (motionEvent.action == MotionEvent.ACTION_MOVE) {
			gestureDetector.acc.x += motionEvent.x - gestureDetector.touch.x
			gestureDetector.acc.y += motionEvent.y - gestureDetector.touch.y
		} else {
			if (motionEvent.action == MotionEvent.ACTION_UP) {
				if (gestureDetector.movementIsNegligible()) {
					return true
				}
				if (gestureDetector.isHorizontal) {
					if (gestureDetector.acc.x > 0) {
						keyPressed(KeyEvent.KEYCODE_DPAD_RIGHT)
					} else {
						keyPressed(KeyEvent.KEYCODE_DPAD_LEFT)
					}
				} else {
					if (gestureDetector.acc.y < 0) {
						keyPressed(KeyEvent.KEYCODE_DPAD_DOWN)
					} else {
						keyPressed(KeyEvent.KEYCODE_DPAD_UP)
					}
				}
			} else if (motionEvent.action == MotionEvent.ACTION_DOWN) {
				gestureDetector.touch.x = motionEvent.x.toInt().toFloat()
				gestureDetector.touch.y = motionEvent.y.toInt().toFloat()
			}
			gestureDetector.reset()
		}
		return true
	}

	internal enum class Controls(
		val keycode: Int,
		val direction: Player.Directions,
		val oposite: Player.Directions
	) {
		Up(
			KeyEvent.KEYCODE_DPAD_UP,
			Player.Directions.N,
			Player.Directions.S
		),
		Down(KeyEvent.KEYCODE_DPAD_DOWN, Player.Directions.S, Player.Directions.N), Left(
			KeyEvent.KEYCODE_DPAD_LEFT, Player.Directions.W, Player.Directions.E
		),
		Right(KeyEvent.KEYCODE_DPAD_RIGHT, Player.Directions.E, Player.Directions.W);

		companion object {
			fun directionFrom(keycode: Int, current: Player.Directions?): Player.Directions? {
				for (c in values()) {
					if (c.keycode == keycode && c.oposite !== current) {
						return c.direction
					}
				}
				return null
			}
		}
	}
}
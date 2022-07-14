package br.odb.angstronme

import br.odb.angstronme.Vec2
import br.odb.angstronme.SimpleGestureDetector
import kotlin.math.abs

/**
 * Created by monty on 6/6/15.
 */
class SimpleGestureDetector {
	@JvmField
	var acc = Vec2(0.0f, 0.0f)
	@JvmField
	var touch = Vec2(0.0f, 0.0f)
	val isHorizontal: Boolean
		get() {
			val ax = abs(acc.x)
			val ay = abs(acc.y)
			return ax > ay
		}

	fun reset() {
		acc.x = 0f
		acc.y = 0f
	}

	fun movementIsNegligible(): Boolean {
		val ax = abs(acc.x)
		val ay = abs(acc.y)
		return TRIVIALITY_THRESHOLD > ay && TRIVIALITY_THRESHOLD > ax
	}

	companion object {
		const val TRIVIALITY_THRESHOLD = 50f
	}
}
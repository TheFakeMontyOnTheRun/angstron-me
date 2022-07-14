package br.odb.angstronme

/**
 * Created by monty on 6/6/15.
 */
class Player {
	@JvmField
	val position = Vec3(0.0f, 0.0f, 0.0f)
	@JvmField
	var direction: Directions? = null
	fun updatePosition() {
		when (direction) {
			Directions.N -> position.y--
			Directions.E -> position.x--
			Directions.S -> position.y++
			Directions.W -> position.x++
			else -> {}
		}
	}

	enum class Team {
		NOTHING, PLAYER, CPU
	}

	enum class Directions {
		N, E, S, W
	}
}
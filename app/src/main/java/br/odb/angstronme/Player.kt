package br.odb.angstronme

import br.odb.angstronme.Player.Team
import br.odb.angstronme.Vec3

/**
 * Created by monty on 6/6/15.
 */
class Player(val team: Team) {
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
		NOTHING, PLAYER, Team, CPU
	}

	enum class Directions {
		N, E, S, W
	}
}
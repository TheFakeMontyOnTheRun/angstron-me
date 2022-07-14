package br.odb.angstronme
/*
 * Vec3.java
 *
 * Created on 24 de Novembro de 2007, 03:53
 */ /**
 * @author daniel
 */
class Vec3(aX: Float, aY: Float, aZ: Float) {
	/**
	 * Creates a new instance of Vec3
	 */
	@JvmField
	var x = 0f
	@JvmField
	var y = 0f
	@JvmField
	var z = 0f
	operator fun set(aX: Float, aY: Float, aZ: Float) {
		x = aX
		y = aY
		z = aZ
	}

	init {
		set(aX, aY, aZ)
	}
}
package br.odb.angstronme;

/**
 * Created by monty on 6/6/15.
 */
public class SimpleGestureDetector {

	public static final float TRIVIALITY_THRESHOLD = 50;

	public Vec2 acc = new Vec2(0, 0);
	public Vec2 touch = new Vec2(0, 0);

	public boolean isHorizontal() {
		float ax = Math.abs(acc.x);
		float ay = Math.abs(acc.y);

		return (ax > ay);
	}

	public void reset() {
		acc.x = 0;
		acc.y = 0;
	}

	public boolean movementIsNegligible() {
		float ax = Math.abs(acc.x);
		float ay = Math.abs(acc.y);

		return (TRIVIALITY_THRESHOLD > ay) && (TRIVIALITY_THRESHOLD > ax);
	}
}

package br.odb.angstronme;

/*
 * Vec3.java
 *
 * Created on 24 de Novembro de 2007, 03:53
 */

/**
 * @author daniel
 */
public class Vec3 {

    /**
     * Creates a new instance of Vec3
     */
    public float x;
    public float y;
    public float z;

    public Vec3(float aX, float aY, float aZ) {
        set(aX, aY, aZ);
    }

    public void set(float aX, float aY, float aZ) {
        x = aX;
        y = aY;
        z = aZ;
    }
}

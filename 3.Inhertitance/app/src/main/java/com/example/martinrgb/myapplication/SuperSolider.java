package com.example.martinrgb.myapplication;

/**
 * Created by MartinRGB on 2016/12/16.
 */

public class SuperSolider extends Enemy {
    public SuperSolider(int hitPoints, int lives) {
        super(hitPoints, lives);
    }


    public void takeDamage(int damage){
        super.takeDamge(damage/2);
    }
}

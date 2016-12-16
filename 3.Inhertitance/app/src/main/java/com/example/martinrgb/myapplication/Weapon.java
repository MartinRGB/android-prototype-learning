package com.example.martinrgb.myapplication;

/**
 * Created by MartinRGB on 2016/11/18.
 */

public class Weapon {
    //定义变量
    private String weaponName;
    private int damagePoints;
    private int durabilityPoints;

    //结构体
    public Weapon(String weaponName, int damagePoints, int durabilityPoints) {
        super();
        this.weaponName = weaponName;
        this.damagePoints = damagePoints;
        this.durabilityPoints = durabilityPoints;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public void setWeaponName(String weaponName) {
        this.weaponName = weaponName;
    }

    public int getDamagePoints() {
        return damagePoints;
    }

    public void setDamagePoints(int damagePoints) {
        this.damagePoints = damagePoints;
    }

    public int getDurabilityPoints() {
        return durabilityPoints;
    }

    public void setDurabilityPoints(int durabilityPoints) {
        this.durabilityPoints = durabilityPoints;
    }

}

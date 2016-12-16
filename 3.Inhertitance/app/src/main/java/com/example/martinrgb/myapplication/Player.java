package com.example.martinrgb.myapplication;

import java.util.ArrayList;

/**
 * Created by MartinRGB on 2016/11/18.
 */

public class Player {
    private String handleName;
    private int lives;
    private int level;
    private  int score;
    private  Weapon weapon;
    private ArrayList<InventoryItem> inventoryitems;

    //结构体
    public Player(){
        setHandleName("无名氏");
        setLives(3);
        setLevel(1);
        setScore(0);
        setDefaultWeapon();
        inventoryitems = new ArrayList<InventoryItem>();
    }

    public ArrayList<InventoryItem> getInventoryitems() {
        return inventoryitems;
    }

    public void addInventoryItem(InventoryItem inventoryItem){
        inventoryitems.add(inventoryItem);
    }

    public boolean dropInventoryItem(InventoryItem inventoryItem){
        if(this.inventoryitems.contains(inventoryItem)){
            inventoryitems.remove(inventoryItem);
            return  true;
        }
        return  false;
    }

//    public void setInventoryitems(ArrayList<InventoryItem> inventoryitems) {
//        this.inventoryitems = inventoryitems;
//    }

    public Player(String handle){
        //等同于走了一遍上面的Player()
        this();
        setHandleName(handle);
    }

    //存取
    private void setDefaultWeapon(){
        this.weapon = new Weapon("Sword",10,100);
    }

    public void setNameAndLevel(String name,int level){
        this.handleName = name;
        this.level = level;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}

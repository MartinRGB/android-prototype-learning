package com.example.martinrgb.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Player martin = new Player("剑客");
        //martin.setNameAndLevel("安卓学习者",60);

        System.out.println("用户 Martin 的名字是" + martin.getHandleName());
        System.out.println("用户 Martin 使用的武器是" + martin.getWeapon().getWeaponName());

        Weapon myAxe = new Weapon("黄金战斧",15,50);
        martin.setWeapon(myAxe);
        System.out.println("用户 Martin 使用的武器是" + martin.getWeapon().getWeaponName());

        InventoryItem redPotion = new InventoryItem(ItemType.Potion,"红色药剂");
        martin.addInventoryItem(redPotion);
        martin.addInventoryItem(new InventoryItem(ItemType.Armor,"隐形药剂"));
        martin.addInventoryItem(new InventoryItem(ItemType.Armor,"青铜护甲"));
        martin.addInventoryItem(new InventoryItem(ItemType.Ring,"黄金戒指"));

        InventoryItem bluePotion = new InventoryItem(ItemType.Potion,"蓝色药剂");

        //因为dropInventroyItem里面写了返送是true还是false的方法。
        boolean wasDeleted = martin.dropInventoryItem(redPotion);
        System.out.println(wasDeleted);

        ArrayList<InventoryItem> allItems = martin.getInventoryitems();
        for(InventoryItem item: allItems){
            System.out.println(item.getName());
        }

        Enemy enemy = new Enemy(10,3);
        System.out.println("Hitpoints: " + enemy.getHitPoints() + "Lives:" + enemy.getLives());
        enemy.takeDamge(3);

        SuperSolider superSolider = new SuperSolider(10,3);
        System.out.println("Hitpoints: " + superSolider.getHitPoints() + "Lives:" + superSolider.getLives());
        superSolider.takeDamage(6);



    }
}

package com.example.martinrgb.a4whileloop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //WHILE LOOP

//        int[] elements = {5,7,12,100,8,-1,3};
//        int indexPos = 0;
//        int sumTotal = 0;
//
//        while(indexPos < elements.length)
//        {
//            System.out.println("Processing: " + indexPos + " Value: " + elements[indexPos]);
//
//            if(elements[indexPos]>99){
//
//                System.out.print("Continue!");
//                //跳过这一位
//                indexPos += 1;
//                //Forever 持续这个index的loop
//                continue;
//            }
//
//            if(elements[indexPos]<0){
//                //跳出whileloop
//                System.out.print("Break!");
//                break;
//            }
//
//
//            sumTotal = sumTotal + elements[indexPos];
//            indexPos++;
//        }
//
//        System.out.println("Sum total was:" + sumTotal);




        //FOR LOOP - 不设置索引号，拿数值本身来做循环

//        int[] elements = {5,7,12,100,8,-1,3};
//        int sumTotal = 0;
//
//       for(int elementContents : elements)
//        {
//            System.out.println("Processing: " + elementContents);
//
//            if(elementContents>99){
//
//                System.out.print("Continue!");
//                continue;
//            }
//
//            if(elementContents<0){
//                System.out.print("Break!");
//                break;
//            }
//
//
//            sumTotal = sumTotal + elementContents;
//        }
//
//        System.out.println("Sum total was:" + sumTotal);



        // Do While Loop

        int[] elements = {5,7,12,100,8,-1,3};
        int indexPos = 0;
        int sumTotal = 0;


        do{
            System.out.println("Processing: " + indexPos + " Value: " + elements[indexPos]);

            if(elements[indexPos]>99){

                System.out.print("Continue!");
                //跳过这一位
                indexPos += 1;
                //Forever 持续这个index的loop
                continue;
            }

            if(elements[indexPos]<0){
                //跳出whileloop
                System.out.print("Break!");
                break;
            }


            sumTotal = sumTotal + elements[indexPos];
            indexPos++;
        }while(indexPos < elements.length);

        System.out.println("Sum total was:" + sumTotal);




    }

}

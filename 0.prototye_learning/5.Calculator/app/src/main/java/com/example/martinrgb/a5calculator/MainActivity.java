package com.example.martinrgb.a5calculator;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText operand1;
    private EditText operand2;
    private Button btnAdd;
    private Button btnSub;
    private Button btnDiv;
    private Button btnMul;
    private TextView textResult;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deleteBars();
        setContentView(R.layout.activity_main);

        initType();

        btnAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                double oper1 = Double.parseDouble(operand1.getText().toString());
                double oper2 = Double.parseDouble(operand2.getText().toString());

                textResult.setText(Double.toString(oper1 + oper2));
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                double oper1 = Double.parseDouble(operand1.getText().toString());
                double oper2 = Double.parseDouble(operand2.getText().toString());

                textResult.setText(Double.toString(oper1 - oper2));
            }
        });

        btnDiv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                double oper1 = Double.parseDouble(operand1.getText().toString());
                double oper2 = Double.parseDouble(operand2.getText().toString());

                textResult.setText(Double.toString(oper1/oper2));
            }
        });

        btnMul.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                double oper1 = Double.parseDouble(operand1.getText().toString());
                double oper2 = Double.parseDouble(operand2.getText().toString());

                textResult.setText(Double.toString(oper1*oper2));
            }
        });
    }

    private void deleteBars(){
        //Delete Title Bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Delete Action Bar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
    }

    private void initType(){

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/DIN-Bold.otf");

        TextView myTextView = (TextView)findViewById(R.id.Title);
        myTextView.setTypeface(myTypeface);

        TextView myTextView2 = (TextView)findViewById(R.id.result);
        myTextView2.setTypeface(myTypeface);

        textResult = (TextView)findViewById(R.id.resultNumber);
        textResult.setTypeface(myTypeface);

        operand1 = (EditText)findViewById(R.id.line1);
        operand1.setTypeface(myTypeface);

        operand2 = (EditText)findViewById(R.id.line2);
        operand2.setTypeface(myTypeface);


        btnAdd = (Button)findViewById(R.id.button);
        btnAdd.setTypeface(myTypeface);

        btnSub = (Button)findViewById(R.id.button2);
        btnSub.setTypeface(myTypeface);


        btnMul = (Button)findViewById(R.id.button3);
        btnMul.setTypeface(myTypeface);

        btnDiv = (Button)findViewById(R.id.button4);
        btnDiv.setTypeface(myTypeface);


    }
}

package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;


import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.adapter.CategoryAdapter;
import com.example.todolist.model.Category;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity  {
    RecyclerView categoryRecycler;
    int count_task = 0;
    CategoryAdapter categoryAdapter;
    CheckBox categoryTitle;
    EditText text_input;
    Button button;
    RelativeLayout contentView;
    boolean isKeyboardShowing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_input = findViewById(R.id.text_input);
        button = (Button) findViewById(R.id.button);
        contentView = findViewById(R.id.contentView);
        categoryTitle = (CheckBox) findViewById(R.id.categoryTitle);
        categoryRecycler = findViewById(R.id.list);

        List<Category> categoryList = new ArrayList<>();








        button.setOnClickListener(v -> {
            if(text_input.getVisibility() == View.INVISIBLE) {
                text_input.setVisibility(View.VISIBLE);
                text_input.requestFocus();
                text_input.setFocusableInTouchMode(true);
                if(!checkLabelOnNothing(text_input.getText().toString())) {
                    text_input.setText("");
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(text_input, InputMethodManager.SHOW_FORCED);


                System.out.println("Click");

            }
            else if(text_input.getVisibility() == View.VISIBLE) {
                categoryList.add(new Category(count_task, text_input.getText().toString(), false));
                text_input.setText("");
                setCategoryRecycler(categoryList);
                count_task++;
            }

        });

        text_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {  // check state of keyboard and give new task in list
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && text_input.getText().length() == 0) {

                    System.out.println("Ok");
                    Toast.makeText(getApplicationContext(), "Некорректные данные", Toast.LENGTH_SHORT).show();

                    return true; // Focus will do whatever you put in the logic.
                }
                else if(actionId == EditorInfo.IME_ACTION_DONE && text_input.getText().length() != 0){
                    if(!checkLabelOnNothing(text_input.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Некорректные данные", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    System.out.println(text_input.getText().toString());
                    categoryList.add(new Category(count_task, text_input.getText().toString(), false));
                    text_input.setText("");
                    setCategoryRecycler(categoryList);
                    count_task++;
                    System.out.println("Ok 2");


                    return true;
                }

                else if (!isKeyboardShowing  && text_input.getText().length() > 0){
                    text_input.setVisibility(View.VISIBLE);
                    System.out.println("Ok 3");

                    return true;
                }
                return false;  // Focus will change according to the actionId
            }
        });




        contentView.getViewTreeObserver().addOnGlobalLayoutListener(     // method get event the hide or open keyboard from user
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        contentView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = contentView.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

//                        Log.d(TAG, "keypadHeight = " + keypadHeight);

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                onKeyboardVisibilityChanged(true);
                            }
                        }
                        else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                onKeyboardVisibilityChanged(false);
                                if(!checkLabelOnNothing(text_input.getText().toString())) {

                                    text_input.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }
                });


//        categoryList.add(new Category(1, "gg"));
//        categoryList.add(new Category(2, "gccccg"));
//
    }
    void onKeyboardVisibilityChanged(boolean opened ) {






        System.out.println("keyboard " + opened);
    }
    private void setCategoryRecycler(List<Category> categoryList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        categoryRecycler = findViewById(R.id.list);
        categoryRecycler.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(this,categoryList);

        categoryRecycler.setAdapter(categoryAdapter);

    }
    public boolean checkLabelOnNothing(String s){

        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) != ' ') {
                return true;
            }

        }

        return false;
    }


}


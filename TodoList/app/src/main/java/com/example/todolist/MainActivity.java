package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
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

import com.example.todolist.adapter.CategoryAdapter;
import com.example.todolist.model.Category;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    RecyclerView categoryRecycler;
    int count_task = 0;
    CategoryAdapter categoryAdapter;
    CheckBox categoryTitle;
    EditText text_input;
    Button button;
    RelativeLayout contentView;
    List<Category> categoryList;
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
//        loadArray(this);
        categoryList = PreConfig.readListFromPref(this);
        if(categoryList == null) {
            categoryList = new ArrayList<>();
        }
        for(int i = 0; i < categoryList.size(); i++){
            setCategoryRecycler(categoryList);

        }
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
            else if(text_input.getVisibility() == View.VISIBLE && !checkLabelOnNothing(text_input.getText().toString())){
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(text_input, InputMethodManager.SHOW_FORCED);
            }
            else if(text_input.getVisibility() == View.VISIBLE && checkLabelOnNothing(text_input.getText().toString())) {
                categoryList.add(new Category(count_task, text_input.getText().toString(), false));
                PreConfig.writeListInPref(getApplicationContext(),categoryList);
                text_input.setText("");
                text_input.setVisibility(View.INVISIBLE);
                setCategoryRecycler(categoryList);
                count_task++;
                System.out.println("GG");

            }

        });

        text_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {  // check state of keyboard and give new task in list
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && text_input.getText().length() == 0) {

                    System.out.println("Ok");

                    return false; // Focus will do whatever you put in the logic.
                }
                else if(actionId == EditorInfo.IME_ACTION_DONE && text_input.getText().length() != 0){
                    if(!checkLabelOnNothing(text_input.getText().toString())){
                        return false;
                    }

                    System.out.println(text_input.getText().toString());
                    categoryList.add(new Category(count_task, text_input.getText().toString(), false));
                    PreConfig.writeListInPref(getApplicationContext(),categoryList);
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

    @Override
    protected void onPause() {
        super.onPause();
        View view = this.getCurrentFocus();;
        if(view != null && !checkLabelOnNothing(text_input.getText().toString())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
        if(checkLabelOnNothing(text_input.getText().toString())){
            InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }


    //    public  boolean saveArray()
//    {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor mEdit1 = sp.edit();
//        /* sKey is an array */
//        mEdit1.putInt("Status_size", categoryList.size());
//
//        for(int i=0;i<categoryList.size();i++)
//        {
//            mEdit1.remove("Status_" + i);
//            mEdit1.putString("Status_" + i, categoryList.get(i).toString());
//        }
//
//        return mEdit1.commit();
//    }
//    public  void loadArray(Context mContext)
//    {
//        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(mContext);
//        categoryList.clear();
//        int size = mSharedPreference1.getInt("Status_size", 0);
//
//        for(int i=0; i <size; i++)
//        {
////            categoryList.add(new Category(count_task, text_input.getText().toString(), false));
//
//            categoryList.add(new Category(count_task,mSharedPreference1.getString("Status_" + i, "ale"),false));
//            setCategoryRecycler(categoryList);
//            count_task++;
//
//        }
//
//    }









}


package com.example.todolist.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    Context context;
    List<Category> categories;


    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoryItems = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(categoryItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.categoryTitle.setText(categories.get(position).getTask());
        holder.categoryTitle.setChecked(categories.get(position).isSelected());



        holder.categoryTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Кнопка Нет несёт за собой новые баги, поэтому она не нужна :)")
                        .setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                categories.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();
                                holder.categoryTitle.setChecked(false);
                                dialogInterface.cancel();

                            }
                        });



                if(isChecked) {
                    AlertDialog alert = alertDialog.create();
                    alert.setTitle("Удаление напоминания");
                    alert.setOnShowListener(arg0 -> {
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

                    });
                    alert.show();
                    System.out.println("Флажок выбран");
//                    categories.remove(holder.getAdapterPosition());
//                    notifyDataSetChanged();
                }
                else {
                    System.out.println("Флажок не выбран");
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public static final class CategoryViewHolder extends RecyclerView.ViewHolder{
        CheckBox categoryTitle;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);


        }
    }

}

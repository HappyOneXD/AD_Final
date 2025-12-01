package com.example.testing.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.Models.ExpenseModel;
import com.example.testing.R;

import java.util.ArrayList;

// Adapter de hien thi danh sach cac chi phi (expenses) trong RecyclerView
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseItemViewHolder> {
    // Danh sach cac expense can hien thi
    public ArrayList<ExpenseModel> expenseModels;
    // Context de truy cap tai nguyen va inflate layout
    public Context context;
    // Interface de xu ly su kien click vao item
    public OnClickListener onClickListener;

    // Interface dinh nghia phuong thuc onClick
    public interface OnClickListener {
        void onClick(int position);
    }

    // Thiet lap listener cho su kien click
    public void setOnClickListener(OnClickListener clickListener){
        this.onClickListener = clickListener;
    }

    // Constructor nhan vao danh sach expense va context
    public ExpenseAdapter(ArrayList<ExpenseModel> models, Context myContext){
        expenseModels = models;
        context = myContext;
    }

    // Tao ViewHolder moi khi can hien thi item moi
    @NonNull
    @Override
    public ExpenseAdapter.ExpenseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho tung item expense
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item_list, parent, false);
        return new ExpenseItemViewHolder(view);
    }

    // Gan du lieu vao ViewHolder tai vi tri cu the
    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ExpenseItemViewHolder holder, int position) {
        // Lay expense tai vi tri position
        ExpenseModel model = expenseModels.get(position);

        // Hien thi ten expense
        holder.tvNameExpense.setText(model.getName());
        // Hien thi so tien expense
        holder.tvAmountExpense.setText(String.valueOf(model.getAmount()));
        // Hien thi ten budget ma expense nay thuoc ve
        holder.tvBudgetName.setText(model.getBudgetName());

        // Xu ly su kien click vao item
        holder.viewItem.setOnClickListener(view -> {
            if (onClickListener != null){
                onClickListener.onClick(position);
            }
        });
    }

    // Tra ve so luong item trong danh sach
    @Override
    public int getItemCount() {
        return expenseModels.size();
    }

    // ViewHolder chua cac view cua mot item expense
    public class ExpenseItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvNameExpense, tvAmountExpense, tvBudgetName;
        View viewItem;

        // Constructor cua ViewHolder
        public ExpenseItemViewHolder(@NonNull View itemView) {
            super(itemView.getRootView());
            viewItem = itemView;
            // Anh xa cac view tu layout
            tvNameExpense = viewItem.findViewById(R.id.tvNameExpense);
            tvAmountExpense = viewItem.findViewById(R.id.tvAmountExpense);
            tvBudgetName = viewItem.findViewById(R.id.tvBudgetName);

            // Xu ly su kien click vao tung item
            viewItem.setOnClickListener(view -> {
                if (onClickListener != null){
                    // Lay vi tri cua item duoc click
                    onClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
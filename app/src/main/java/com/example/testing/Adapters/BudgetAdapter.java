package com.example.testing.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.Models.BudgetModel;
import com.example.testing.R;

import java.util.ArrayList;

// Adapter de hien thi danh sach cac budget trong RecyclerView
public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetItemViewHolder> {
    public ArrayList<BudgetModel> budgetModels;
    public Context context;
    public OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick(int position);
    }

    public void setOnClickListener(OnClickListener clickListener){
        this.onClickListener = clickListener;
    }

    public BudgetAdapter(ArrayList<BudgetModel> models, Context myContext){
        budgetModels = models;
        context = myContext;
    }

    @NonNull
    @Override
    public BudgetAdapter.BudgetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item_list, parent, false);
        return new BudgetItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetAdapter.BudgetItemViewHolder holder, int position) {
        BudgetModel model = budgetModels.get(position);

        // Hien thi ten budget
        holder.tvNameBudget.setText(model.getName());

        // Hien thi so tien ban dau cua budget
        holder.tvMoneyBudget.setText("Budget: " + formatCurrency(model.getMoney()) + " VND");

        // Hien thi so tien con lai
        int remaining = model.getRemainingMoney();
        holder.tvRemainingBudget.setText("Remaining: " + formatCurrency(remaining) + " VND");

        // Doi mau chu neu so tien con lai am (chi tieu vuot qua budget)
        if (remaining < 0){
            holder.tvRemainingBudget.setTextColor(Color.parseColor("#F44336")); // Mau do
        } else {
            holder.tvRemainingBudget.setTextColor(Color.parseColor("#4CAF50")); // Mau xanh
        }

        holder.viewItem.setOnClickListener(view -> {
            if (onClickListener != null){
                onClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return budgetModels.size();
    }

    // Dinh dang so tien theo format currency (1,000,000)
    private String formatCurrency(int amount){
        return String.format("%,d", amount);
    }

    public class BudgetItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvNameBudget, tvMoneyBudget, tvRemainingBudget;
        View viewItem;

        public BudgetItemViewHolder(@NonNull View itemView) {
            super(itemView);
            viewItem = itemView;
            tvNameBudget = viewItem.findViewById(R.id.tvNameBudget);
            tvMoneyBudget = viewItem.findViewById(R.id.tvMoneyBudget);
            tvRemainingBudget = viewItem.findViewById(R.id.tvRemainingBudget);

            viewItem.setOnClickListener(view -> {
                if (onClickListener != null){
                    onClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}

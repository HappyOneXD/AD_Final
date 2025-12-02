package com.example.testing.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.Models.BudgetModel;
import com.example.testing.R;

import java.util.ArrayList;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetItemViewHolder> {
    public ArrayList<BudgetModel> budgetModels;
    public Context context;
    public OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick(int position);
    }

    public void setOnClickListener(OnClickListener clickListener) {
        this.onClickListener = clickListener;
    }

    public BudgetAdapter(ArrayList<BudgetModel> models, Context myContext) {
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
        BudgetModel model = budgetModels.get(position); // lay du lieu theo vi tri tung dong trong list item budget
        // Hien thi so tien voi dinh dang currency
        holder.tvMoneyBudget.setText(formatCurrency(model.getMoney()) + " VND");
        holder.tvNameBudget.setText(model.getName());
        holder.viewItem.setOnClickListener(view -> {
            if (onClickListener != null){
                onClickListener.onClick(position);
            }
        });
    }

    // Dinh dang so tien theo format currency (1,000,000)
    private String formatCurrency(int amount){
        return String.format("%,d", amount);
    }

    @Override
    public int getItemCount() {
        return budgetModels.size(); // bao nhieu du lieu trong database can hien thi
    }

    public class BudgetItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameBudget, tvMoneyBudget;
        View viewItem;

        public BudgetItemViewHolder(@NonNull View itemView) {
            super(itemView.getRootView());
            viewItem = itemView;
            tvNameBudget = viewItem.findViewById(R.id.tvNameBudget);
            tvMoneyBudget = viewItem.findViewById(R.id.tvMoneyBudget);
            viewItem.setOnClickListener(view -> {
                if (onClickListener != null) {
                    onClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}

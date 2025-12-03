package com.example.testing.Models;

public class BudgetModel {
    private int id;
    private String name;
    private int money;
    private String description;
    private int status;
    private int user_id;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    // So tien con lai sau khi tru expense
    private int remainingMoney;

    public BudgetModel(int id, String name, int money, String description, int status, int user_id, String createdAt, String updatedAt, String deletedAt) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.description = description;
        this.status = status;
        this.user_id = user_id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.remainingMoney = money; // Mac dinh bang voi tong tien
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }
    public int getRemainingMoney() {
        return remainingMoney;
    }

    public void setRemainingMoney(int remainingMoney) {
        this.remainingMoney = remainingMoney;
    }
}

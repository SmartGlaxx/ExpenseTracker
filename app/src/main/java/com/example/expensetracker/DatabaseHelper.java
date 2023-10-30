package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "smartDB2";
    private static final String TABLE_USERS = "usertable";
    private static final String USER_ID = "id";
    private static final String USER_FIRST_NAME = "firstname";
    private static final String USER_LAST_NAME = "lastname";
    private static final String USER_EMAIL = "email";
    private static final String USER_PHONE = "phone";
    private static final String USER_PASSWORD = "password";


    private static final String TABLE_INCOME = "incometable"; // Constants for the income table
    private static final String INCOME_ID = "income_id"; // Primary key for the income table
    private static final String INCOME_SOURCE = "income_source";
    private static final String INCOME_AMOUNT = "income_amount";
    private static final String INCOME_DATE = "income_date";

    private static final String TABLE_EXPENSES = "expensestable";
    private static final String EXPENSE_ID = "expense_id";
    private static final String EXPENSE_ITEM = "expense_item";
    private static final String EXPENSE_CATEGORY = "expense_category";
    private static final String EXPENSE_AMOUNT = "expense_amount";
    private static final String EXPENSE_DATE = "expense_date";


    private static final String TABLE_BUDGET = "budgettable";
    private static final String BUDGET_ID = "id";
    private static final String BUDGET_AMOUNT = "amount";

    private static final String BUDGET_CATEGORY = "budget_category";
    private static final String BUDGET_NOTE = "note";




    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_USERS + "("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_FIRST_NAME + " Text, "
                + USER_LAST_NAME + " Text, "
                + USER_EMAIL + " Text, "
                + USER_PHONE + " Text, "
                + USER_PASSWORD + " Text);";


        String createIncomeTable = "CREATE TABLE " + TABLE_INCOME + "("
                + INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + INCOME_SOURCE + " TEXT, "
                + INCOME_AMOUNT + " REAL, "
                + INCOME_DATE + " TEXT);";

        String createExpenseTable = "CREATE TABLE " + TABLE_EXPENSES + "("
                + EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EXPENSE_ITEM + " TEXT, "
                + EXPENSE_CATEGORY + " TEXT, "
                + EXPENSE_AMOUNT + " REAL, "
                + EXPENSE_DATE + " TEXT);";

        String createBudgetTable = "CREATE TABLE " + TABLE_BUDGET + "("
                + BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BUDGET_AMOUNT + " REAL, "
                + BUDGET_CATEGORY + " TEXT, "
                + BUDGET_NOTE + " TEXT"
                + ");";

        db.execSQL(createUserTable);
        db.execSQL(createIncomeTable);
        db.execSQL(createExpenseTable);
        db.execSQL(createBudgetTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    int insertNewUser(User user) {
        int result = -1;

        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String phone = user.getPhone();
        String password = user.getPassword();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(USER_FIRST_NAME, firstName);
        cValues.put(USER_LAST_NAME, lastName);
        cValues.put(USER_EMAIL, email);
        cValues.put(USER_PHONE, phone);
        cValues.put(USER_PASSWORD, password);
        Log.d("Val", firstName + " " + lastName + " " + email + " " + phone + " " + password);

        try {
            long newRowId = db.insertOrThrow(TABLE_USERS, null, cValues);
            if (newRowId != -1) {
                result = 1;
            }
        } catch (SQLException e) {
            Log.e("Database Insert Error", e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return result;
    }


    public boolean doesEmailExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] columns = { USER_EMAIL };

            String selection = USER_EMAIL + " = ?";

            String[] selectionArgs = { email };

            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Log.e("Database Query Error", e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }


    public boolean login(String email, String enteredPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] columns = { USER_EMAIL, USER_PASSWORD };

            String selection = USER_EMAIL + " = ?";

            String[] selectionArgs = { email };

            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {

                String storedHashedPassword = cursor.getString(cursor.getColumnIndex(USER_PASSWORD));

                if (verifyPassword(enteredPassword, storedHashedPassword)) {
                    return true;
                }
            }

            return false;
        } catch (SQLException e) {
            Log.e("Database Query Error", e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    private boolean verifyPassword(String enteredPassword, String storedHashedPassword) {
        String hashedEnteredPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] passwordBytes = enteredPassword.getBytes();

            byte[] hashedBytes = md.digest(passwordBytes);

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            hashedEnteredPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return hashedEnteredPassword.equals(storedHashedPassword);
    }



    public String getFirstNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] columns = { USER_FIRST_NAME };
            String selection = USER_EMAIL + " = ?";
            String[] selectionArgs = { email };

            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(USER_FIRST_NAME));
            }

            return null; // Return null if no matching email is found
        } catch (SQLException e) {
            Log.e("Database Query Error", e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }


    public long insertNewIncome(Income income) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(INCOME_SOURCE, income.getSource());
        cValues.put(INCOME_AMOUNT, income.getAmount());
        cValues.put(INCOME_DATE, income.getDate());

        long newRowId = db.insert(TABLE_INCOME, null, cValues);
        db.close();
        return newRowId;
    }

    public long insertNewExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(EXPENSE_ITEM, expense.getItem());
        cValues.put(EXPENSE_CATEGORY, expense.getCategory());
        cValues.put(EXPENSE_AMOUNT, expense.getAmount());
        cValues.put(EXPENSE_DATE, expense.getDate());

        long newRowId = db.insert(TABLE_EXPENSES, null, cValues);
        db.close();
        return newRowId;
    }

    public long insertNewBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BUDGET_AMOUNT, budget.getAmount());
        values.put(BUDGET_CATEGORY, budget.getCategory());
        values.put(BUDGET_NOTE, budget.getNote());

        long newRowId = db.insert(TABLE_BUDGET, null, values);
        db.close();
        return newRowId;
    }


    public List<Expense> getAllExpenses() {
        List<Expense> expenseList = new ArrayList<>();

        // Define the columns you want to retrieve from the expenses table
        String[] columns = {
                EXPENSE_ID,
                EXPENSE_ITEM,
                EXPENSE_CATEGORY,
                EXPENSE_AMOUNT,
                EXPENSE_DATE
        };

        // Create a readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database to retrieve all rows from the expenses table
        Cursor cursor = db.query(TABLE_EXPENSES, columns, null, null, null, null, null);

        // Loop through the cursor to create Expense objects and add them to the list
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(EXPENSE_ID));
                String item = cursor.getString(cursor.getColumnIndex(EXPENSE_ITEM));
                String category = cursor.getString(cursor.getColumnIndex(EXPENSE_CATEGORY));
                double amount = cursor.getDouble(cursor.getColumnIndex(EXPENSE_AMOUNT));
                String date = cursor.getString(cursor.getColumnIndex(EXPENSE_DATE));


                Expense expense = new Expense(item, category, amount, date);
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return expenseList;
    }

    public List<CategorySum> getExpensesByCategorySum() {
        List<CategorySum> categorySums = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + EXPENSE_CATEGORY + ", SUM(" + EXPENSE_AMOUNT + ") AS " + EXPENSE_AMOUNT +
                " FROM " + TABLE_EXPENSES +
                " GROUP BY " + EXPENSE_CATEGORY;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String category = cursor.getString(cursor.getColumnIndex(EXPENSE_CATEGORY));
                    double sum = cursor.getDouble(cursor.getColumnIndex(EXPENSE_AMOUNT));
                    categorySums.add(new CategorySum(category, sum));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return categorySums;
    }

    public List<BudgetCategorySum> getBudgetByCategorySum() {
        List<BudgetCategorySum> categorySums = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + BUDGET_CATEGORY + ", SUM(" + BUDGET_AMOUNT + ") AS " + BUDGET_AMOUNT +
                " FROM " + TABLE_BUDGET +
                " GROUP BY " + BUDGET_CATEGORY;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String category = cursor.getString(cursor.getColumnIndex(BUDGET_CATEGORY));
                    double sum = cursor.getDouble(cursor.getColumnIndex(BUDGET_AMOUNT));
                    categorySums.add(new BudgetCategorySum(category, sum));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return categorySums;
    }


    public double getIncomeSum() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + INCOME_AMOUNT + ") AS incomeSum FROM " + TABLE_INCOME;
        Cursor cursor = db.rawQuery(query, null);

        double incomeSum = 0.0;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                incomeSum = cursor.getDouble(cursor.getColumnIndex("incomeSum"));
            }
            cursor.close();
        }

        db.close();
        return incomeSum;
    }


    public double getExpenseSum() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + EXPENSE_AMOUNT + ") AS expenseSum FROM " + TABLE_EXPENSES;
        Cursor cursor = db.rawQuery(query, null);

        double expenseSum = 0.0;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                expenseSum = cursor.getDouble(cursor.getColumnIndex("expenseSum"));
            }
            cursor.close();
        }

        db.close();
        return expenseSum;
    }


}

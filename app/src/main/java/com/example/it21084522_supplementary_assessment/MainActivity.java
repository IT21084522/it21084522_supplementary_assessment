package com.example.it21084522_supplementary_assessment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.example.supplementary.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edtitem, edtprice;

    ApiInterface apiInterface;

    RecyclerView popularRecyclerView, recommendedRecyclerView, allMenuRecyclerView;

    PopularAdapter popularAdapter;
    RecommendedAdapter recommendedAdapter;
    AllMenuAdapter allMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtitem = findViewById(R.id.editTextTextName);
        edtprice = findViewById(R.id.editTextTextPrice);

        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<List<FoodData>> call = apiInterface.getAllData();
        call.enqueue(new Callback<List<FoodData>>() {
            @Override
            public void onResponse(Call<List<FoodData>> call, Response<List<FoodData>> response) {

                List<FoodData> foodDataList = response.body();


                getPopularData(foodDataList.get(0).getPopular());

                getRecommendedData(foodDataList.get(0).getRecommended());

                getAllMenu(foodDataList.get(0).getAllmenu());
            }

            @Override
            public void onFailure(Call<List<FoodData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Server is not responding.", Toast.LENGTH_SHORT).show();
            }
    }


        private void  getPopularData(List<Popular> popularList){

            popularRecyclerView = findViewById(R.id.popular_recycler);
            popularAdapter = new PopularAdapter(this, popularList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            popularRecyclerView.setLayoutManager(layoutManager);
            popularRecyclerView.setAdapter(popularAdapter);

        }

        private void  getRecommendedData(List<Recommended> recommendedList){

            recommendedRecyclerView = findViewById(R.id.recommended_recycler);
            recommendedAdapter = new RecommendedAdapter(this, recommendedList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recommendedRecyclerView.setLayoutManager(layoutManager);
            recommendedRecyclerView.setAdapter(recommendedAdapter);

        }

        private void  getAllMenu(List<Allmenu> allmenuList){

            allMenuRecyclerView = findViewById(R.id.all_menu_recycler);
            allMenuAdapter = new AllMenuAdapter(this, allmenuList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            allMenuRecyclerView.setLayoutManager(layoutManager);
            allMenuRecyclerView.setAdapter(allMenuAdapter);
            allMenuAdapter.notifyDataSetChanged();

        }


    public void saveUser(View view){
        String item = edtitem.getText().toString();
        String price = edtprice.getText().toString();

        DBHelper dbHelper = new DBHelper(this);

        if(item.isEmpty()||price.isEmpty()){
            Toast.makeText(this, "Enter values", Toast.LENGTH_SHORT).show();
        }else{
            long inserted = dbHelper.addInfo(item,price);

            if(inserted>0){
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Something went wrong ;(", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void viewAll(View view){
        DBHelper dbHelper = new DBHelper(this);

        List info = dbHelper.readAllInfo();
        List items = new ArrayList<Integer>();

        String[] data = (String[]) info.toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Items");
        builder.setItems(data, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userName = data[i].split(":")[0];
                edtitem.setText(item);
                edtprice.setText(price);
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void deleteUser(View view){
        DBHelper dbHelper = new DBHelper(this);

        String name = edtName.getText().toString();

        if(name.isEmpty()){
            Toast.makeText(this, "Please select user to Delete", Toast.LENGTH_SHORT).show();
        }else{
            dbHelper.deleteInfo(item);
            Toast.makeText(this, "User deleted!", Toast.LENGTH_SHORT).show();

            edtitem.setText("");
            edtprice.setText("");

        }

        public void updateUser(View view){
            DBHelper dbHelper = new DBHelper(this);
            String item = edtitem.getText().toString();
            String price = edtprice.getText().toString();

            if(item.isEmpty()||price.isEmpty()){

            }else{
                dbHelper.updateInfo(view,item,price);
            }

        }



    }
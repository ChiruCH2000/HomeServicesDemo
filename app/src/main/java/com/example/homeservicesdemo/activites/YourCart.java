package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.adapters.CartAdapter;
import com.example.homeservicesdemo.helperclass.CartItems;
import com.example.homeservicesdemo.helperclass.DataBaseHelper;
import com.example.homeservicesdemo.helperclass.SelectedServiceDetails;

import java.util.List;

public class YourCart extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImageView mimgBackIcon;
    private DataBaseHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_cart);

        InitializViews();
        mDbHelper = new DataBaseHelper(this);

        // Retrieve the data from the database
        List<CartItems> cartItemsList = mDbHelper.getAllCartItems();

        CartAdapter cartAdapter = new CartAdapter(YourCart.this,cartItemsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(YourCart.this));
        mRecyclerView.setAdapter(cartAdapter);
        mimgBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void InitializViews() {

        mRecyclerView =findViewById(R.id.activity_your_cart_recyclerView);
        mimgBackIcon = findViewById(R.id.activity_your_cart_img_backIcon);

    }
}
package com.example.food.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.food.Adapter.SanPhamAdapter;
import com.example.food.Model.SanPhamModels;
import com.example.food.Presenter.SanPhamPreSenter;
import com.example.food.Presenter.SanPhamView;
import com.example.food.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity  extends AppCompatActivity{
    private FirebaseFirestore db;
    private TextView textView;
    private ImageView imageView;
    private  ArrayList<SanPhamModels> arrayList;
    private RecyclerView rCvSP;
    private SanPhamAdapter sanPhamAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        InitWidget();
        arrayList  =new ArrayList<>();
        Init();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void Init() {
        db= FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        if(intent.hasExtra("KEY")){ arrayList.clear();
            if(sanPhamAdapter!=null){
                sanPhamAdapter.notifyDataSetChanged();
            }
            String key = Objects.requireNonNull(intent.getStringExtra("KEY")).trim();
            textView.setText(key);
            db.collection("SanPham").orderBy("tensp").
                    startAt(key).endAt(key+'\uf8ff').
                    get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.size()>0){
                                for(QueryDocumentSnapshot d : queryDocumentSnapshots){
                                    arrayList.add(d.toObject(SanPhamModels.class));
                                }
                                sanPhamAdapter = new SanPhamAdapter(SearchActivity.this,arrayList,2);
                                rCvSP.setLayoutManager(new GridLayoutManager(SearchActivity.this,2));
                                rCvSP.setAdapter(sanPhamAdapter);
                            }

                        }
                    });
        }
        imageView.setOnClickListener(v->finish());
    }

    private void InitWidget() {
        rCvSP = findViewById(R.id.rcvDanhMuc);
       imageView = findViewById(R.id.btn_back);
        textView=findViewById(R.id.text_title);
    }
}

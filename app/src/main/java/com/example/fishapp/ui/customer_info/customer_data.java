package com.example.fishapp.ui.customer_info;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fishapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class customer_data extends AppCompatActivity {
    ListView listView;
    ArrayList<String> fishList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    // Firestore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_data);

        listView = findViewById(R.id.list);

        // Set up ArrayAdapter for ListView
        arrayAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item,
                fishList);
        listView.setAdapter(arrayAdapter);

        // Fetch data from Firestore
        fetchDataFromFirestore();
    }

    private void fetchDataFromFirestore() {
        // Reference to the "Data" collection
        CollectionReference dataCollection = db.collection("Data");

        dataCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    fishList.clear(); // Clear the list to avoid duplicates
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String fishData = document.getString("data"); // Adjust field name as needed
                        if (fishData != null) {
                            fishList.add(fishData);
                        }
                    }
                    // Notify the adapter of the data change
                    arrayAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(customer_data.this,
                            "Error fetching data: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", task.getException().getMessage());
                }
            }
        });
    }
}

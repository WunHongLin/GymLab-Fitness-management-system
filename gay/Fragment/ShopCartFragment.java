package com.example.gay.Fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.gay.Adapter.shopAdapter;
import com.example.gay.R;
import com.example.gay.ShopDetailActivity;
import com.example.gay.shop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopCartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button goCar;
    private CollectionReference dbCR;
    private DocumentReference dbDR;
    private ArrayList<String> shopRecyclerTitle = new ArrayList<String>();
    private shopAdapter shopAdapter;

    public ShopCartFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ShopCartFragment newInstance(String param1, String param2) {
        ShopCartFragment fragment = new ShopCartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_shop_cart, container, false);
        RecyclerView shopRecycler1 = (RecyclerView) view.findViewById(R.id.shopRecycler);
        shopRecycler1.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("User").document(user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String name = task.getResult().getString("userName");
                String image  =task.getResult().getString("userImage");
                CircleImageView userShopImage=  view.findViewById(R.id.ShopUserImage);
                TextView userNameShop = view.findViewById(R.id.textView3);
                userNameShop.setText(name);
                if (getActivity() != null) {
                    Glide.with(getActivity()).load(image).into(userShopImage);
                }
            }
        });
        shopRecyclerTitle.clear();

        //download the category from the database
        dbCR = FirebaseFirestore.getInstance().collection("/productCategory");
        dbCR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot document:task.getResult()){ shopRecyclerTitle.add(document.getId()); }
                shopAdapter = new shopAdapter(shopRecyclerTitle,getContext());
                shopRecycler1.setAdapter(shopAdapter);
            }
        });

        goCar = (Button) view.findViewById(R.id.button);
        goCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShopDetailActivity.class);
                startActivity(intent);
            }
        });
        //set items event
        return view;
    }
}
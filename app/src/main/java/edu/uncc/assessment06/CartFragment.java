package edu.uncc.assessment06;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uncc.assessment06.databinding.CartRowItemBinding;
import edu.uncc.assessment06.databinding.FragmentCartBinding;
import edu.uncc.assessment06.databinding.ProductRowItemBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class CartFragment extends Fragment {

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentCartBinding binding;
    ArrayList<Product> products = new ArrayList<>();
    CartAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Cart");

        adapter = new CartAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser userID = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("cart")
                .whereEqualTo("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {


                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            products.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                Product product = doc.toObject(Product.class);
                                products.add(product);
                            }
                            adapter.notifyDataSetChanged();
                            calculateTotal();
                        }
                    }
                });
    }

    private void calculateTotal() {
        double total = 0.0;
        for (int i = 0; i < products.size(); i++) {
            String price = products.get(i).getPrice();
            double priceValue = Double.parseDouble(price);
            total += priceValue;
        }
        binding.textViewTotal.setText("Total : $" + total);
        System.out.println("Item total is" + total);
    }
    class CartAdapter extends RecyclerView.Adapter<CartFragment.CartAdapter.CartViewHolder>{


        @NonNull
        @Override
        public CartFragment.CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CartFragment.CartAdapter.CartViewHolder(CartRowItemBinding.inflate(getLayoutInflater(), parent, false));
        }


        @Override
        public void onBindViewHolder(@NonNull CartFragment.CartAdapter.CartViewHolder holder, int position) {
            Product product = products.get(position);
            holder.setupUI(product);
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        class CartViewHolder extends RecyclerView.ViewHolder{
            CartRowItemBinding mBinding;
            Product mProduct;
            public CartViewHolder(CartRowItemBinding rowItemBinding) {
                super(rowItemBinding.getRoot());
                mBinding = rowItemBinding;
            }

            void setupUI(Product product){
                this.mProduct = product;
                System.out.println("Product is, " + product);
                mBinding.textViewProductName.setText(product.getName());
                mBinding.textViewProductPrice.setText("$" + product.getPrice());
                Picasso.get().load(product.getImg_url()).into(mBinding.imageViewProductIcon);

                mBinding.imageViewDeleteFromCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String docID = product.getDocId();
                        System.out.println("Clicked on document ID" + docID);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("cart").document(docID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        }
    }
}
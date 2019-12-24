package com.example.uitest;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

     public TextView carplate,model,insurance;

    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);

        carplate=itemView.findViewById(R.id.carplate_text);
        model=itemView.findViewById(R.id.carmodel_text);
        insurance=itemView.findViewById(R.id.insurancs_text);

    }
}

package com.example.fbkelasb.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbkelasb.MainActivity;
import com.example.fbkelasb.R;
import com.example.fbkelasb.TemanEdit;
import com.example.fbkelasb.database.Teman;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterTeman extends RecyclerView.Adapter<AdapterTeman.ViewHolder> {

    private ArrayList<Teman> daftarTeman;
    private Context context;
    private DatabaseReference databaseReference;

    public AdapterTeman(ArrayList<Teman> teman, Context ctx) {
        daftarTeman = teman;
        context = ctx;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama;

        ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);

            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teman,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String kode, nama, telpon;

        nama = daftarTeman.get(position).getNama();
        kode = daftarTeman.get(position).getKode();
        telpon = daftarTeman.get(position).getTelpon();

        holder.tvNama.setText(nama);

        holder.tvNama.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.inflate(R.menu.menuteman);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnEdit:
                                Bundle bundle = new Bundle();
                                bundle.putString("Kunci1", kode);
                                bundle.putString("Kunci2", nama);
                                bundle.putString("Kunci3", telpon);

                                Intent intent = new Intent(view.getContext(), TemanEdit.class);
                                intent.putExtras(bundle);
                                view.getContext().startActivity(intent);
                                break;

                            case R.id.mnHapus:
                                AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
                                alertDlg.setTitle("Yakin data " + nama + " akan dihapus?");
                                alertDlg.setMessage("Tekan 'Ya' untuk menghapus")
                                        .setCancelable(false)
                                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which) {
                                                DeleteData(kode);
                                                Toast.makeText(view.getContext(), "Data " + nama + " berhasil dihapus", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(view.getContext(), MainActivity.class);
                                                view.getContext().startActivity(intent);

                                            }
                                        })
                                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog aDlg = alertDlg.create();
                                aDlg.show();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return daftarTeman.size();
    }

    public void DeleteData(String kode){
        if (databaseReference != null)
        {
            databaseReference.child("Teman").child(kode).removeValue();
        }
    }
}

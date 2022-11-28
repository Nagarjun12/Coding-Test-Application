package com.example.myapplication.listadapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.db.DBAdapter;
import com.example.myapplication.model.ListModel;
import java.util.ArrayList;

public class DBListDetailAdapter extends RecyclerView.Adapter<DBListDetailAdapter.MyViewHolder> {

    private ArrayList<ListModel> list;
    private final Activity activity;
    private int layout;
    private DBAdapter dbAdapter;

    public DBListDetailAdapter(Activity activity, ArrayList<ListModel> list) {
        this.activity = activity;
        this.list = list;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtUserId;
        private final TextView txtUsername;
        private final ImageView ivEdit;
        private final ImageView ivDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtUserId = itemView.findViewById(R.id.txtUserID);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        dbAdapter = new DBAdapter(activity);
        dbAdapter.open();

        ListModel userModel = list.get(position);
        holder.txtUserId.setText("User ID : " + userModel.getUserId());
        holder.txtUsername.setText(userModel.getUsername());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog(userModel.userId, position);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbAdapter.deleteLogs(userModel.userId);

                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
                notifyDataSetChanged();

                Toast.makeText(activity, "Delete Successful !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(ArrayList<ListModel> list) {
        this.list = list;
        notifyItemRangeInserted(0, this.list.size());
    }

    protected void editDialog(int userId, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alerdialog_edit, null);
        dialogBuilder.setView(dialogView);

        EditText etUsername = dialogView.findViewById(R.id.et_username);
        Button btnSubmit = dialogView.findViewById(R.id.btn_submit);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbAdapter.updateUsername(userId, etUsername.getText().toString());

                notifyItemChanged(position, list.size());
                notifyItemChanged(position);
                activity.recreate();
                alertDialog.dismiss();

                Toast.makeText(activity, "Update Successful !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

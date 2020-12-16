package io.github.eperatis.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    DBHelper helper;
    EditText TxtTitle, TxtDetail;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxtTitle = (EditText)findViewById(R.id.txTitle_Edit);
        TxtDetail = (EditText)findViewById(R.id.txDetail_Edit);

        getData();
    }

    private void getData() {
        Cursor cursor = helper.oneData(id);
        if(cursor.moveToFirst()){
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.row_title));
            String detail = cursor.getString(cursor.getColumnIndex(DBHelper.row_note));

            TxtTitle.setText(title);
            TxtDetail.setText(detail);
        }
    }

    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_edit:
                String title = TxtTitle.getText().toString().trim();
                String detail = TxtDetail.getText().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_title, title);
                values.put(DBHelper.row_note, detail);

                if (title.equals("") && detail.equals("")) {
                    Toast.makeText(EditActivity.this, "Nem törtnt változás.", Toast.LENGTH_SHORT).show();
                } else {
                    helper.updateData(values, id);
                    Toast.makeText(EditActivity.this, "Mentve", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }

        switch (item.getItemId()){
            case R.id.delete_edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setMessage("Ez a jegyzet törölve lesz.");
                builder.setCancelable(true);
                builder.setPositiveButton("Törlés", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.deleteData(id);
                        Toast.makeText(EditActivity.this, "Törölve", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
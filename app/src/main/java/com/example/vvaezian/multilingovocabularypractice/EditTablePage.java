package com.example.vvaezian.multilingovocabularypractice;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditTablePage extends AppCompatActivity {

    DatabaseHelper myDB;
    EditText etEN, etFR, etDelete;
    Button btnAddData;
    Button btnShowData;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_table_page);

        etEN = (EditText) findViewById(R.id.etEN);
        etFR = (EditText) findViewById(R.id.etFR);
        etDelete = (EditText)  findViewById(R.id.etDelete);
        btnAddData = (Button) findViewById(R.id.btnAddData);
        btnShowData = (Button) findViewById(R.id.btnShowData);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        myDB = new DatabaseHelper(this);

        AddData();
        ShowData();
        DeleteRow();
    }

    public void AddData(){
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isInserted = myDB.insertData(etEN.getText().toString(),
                                                             etFR.getText().toString());
                        if (isInserted)
                            Toast.makeText(EditTablePage.this, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(EditTablePage.this, "Data Insertion Failed", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    public void ShowData() {
        btnShowData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor res = myDB.getAllData();
                        if (res.getCount() == 0) {
                            showMessage("Error", "Nothing Found!");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("ID: " + res.getString(0) + "\n");
                            buffer.append("EN: " + res.getString(1) + "\n");
                            buffer.append("FR: " + res.getString(2) + "\n\n");
                        }

                        showMessage("Data", buffer.toString());

                    }
                }
        );
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void DeleteRow() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer deletedRows = myDB.deleteData(etDelete.getText().toString());
                        if (deletedRows > 0)
                            Toast.makeText(EditTablePage.this, "Data Deleted Successfully", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(EditTablePage.this, "Data Deletion Failed", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


}

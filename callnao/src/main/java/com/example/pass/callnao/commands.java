package com.example.pass.callnao;

/**
 * Created by HiddenVinnyP on 09.04.2014.
 */
import android.app.Dialog;
import android.app.ListActivity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.Toast;

public class commands extends ListActivity{

    Dialog dialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(GlobalValues.getInstance().nao.getNaoOnLine()){
            loadData();
        }else
        {
        }

        dialog = new Dialog(this);
        dialog.setTitle("�������� ��������");
        dialog.setContentView(R.layout.dialog);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
                String item = text.getText().toString();
                GlobalValues.getInstance().nao.stopAllBehavior();
                GlobalValues.getInstance().nao.sendBehavior(item);
                dialog.dismiss();
            }
        });

        Button dalogButton = (Button) dialog.findViewById(R.id.dialogButtonCANCEL);
        // if button is clicked, close the custom dialog
        dalogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalValues.getInstance().nao.stopAllBehavior();
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        //ClobalValues.getInstance().nao.sendBehavior(item);
        TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
        text.setText(item);
        dialog.show();
        //Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }

    /*public void clickStartBehavior(View view){
    	TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
    	String item = text.getText().toString();
    	ClobalValues.getInstance().nao.stopAllBehavior();
    	ClobalValues.getInstance().nao.sendBehavior(item);
    }*/
    public void clickStopBehavior(View view){
        GlobalValues.getInstance().nao.stopAllBehavior();
    }


    public void loadData(){
        String[] values = GlobalValues.getInstance().nao.getBehaviors();
        // ������������� ������������ �������
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.listview, R.id.label, values);
        setListAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(GlobalValues.getInstance().nao.getNaoOnLine()){
            loadData();
        }else
        {
            Toast.makeText(this, "����� �� ���������", Toast.LENGTH_LONG).show();
        }
    }
}

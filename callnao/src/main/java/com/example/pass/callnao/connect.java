package com.example.pass.callnao;

/**
 * Created by HiddenVinnyP on 09.04.2014.
 */

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.widget.TextView;

public class connect extends Activity {
    FindNao m_find_nao;
    WifiManager wifiManager ;
    WifiInfo wifiInfo ;
    int ipAddress;
    TextView tw;
    Button bt;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_connect);
        //loadData();

    }
/*    public void loadData(){
        tw=(TextView)(findViewById(R.id.ip));
        Button bt = (Button)(findViewById(R.id.connect));
        if(GlobalValues.getInstance().nao.isNaoOnline())
        {
            tw.setText(GlobalValues.getInstance().nao.getStringIP());
            bt.setText("����� ������");
            bt.setEnabled(false);

        }else{

            final WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            final int ipAddress = wifiInfo.getIpAddress();

            tw.setText(String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), 0));
            bt.setText("����� ������");
            bt.setEnabled(true);
            Toast.makeText(this, "����� �� ���������", Toast.LENGTH_LONG).show();
        }
    }*/
    public void clickConnect(View view)
    {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi= connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(!mWifi.isConnected())
        {
            final Intent intent = new Intent(Intent.ACTION_MAIN,null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            final ComponentName cn = new ComponentName("com.android.settings","com.android.settings.wifi.WifiSettings");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else{
            m_find_nao= new FindNao();
            m_find_nao.execute();
        }


    }
    class FindNao extends AsyncTask<Void, Void, Void>{
        String ip="";
        String inputip ="";
        Button bt = (Button)(findViewById(R.id.connect));

        @Override
        protected void onPreExecute() {
            tw=(TextView)(findViewById(R.id.ip));
            inputip=tw.getText().toString();
            tw.setText("---.---.---.---");
            bt.setEnabled(false);
        }
        @Override
        protected void onPostExecute(Void reult) {
            setProgressBarIndeterminateVisibility(false);
            tw=(TextView)(findViewById(R.id.ip));
            tw.setText(GlobalValues.getInstance().nao.getStringIP());

            if(GlobalValues.getInstance().nao.getNaoOnLine()){
                bt.setText("����� ������");
                bt.setEnabled(false);

            }else
            {
                bt.setText("��������� �����");
                bt.setEnabled(true);
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub


            try{

                final WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                GlobalValues.getInstance().nao.tryConnection(inputip, wifiManager, wifiInfo);

            }catch (Exception e) {

                //	Toast.makeText(this,"Exception: " + e.getMessage(),Toast.LENGTH_LONG);
            }
            return null;
        }
        private String NAORequest(String str,String ip){
            try{

                HttpParams param = new BasicHttpParams();
                HttpConnectionParams.setSoTimeout(param, 150);
                HttpConnectionParams.setConnectionTimeout(param, 150);
                DefaultHttpClient client = new DefaultHttpClient(param);//("http://"+ ip +":9559/?eval="+str);
                HttpGet getRequest = new HttpGet("http://"+ ip +":9559/?eval="+str);

                HttpResponse response = client.execute(getRequest);

                HttpEntity entity = response.getEntity();
                if (entity != null){
                    InputStream inputstream = null;
                    try{
                        inputstream = entity.getContent();
                        StringBuffer data = new StringBuffer();
                        int c;
                        while ((c = inputstream.read()) != -1){
                            data.append((char) c);


                        }
                        return new String (data.toString());

                    }finally{
                        if(inputstream != null)
                            inputstream.close();
                        entity.consumeContent();
                    }
                }

            }catch (Exception e) {
                // TODO Auto-generated catch block
                //Toast.makeText(this,"Exception: " + e.getMessage(),Toast.LENGTH_LONG);
            }
            return "";

        }
    }
    private String NAORequest(String str,String ip){
        try{

            DefaultHttpClient client = new DefaultHttpClient();//("http://"+ ip +":9559/?eval="+str);
            HttpGet getRequest = new HttpGet("http://"+ ip +":9559/?eval="+str);
            HttpResponse response = client.execute(getRequest);
            HttpEntity entity = response.getEntity();
            if (entity != null){
                InputStream inputstream = null;
                try{
                    inputstream = entity.getContent();
                    StringBuffer data = new StringBuffer();
                    int c;
                    while ((c = inputstream.read()) != -1){
                        data.append((char) c);


                    }
                    return new String (data.toString());
                }finally{
                    if(inputstream != null)
                        inputstream.close();
                    entity.consumeContent();
                }
            }
        }catch (Exception e) {
            // TODO Auto-generated catch block
            //Toast.makeText(this,"Exception: " + e.getMessage(),Toast.LENGTH_LONG);
        }

        return "";

    }
    @Override
    public void onResume(){
        super.onResume();
        //loadData();
    }
}
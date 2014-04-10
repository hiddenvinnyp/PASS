package com.example.pass.callnao;

/**
 * Created by HiddenVinnyP on 09.04.2014.
 */

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.ComponentName;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Nao {
    private boolean naoOnline = false;
    private String IP="";
    private String[] joints= new String[]{"Head","LShoulderPitch","RShoulderPitch","LElbowRoll","RElbowRoll","LWristYaw","RWristYaw","LHipPitch","RHipPitch","LKneePitch","RKneePitch","LAnklePitch","RAnklePitch"};
    public Nao(){

    }
    public String getStringIP(){
        return this.IP;
    }
    public void setStringIP(String ip){
        this.IP = ip;
    }
    public void setNaoOnLine(boolean online){
        this.naoOnline = online;
    }
    public boolean getNaoOnLine(){
        return this.naoOnline;
    }
    public boolean isNaoOnline(){
        if(NAORequest("ALBonjour.ping()",this.IP).contains("true")){
            return true;
        }else
        {
            this.naoOnline=false;
            return false;
        }
    }
    private static String NAORequest(String str,String ip){
        try{

            HttpParams param = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(param, 200);
            HttpConnectionParams.setConnectionTimeout(param, 200);
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
    public static void tryConnection(String inputip,WifiManager wifiManager,WifiInfo wifiInfo){

        try{


            final int ipAddress = wifiInfo.getIpAddress();


            String ip ="";
            String d = NAORequest("ALBonjour.ping()",inputip);
            int i = 0;

            if(!d.contains("true"))
            {
                while(!d.contains("true") && i < 257 )
                {

                    ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), i++);
                    d = NAORequest("ALBonjour.ping()",ip);
                }
                if(d.contains("true")&&i < 257){
                    GlobalValues.getInstance().nao.setNaoOnLine(true);
                    GlobalValues.getInstance().nao.setStringIP(ip);
                }else
                {
                    GlobalValues.getInstance().nao.setNaoOnLine(false);
                    GlobalValues.getInstance().nao.setStringIP("0.0.0.0");
                    //Toast.makeText(MainActivity,"����� �� ������",Toast.LENGTH_LONG);
                }
            }else	{
                GlobalValues.getInstance().nao.setNaoOnLine(true);
                GlobalValues.getInstance().nao.setStringIP(inputip);
            }


        }catch (Exception e) {

            //	Toast.makeText(this,"Exception: " + e.getMessage(),Toast.LENGTH_LONG);
        }

    }
    public String[] getBehaviors(){
        if(this.IP != "")
        {
            String behavs=deleteHTMLTag(connectToRobot(this.IP,"ALBehaviorManager.getBehaviorNames()",300));
            behavs = behavs.replace("[", "");
            behavs = behavs.replace("\"", "");
            return behavs.split(", ");

        }else{

            return null;
        }
    }
    public void sendBehavior(String name){
        connectToRobot(this.IP, "ALBehaviorManager.runBehavior('" + name + "')",300);
    }
    public void setEnglish(){
        connectToRobot(this.IP,"ALTextToSpeech.setLanguage('English')",300);
    }
    public void setRussian(){
        connectToRobot(this.IP,"ALTextToSpeech.setLanguage('Russian')",300);
    }
    public void sendPhrase(String msg){
        try{

            connectToRobot(this.IP,"ALTextToSpeech.say('"+ URLEncoder.encode(msg,"UTF-8").replace("+", "%20") +"')",300);
        }catch (Exception e) {

        }
    }
    public void moveForward(){
        connectToRobot(this.IP, "ALMotion.setStiffnesses('Body',1.0f)",300);
        connectToRobot(this.IP, "ALMotion.moveInit()",300);
        connectToRobot(this.IP, "ALMotion.moveTo(0.2f,0.0f,0.0f)",300);
    }
    public void moveLeft(){
        connectToRobot(this.IP, "ALMotion.setStiffnesses('Body',1.0f)",300);
        connectToRobot(this.IP, "ALMotion.moveInit()",300);
        connectToRobot(this.IP, "ALMotion.moveTo(0.0f,0.2f,0.0f)",300);
    }
    public void moveRight(){
        connectToRobot(this.IP, "ALMotion.setStiffnesses('Body',1.0f)",300);
        connectToRobot(this.IP, "ALMotion.moveInit()",300);
        connectToRobot(this.IP, "ALMotion.moveTo(0.0f,-0.2f,0.0f)",300);
    }
    public void moveBack(){
        connectToRobot(this.IP, "ALMotion.setStiffnesses('Body',1.0f)",300);
        connectToRobot(this.IP, "ALMotion.moveInit()",300);
        connectToRobot(this.IP, "ALMotion.moveTo(-0.2f,0.0f,0.0f)",300);
    }
    public void turnLeft(){
        connectToRobot(this.IP, "ALMotion.setStiffnesses('Body',1.0f)",300);
        connectToRobot(this.IP, "ALMotion.moveInit()",300);
        connectToRobot(this.IP, "ALMotion.moveTo(0.0f,0.0f,0.5f)",300);
    }
    public void turnRight(){
        connectToRobot(this.IP, "ALMotion.setStiffnesses('Body',1.0f)",300);
        connectToRobot(this.IP, "ALMotion.moveInit()",300);
        connectToRobot(this.IP, "ALMotion.moveTo(0.0f,0.0f,-0.5f)",300);
    }
    public void stifnessesOn(){
        connectToRobot(this.IP, "ALMotion.setStiffnesses('Body',1.0f)",300);
    }
    public void stifnessesOff(){
        connectToRobot(this.IP, "ALMotion.setStiffnesses('Body',0.0f)",300);
    }
    public void stopAllBehavior(){
        connectToRobot(this.IP, "ALBehaviorManager.stopAllBehaviors()",300);
    }

    public void rest()
    {
        connectToRobot(this.IP, "ALRobotPosture.goToPosture('Crouch',1.0f)",300);//"ALBehaviorManager.runBehavior('User/sitdownKnee')",300);
    }

    public String[] getStat(){
        String[] values = new String[14];
        values[0] = deleteHTMLTag(getBattery());
        for(int i = 1; i < 14; i++)
        {

            values[i] = deleteHTMLTag(getJoints(joints[i-1]));
        }
        //joints = append(joints,);

        return values;
    }

    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }

    public String getJoints(String str)
    {
        return connectToRobot(this.IP, "ALMemory.getData('Device/SubDeviceList/" + str + "/Temperature/Sensor/Value')",200);
    }

    public String getBattery()
    {
        return connectToRobot(this.IP, "ALBattery.getBatteryCharge()",300);
    }

    public void wakeUp()
    {
        connectToRobot(this.IP, "ALMotion.setStiffnesses('Body',1.0f)",300);
        connectToRobot(this.IP, "ALMotion.moveInit()",300);
    }
    static String deleteHTMLTag(String str){
        str = str.replace("<HTML><BODY><PRE>", "");
        return str.replace("</PRE></BODY></HTML>", "");
    }
    static private String connectToRobot(String ip, String str,int timeout){
        StringBuffer data = new StringBuffer();
        try{

            HttpParams param = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(param, timeout);
            HttpConnectionParams.setConnectionTimeout(param, timeout);
            DefaultHttpClient client = new DefaultHttpClient(param);
            HttpGet getRequest = new HttpGet("http://"+ ip +":9559/?eval="+str);

            HttpResponse response = client.execute(getRequest);

            HttpEntity entity = response.getEntity();
            if (entity != null){
                InputStream inputstream = null;
                try{
                    inputstream = entity.getContent();

                    int c;
                    while ((c = inputstream.read()) != -1){
                        data.append((char) c);
                    }


                }finally{
                    if(inputstream != null)
                        inputstream.close();
                    entity.consumeContent();
                }
            }


        }catch (Exception e) {
            // TODO Auto-generated catch block

        }
        return new String (data.toString());
    }
}

//package com.redbear.chat;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import org.apache.http.NameValuePair;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.ArrayList;
//
//
//public class HighScore extends Activity {
//
//    TextView txt1, txt2, txt3;
//    ArrayAdapter<String> scoreAdapter =
//            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scores);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        ListView listView = (ListView) findViewById(R.id.main_listview);
//        listView.setAdapter(scoreAdapter);
//
//        new Task().execute();
//    }
//
//    class Task extends AsyncTask<String, String, String> {
//
//        private ProgressDialog progDial = new ProgressDialog(HighScore.this);
//        InputStream is = null;
//        String result = "";
//
//        protected void onPreExecute() {
//            progDial.setMessage("Fetching Data...");
//            progDial.show();
//            progDial.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    Task.this.cancel(true);
//                }
//            });
//        }
//
//        @Override
//        public String doInBackground(String... params) {
//
//            URL url_select = null;
//            URLConnection con = null;
//
//            try {
//
//                url_select = new URL("http://80.240.129.72/getScores.php");
//                con = url_select.openConnection();
//                is = new BufferedInputStream(con.getInputStream());
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                StringBuilder sb = new StringBuilder();
//                String line = "";
//
//                while ((line = br.readLine()) != null) {
//                    sb.append(line + "\n");
//                }
//
//                is.close();
//                result = sb.toString();
//
//                System.out.println(result);
//            } catch (MalformedURLException e) { e.printStackTrace();
//            } catch (IOException e) { e.printStackTrace(); }
//
//            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
//            return result;
//        }
//
//        protected void onPostExecute(String v) {
//
//            try {
//                JSONArray jArray = new JSONArray(v);
//
//                for (int i = 0; i < jArray.length(); i++) {
//                    JSONObject jObject = null;
//
//                    txt1 = (TextView) findViewById(R.id.id_square);
//                    txt2 = (TextView) findViewById(R.id.user_text);
//                    txt3 = (TextView) findViewById(R.id.highscore_text);
//
//                    jObject = jArray.getJSONObject(i);
//
//                    String id = jObject.getString("id");
//                    String name = jObject.getString("brugernavn");
//                    String highscore = jObject.getString("highscore");
//
//                    Log.d("BITBOX", jObject.toString());
//
//                    txt1.setText(id);
//                    txt2.setText(name);
//                    txt3.setText(highscore);
//                }
//                this.progDial.dismiss();
//            } catch (Exception e) {
//                Log.e("log_tag", "Error parsing data " + e.toString());
//            }
//        }
//    }
//}
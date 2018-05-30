package e.ujay.mobile_challenge_app;

import android.app.ProgressDialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
public class MainActivity extends AppCompatActivity {

    JSONArray jsonArray;
    ListView listView;
    ArrayList<JSONObject> listItems;
    public String url= "https://api.waveapps.com/businesses/89746d57-c25f-4cec-9c63-34d7780b044b/products/";
    String[] productName,productPrice;
    ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
    Locale locale = Locale.CANADA;
    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.list);
        ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.header, listView,false);
        // Add header view to the ListView
        listView.addHeaderView(headerView);
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Authorization", "Bearer 6W9hcvwRvyyZgPu9Odq7ko8DSY8Nfm")
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                JSONArray  myResponse;
                try{
                    jsonArray =  new JSONArray(response.body().string());
                    productName = new String[jsonArray.length()];
                    productPrice = new String[jsonArray.length()];
                    JSONObject jsonObject;
                    for(int i =0;i< jsonArray.length();i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        productName[i] = jsonObject.getString("name");
                        productPrice[i]= fmt.format(jsonObject.get("price"));
                        HashMap<String,String> hashMap=new HashMap<>();
                        hashMap.put("name",productName[i]);
                        hashMap.put("price",productPrice[i]);
                        arrayList.add(hashMap);

                    }

                }
                catch(JSONException e){
                    e.printStackTrace();
                }


                MainActivity.this.runOnUiThread(new Runnable()  {
                    @Override
                    public void run() {
                        String[] from={"name","price"};//string array
                        int[] to={R.id.txtname,R.id.txtprice};//int array of views id's
                        SimpleAdapter simpleAdapter=new SimpleAdapter(MainActivity.this,arrayList,R.layout.list_view_items,from,to);//Create object and set the parameters for simpleAdapter
                        listView.setAdapter(simpleAdapter);//sets the adapter for listView
                    }
                });

            }
        });
    }

}

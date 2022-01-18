package com.novartis.winnovators.agenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novariscyclemeeting2022.R;
import com.novartis.winnovators.utils.UserUtils;
import com.novartis.winnovators.webservice.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaActivity extends AppCompatActivity {
    TextView screenTitle;
    Spinner days_spinner;

    RecyclerView recyclerView;
    ProgressBar loading;

    ArrayList<Agenda_item> items_list;
    Agenda_adapter adapter;
    int selectedDay=1;
    boolean isFirstCall = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());

        days_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay = position+1;
                getData(selectedDay,isFirstCall);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getData(selectedDay,isFirstCall);
    }

    private void initFields() {
        screenTitle = findViewById(R.id.screen_title);
        loading = findViewById(R.id.loading);
        recyclerView = findViewById(R.id.recycler_view);
        days_spinner = findViewById(R.id.days_spinner);
        items_list = new ArrayList<>();
        initRecyclerView();
    }

    public void getData(int dayNo,boolean isFirstCall) {
        loading.setVisibility(View.VISIBLE);

        Webservice.getInstance().getApi().getAgenda(UserUtils.getAccessToken(getBaseContext()), dayNo).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONArray dataArray = responseObject.getJSONObject("day").getJSONArray("sessions");
                    int daysNo = responseObject.getInt("num_of_days");
                   if (isFirstCall){
                       setSalesSpinner(daysNo);
                       return;
                   }
                    setArray(dataArray);
                    loading.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d("Error Throw", t.toString());
                Log.d("commit Test Throw", t.toString());
                Log.d("Call", t.toString());
                Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
            }
        });
    }

    public void setArray(JSONArray list) {
        items_list.clear();
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final String title = currentObject.getString("title");
                final String from = currentObject.getString("time_from");
                final String to = currentObject.getString("time_to");
                items_list.add(new Agenda_item(id,title,from,to));
            }

            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setSalesSpinner(int daysNo) {
        isFirstCall = false;
        List<String> days = new ArrayList<>();
        for (int i =0;i<daysNo;i++){
            days.add("Day"+(i+1));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, days);
        days_spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Agenda_adapter(getBaseContext(), items_list);
        recyclerView.setAdapter(adapter);
    }

}
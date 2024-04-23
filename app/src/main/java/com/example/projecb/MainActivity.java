package com.example.projecb;

import android.graphics.Color;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    EditText editTextTask;
    Button buttonAdd;
    ListView listViewTasks;
    ArrayList<String> taskList;
    ArrayAdapter<String> adapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewTasks = findViewById(R.id.listViewTasks);

        sharedPreferences = getSharedPreferences("ToDoListPrefs", Context.MODE_PRIVATE);
        taskList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        listViewTasks.setAdapter(adapter);

        // Load saved tasks from SharedPreferences
        loadTasks();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        listViewTasks.setOnItemClickListener((parent, view, position, id) -> {
            removeTask(position);
        });
    }

    private void addTask() {
        String task = editTextTask.getText().toString().trim();
        if (!task.isEmpty()) {
            taskList.add(task);
            adapter.notifyDataSetChanged();
            saveTasks(); // Save tasks to SharedPreferences
            editTextTask.getText().clear();
        } else {
            Toast.makeText(MainActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeTask(int position) {
        taskList.remove(position);
        adapter.notifyDataSetChanged();
        saveTasks(); // Save tasks to SharedPreferences
    }

    private void saveTasks() {
        // Save taskList to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> taskSet = new HashSet<>(taskList);
        editor.putStringSet("tasks", taskSet);
        editor.apply();
    }

    private void loadTasks() {
        // Load saved tasks from SharedPreferences
        Set<String> taskSet = sharedPreferences.getStringSet("tasks", new HashSet<String>());
        taskList.addAll(taskSet);
        adapter.notifyDataSetChanged();
    }
}
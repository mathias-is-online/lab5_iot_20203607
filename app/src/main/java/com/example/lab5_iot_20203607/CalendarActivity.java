package com.example.lab5_iot_20203607;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab5_iot_20203607.entity.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Recibir la lista de tareas del intent
        taskList = (List<Task>) getIntent().getSerializableExtra("TASK_LIST");


        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                showTasksForDate(selectedDate);
            }
        });



    }


    private void showTasksForDate(Calendar selectedDate) {
        // Filtrar las tareas para la fecha seleccionada
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String selectedDateString = sdf.format(selectedDate.getTime());

        List<Task> tasksForSelectedDate = new ArrayList<>();
        for (Task task : taskList) {
            String taskDueDateString = sdf.format(task.getDueDate());
            if (taskDueDateString.equals(selectedDateString)) {
                tasksForSelectedDate.add(task);
            }
        }

        // Mostrar las tareas para la fecha seleccionada
        if (tasksForSelectedDate.isEmpty()) {
            Toast.makeText(this, "No hay tareas para esta fecha", Toast.LENGTH_SHORT).show();
        } else {
            StringBuilder message = new StringBuilder("Tareas para el ");
            message.append(selectedDateString).append(":\n");
            for (Task task : tasksForSelectedDate) {
                message.append("- ").append(task.getTitle()).append("\n");
            }
            Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show();
        }
    }
}

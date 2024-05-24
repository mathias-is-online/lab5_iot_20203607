package com.example.lab5_iot_20203607;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab5_iot_20203607.entity.Task;

import java.util.ArrayList;
import java.util.Calendar;





import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription;
    private Button buttonSetReminder, buttonSaveTask, buttonChooseDueDate;
    private Task task;
    private int reminderHour = -1;
    private int reminderMinute = -1;
    private Spinner spinnerImportance;
    private Date dueDate; // Atributo para la fecha de vencimiento
    private Calendar selectedDueDate; // Cambiar el tipo a Calendar



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSetReminder = findViewById(R.id.buttonSetReminder);
        buttonSaveTask = findViewById(R.id.buttonSaveTask);
        spinnerImportance = findViewById(R.id.spinnerImportance);
        buttonChooseDueDate = findViewById(R.id.buttonChooseDueDate);


        // Configurar el Spinner con las opciones de importancia
        List<String> importanceOptions = new ArrayList<>();
        importanceOptions.add("Alto");
        importanceOptions.add("Medio");
        importanceOptions.add("Bajo");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, importanceOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerImportance.setAdapter(adapter);





        buttonChooseDueDate.setOnClickListener(v -> {
            showDatePickerDialog();
        });


        task = new Task();

        buttonSetReminder.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);

            new TimePickerDialog(EditTaskActivity.this, (view, hourOfDay, minute) -> {
                reminderHour = hourOfDay;
                reminderMinute = minute;
            }, currentHour, currentMinute, true).show();
        });

        buttonSaveTask.setOnClickListener(v -> {
            task.setTitle(editTextTitle.getText().toString());
            task.setDescription(editTextDescription.getText().toString());
            if (reminderHour != -1 && reminderMinute != -1) {
                task.setReminderTime(reminderHour, reminderMinute);
            }

            Date selectedDate = selectedDueDate.getTime();

            task.setDueDate(selectedDate);

            String importance = spinnerImportance.getSelectedItem().toString();
            task.setImportance(importance);


            Intent resultIntent = new Intent();
            resultIntent.putExtra("TASK", task);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }



    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth1) -> {
                    // Actualizar la fecha seleccionada
                    selectedDueDate = Calendar.getInstance();
                    selectedDueDate.set(year1, monthOfYear, dayOfMonth1);
                    // Actualizar el texto del botón con la fecha seleccionada
                    String selectedDate = dayOfMonth1 + "/" + (monthOfYear + 1) + "/" + year1;
                    buttonChooseDueDate.setText(selectedDate);
                }, year, month, dayOfMonth);

        // Mostrar el diálogo emergente
        datePickerDialog.show();
    }
}



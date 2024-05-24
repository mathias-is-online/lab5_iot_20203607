package com.example.lab5_iot_20203607;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_iot_20203607.entity.Task;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TaskListActivity extends AppCompatActivity implements TaskAdapter.OnTaskCompleteListener{
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private Button buttonAddTask;
    private Button buttonCalendarView;

    private static final int REQUEST_CODE_EDIT_TASK = 1;
    private static final String TASKS_FILE_NAME = "tasks.dat";

    String canal1 = "importanteDefault";
    String canal2 = "importanteLow";
    String canal3 = "importanteHigh";

    private static final int NOTIFICATION_ID_ALTO = 1;
    private static final int NOTIFICATION_ID_MEDIO = 2;
    private static final int NOTIFICATION_ID_BAJO = 3;

    private static final int NOTIFICATION_ID_PUCP = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        buttonCalendarView = findViewById(R.id.buttonCalendarView);



        String pucpCode = getIntent().getStringExtra("PUCP_CODE");
        if (pucpCode != null) {
            // Aquí puedes utilizar el código PUCP como lo necesites
            Log.d("TaskListActivity", "Código PUCP: " + pucpCode);
        } else {
            Log.e("TaskListActivity", "El código PUCP no se recibió correctamente.");
        }

        crearCanalesNotificaciondefault();
        crearCanalesNotificacionlow();
        crearCanalesNotificacionhigh();


        recyclerViewTasks = findViewById(R.id.recyclerView);
        buttonAddTask = findViewById(R.id.buttonAddTask);

        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        taskList = loadTasksFromInternalStorage();



        actualizarImportanciaTareas();


        taskAdapter = new TaskAdapter(this, taskList, this);
        taskAdapter.setTaskList(taskList);
        taskAdapter.setContext(this);
        recyclerViewTasks.setAdapter(taskAdapter);


        buttonCalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la actividad de vista de calendario
                Intent intent = new Intent(TaskListActivity.this, CalendarActivity.class);
                intent.putExtra("TASK_LIST", (Serializable) taskList);
                startActivity(intent);
            }
        });




        buttonAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(TaskListActivity.this, EditTaskActivity.class);
            startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
        });


        notificarTareasImportantes(pucpCode);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_TASK && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("TASK")) {
                Task task = (Task) data.getSerializableExtra("TASK");
                taskList.add(task);
                taskAdapter.notifyDataSetChanged();
                saveTasksToInternalStorage();
            }
        }
    }

    // Método para actualizar la importancia de las tareas si el recordatorio es menor a 3 horas
    private void actualizarImportanciaTareas() {
        Calendar ahora = Calendar.getInstance();
        ahora.add(Calendar.HOUR, 3); // Sumar 3 horas a la hora actual

        for (Task tarea : taskList) {
            Calendar horaRecordatorio = Calendar.getInstance();
            horaRecordatorio.set(Calendar.HOUR_OF_DAY, tarea.getReminderHour());
            horaRecordatorio.set(Calendar.MINUTE, tarea.getReminderMinute());

            // Si la diferencia entre la hora actual y la hora del recordatorio es menor a 3 horas, actualizar la importancia
            if (horaRecordatorio.getTimeInMillis() - ahora.getTimeInMillis() < 0) {
                tarea.setImportance("Alto");
            }
        }
    }

    private List<Task> loadTasksFromInternalStorage() {
        try (FileInputStream fis = openFileInput(TASKS_FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<Task>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveTasksToInternalStorage() {
        try (FileOutputStream fos = openFileOutput(TASKS_FILE_NAME, MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(taskList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    public void crearCanalesNotificaciondefault() {

        NotificationChannel channel = new NotificationChannel(canal1,
                "Canal notificaciones default",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Canal para notificaciones con prioridad default");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        pedirPermisos();
    }
    public void crearCanalesNotificacionlow() {

        NotificationChannel channel = new NotificationChannel(canal2,
                "Canal notificaciones LOW",
                NotificationManager.IMPORTANCE_LOW);
        channel.setDescription("Canal para notificaciones con prioridad default");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        pedirPermisos();
    }
    public void crearCanalesNotificacionhigh() {

        NotificationChannel channel = new NotificationChannel(canal3,
                "Canal notificaciones HIGH",
                NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Canal para notificaciones con prioridad default");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        pedirPermisos();
    }


    public void pedirPermisos() {
        // TIRAMISU = 33
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(TaskListActivity.this, new String[]{POST_NOTIFICATIONS}, 101);
        }
    }




    // Método para notificar tareas importantes
    private void notificarTareasImportantes(String pucpCode) {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        for (Task task : taskList) {
            if (task.getImportance().equals("Alto")) {
                // Crear notificación con IMPORTANCE_HIGH
                Notification notification = crearNotificacion(task, canal3);

                // Mostrar notificación
                notificationManager.notify(NOTIFICATION_ID_ALTO, notification);
            } else if (task.getImportance().equals("Medio")) {
                // Crear notificación con IMPORTANCE_DEFAULT
                Notification notification = crearNotificacion(task, canal1);

                // Mostrar notificación
                notificationManager.notify(NOTIFICATION_ID_MEDIO, notification);
            } else if (task.getImportance().equals("Bajo")) {
                // Crear notificación con IMPORTANCE_LOW
                Notification notification = crearNotificacion(task, canal2);

                // Mostrar notificación
                notificationManager.notify(NOTIFICATION_ID_BAJO, notification);
            }
        }


        // Crear y mostrar notificación con el código PUCP en IMPORTANCE_DEFAULT
        Notification notification = crearNotificacionPUCP(pucpCode, canal1);
        notificationManager.notify(NOTIFICATION_ID_PUCP, notification);

    }



    private Notification crearNotificacionPUCP(String pucpCode, String channelId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("CODIGO PUCP:")
                .setContentText(pucpCode)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }
    // Método para crear una notificación
    private Notification crearNotificacion(Task task, String channelId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(task.getTitle())
                .setContentText(task.getDescription() )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }

    @Override
    public void onTaskComplete(int position) {
        taskList.remove(position);
        saveTasksToInternalStorage();
        taskAdapter.notifyDataSetChanged();
    }
}

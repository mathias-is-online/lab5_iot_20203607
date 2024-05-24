package com.example.lab5_iot_20203607;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_iot_20203607.entity.Task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private Context context;
    private OnTaskCompleteListener onTaskCompleteListener;

    public TaskAdapter(Context context, List<Task> taskList, OnTaskCompleteListener onTaskCompleteListener) {
        this.context = context;
        this.taskList = taskList;
        this.onTaskCompleteListener = onTaskCompleteListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.task = task;

        TextView textViewFirstName = holder.itemView.findViewById(R.id.textViewTaskTitle);
        textViewFirstName.setText(task.getTitle());

        TextView textViewTaskDescription = holder.itemView.findViewById(R.id.textViewTaskDescription);
        textViewTaskDescription.setText(task.getDescription());

        TextView textViewTaskDueDate = holder.itemView.findViewById(R.id.textViewTaskReminder);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dueDateStr = dateFormat.format(task.getDueDate());
        holder.textViewTaskDueDate.setText("Fecha de vencimiento: " + dueDateStr);
        holder.textViewTaskDueDate.setVisibility(View.VISIBLE);

        TextView textViewTaskReminder = holder.itemView.findViewById(R.id.textViewTaskReminder);
        textViewTaskReminder.setText(String.format("Recordatorio a las %02d:%02d", task.getReminderHour(), task.getReminderMinute()));


        holder.buttonDone.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmación")
                    .setMessage("¿Seguro que desea borrar este task?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        onTaskCompleteListener.onTaskComplete(position);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateTasks(List<Task> newTaskList) {
        taskList = newTaskList;
        notifyDataSetChanged();
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        Task task;
        TextView textViewTaskTitle, textViewTaskDescription, textViewTaskDueDate;
        Button buttonDone;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskTitle = itemView.findViewById(R.id.textViewTaskTitle);
            textViewTaskDescription = itemView.findViewById(R.id.textViewTaskDescription);
            textViewTaskDueDate = itemView.findViewById(R.id.textViewTaskDueDate);
            buttonDone = itemView.findViewById(R.id.buttonCompleteTask);
        }
    }

    public interface OnTaskCompleteListener {
        void onTaskComplete(int position);
    }
}



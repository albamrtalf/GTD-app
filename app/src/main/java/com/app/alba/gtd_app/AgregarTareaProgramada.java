package com.app.alba.gtd_app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Create by Alba
 */
public class AgregarTareaProgramada extends AppCompatActivity implements View.OnClickListener {

    Button Add, Canc;
    Switch Perd;
    EditText TITLE, DATE_I, TIME_I, DATE_F, TIME_F, CONTENT;
    Spinner PERIODICIDAD;
    String type, getTitle;

    private static final int SALIR = Menu.FIRST;
    private static int dia, mes, agno, hora, minutos;
    private final static String[] record = { "Cada día", "Cada 2 días", "Cada 5 días", "Cada 10 días", "Cada 15 días"};

    AdaptadorBD DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_t_programada);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, record);

        Add = (Button)findViewById(R.id.button_guardar);
        Canc = (Button)findViewById(R.id.button_cancelar);
        Perd = (Switch)findViewById(R.id.switch_periodica_tp);

        TITLE = (EditText)findViewById(R.id.editText_titulo);
        DATE_I = (EditText)findViewById(R.id.editText_inicio_fecha_tp);
        TIME_I = (EditText)findViewById(R.id.editText_hora_inicio_tp);
        DATE_F = (EditText)findViewById(R.id.editText_fehca_fin_tp);
        TIME_F = (EditText)findViewById(R.id.editText_hora_fin_tp);
        CONTENT = (EditText)findViewById(R.id.editText_nota);

        PERIODICIDAD = (Spinner)findViewById(R.id.spinner_tp);

        DATE_I.setInputType(InputType.TYPE_NULL);
        DATE_I.setOnClickListener(this);
        DATE_F.setInputType(InputType.TYPE_NULL);
        DATE_F.setOnClickListener(this);
        TIME_I.setInputType(InputType.TYPE_NULL);
        TIME_I.setOnClickListener(this);
        TIME_F.setInputType(InputType.TYPE_NULL);
        TIME_F.setOnClickListener(this);
       // Canc.setOnClickListener(this);

        PERIODICIDAD.setAdapter(adapter);


        Bundle bundle = this.getIntent().getExtras();

        String date_i, time_i, date_f, time_f, content;
        getTitle = bundle.getString("title");
        date_i = bundle.getString("date_i");
        time_i = bundle.getString("time_i");
        date_f = bundle.getString("date_f");
        time_f = bundle.getString("time_f");
        content = bundle.getString("content");
        type = bundle.getString("type");

        if (type.equals("add")) {
            Add.setText("Guardar");
        } else {
            if (type.equals("edit")) {
                TITLE.setText(getTitle);
                DATE_I.setText(date_i);
                TIME_I.setText(time_i);
                DATE_F.setText(date_f);
                TIME_F.setText(time_f);
                CONTENT.setText(content);
                Add.setText("Update task");
            }

        }
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUpdateTasks();
            }
        });
        Canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CookieSyncManager.createInstance(this);
                android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
                cookieManager.removeAllCookie();

                Intent intent = new Intent(AgregarTareaProgramada.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //Eliminar actividades anteriores | Crear nueva actividad
                startActivity(intent);
            }
        });
        Perd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PERIODICIDAD.setEnabled(true);
                } else {
                    PERIODICIDAD.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == DATE_I) {
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            agno = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    DATE_I.setText(dayOfMonth + "/" + month + "/" + year);
                }
            }, dia, mes, agno);
            datePickerDialog.show();
        }
        if (v == DATE_F) {
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            agno = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    DATE_F.setText(dayOfMonth + "/" + month + "/" + year);
                }
            }, dia, mes, agno);
            datePickerDialog.show();
        }
        if (v == TIME_I) {
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    TIME_I.setText(hourOfDay + ":" + minute);
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        }
        if (v == TIME_F) {
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    TIME_F.setText(hourOfDay + ":" + minute);
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        }
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);

        menu.add(1,SALIR,0,R.string.menu_salir);
//                .setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case SALIR:
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();

                Intent intent = new Intent(AgregarTareaProgramada.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //Eliminar actividades anteriores | Crear nueva actividad
                startActivity(intent);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    private void addUpdateTasks() {
        DB = new AdaptadorBD(this);
        String title, date_i, time_i, date_f, time_f, content, msj;
        title = TITLE.getText().toString();
        date_i = DATE_I.getText().toString();
        time_i = TIME_I.getText().toString();
        date_f = DATE_F.getText().toString();
        time_f = TIME_F.getText().toString();
        content = CONTENT.getText().toString();

        if(type.equals("add")) {
            if (title.equals("")){
                msj = "Ingrese un titulo";
                TITLE.requestFocus();
                Mensaje(msj);
            } else {
                Cursor c = DB.getTask(title);
                String gettitle = "";
                if (c.moveToFirst()) {
                    do {
                        gettitle = c.getString(1);
                    }while (c.moveToNext());
                }
                if (gettitle.equals(title)) {
                    TITLE.requestFocus();
                    msj = "La tarea ya existe";
                    Mensaje(msj);
                } else {
                    DB.addTask(title,date_i,time_i,date_f,time_f,content);
                    actividad(title,date_i,time_i,date_f,time_f,content);
                }
            }
        } else {
            if (type.equals("edit")) {
                Add.setText("Actualizar");
                if (type.equals("")) {
                    msj = "Ingrese un titulo";
                    TITLE.requestFocus();
                    Mensaje(msj);
                } else {
                    DB.updateTask(title,date_i,time_i,date_f,time_f,content,getTitle);
                    actividad(title,date_i,time_i,date_f,time_f,content);
                }
            }
        }
    }

    public void Mensaje (String msj) {
        Toast toast = Toast.makeText(this,msj,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

    public void actividad (String title, String date_i, String time_i, String date_f, String time_f, String content) {
        Intent intent = new Intent(AgregarTareaProgramada.this,VerTarea.class);
        intent.putExtra("title",title);
        intent.putExtra("date_i",date_i);
        intent.putExtra("time_i",time_i);
        intent.putExtra("date_f",date_f);
        intent.putExtra("time_f",time_f);
        intent.putExtra("content",content);
        startActivity(intent);
    }
}

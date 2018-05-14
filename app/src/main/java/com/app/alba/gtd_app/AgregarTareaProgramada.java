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
    EditText TITLE, CONTENT, HORA_IN, FECHA_IN, HORA_FIN, FECHA_FIN;
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

        Bundle bundle = this.getIntent().getExtras();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, record);

        Add = (Button)findViewById(R.id.button_guardar);
        Canc = (Button)findViewById(R.id.button_cancelar);
        Perd = (Switch)findViewById(R.id.switch_periodica_tp);

        TITLE = (EditText)findViewById(R.id.editText_titulo);
        CONTENT = (EditText)findViewById(R.id.editText_nota);
        FECHA_IN = (EditText)findViewById(R.id.editText_inicio_fecha_tp);
        FECHA_FIN = (EditText)findViewById(R.id.editText_fehca_fin_tp);
        HORA_IN = (EditText)findViewById(R.id.editText_hora_inicio_tp);
        HORA_FIN = (EditText)findViewById(R.id.editText_hora_fin_tp);

        PERIODICIDAD = (Spinner)findViewById(R.id.spinner_tp);

        FECHA_IN.setInputType(InputType.TYPE_NULL);
        FECHA_IN.setOnClickListener(this);
        FECHA_FIN.setInputType(InputType.TYPE_NULL);
        FECHA_FIN.setOnClickListener(this);
        HORA_IN.setInputType(InputType.TYPE_NULL);
        HORA_IN.setOnClickListener(this);
        HORA_FIN.setInputType(InputType.TYPE_NULL);
        HORA_FIN.setOnClickListener(this);
        Canc.setOnClickListener(this);

        PERIODICIDAD.setAdapter(adapter);

        String content;
        getTitle = bundle.getString("title");
        content = bundle.getString("content");
        type = bundle.getString("type");

        if (type.equals("add")) {
            Add.setText("Guardar");
        } else {
            if (type.equals("edit")) {
                TITLE.setText(getTitle);
                CONTENT.setText(content);
                Add.setText("Update tarea");
            }
        }
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUpdateTasks();
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
        if (v == FECHA_IN) {
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            agno = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    FECHA_IN.setText(dayOfMonth + "/" + month + "/" + year);
                }
            }, dia, mes, agno);
            datePickerDialog.show();
        }
        if (v == FECHA_FIN) {
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            agno = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    FECHA_FIN.setText(dayOfMonth + "/" + month + "/" + year);
                }
            }, dia, mes, agno);
            datePickerDialog.show();
        }
        if (v == HORA_IN) {
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    HORA_IN.setText(hourOfDay + ":" + minute);
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        }
        if (v == HORA_FIN) {
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    HORA_FIN.setText(hourOfDay + ":" + minute);
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        }
        if (v == Canc) {
            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();

            Intent intent = new Intent(AgregarTareaProgramada.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //Eliminar actividades anteriores | Crear nueva actividad
            startActivity(intent);
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
        String title, date_i, date_f, time_i, time_f, content, msj;
        title = TITLE.getText().toString();
        date_i = FECHA_IN.getText().toString();
        date_f = FECHA_IN.getText().toString();
        time_i = HORA_IN.getText().toString();
        time_f = HORA_FIN.getText().toString();
        content = CONTENT.getText().toString();

        if (type.equals("add")){
            if (title.equals("")){
                msj = "Ingrese un titulo";
                TITLE.requestFocus();
                Mensaje(msj);
            } else {
                if (content.equals("")){
                    msj = "Ingrese un mensaje";
                    CONTENT.requestFocus();
                    Mensaje(msj);
                } else {
                    Cursor c = DB.getTask(title);
                    String gettitle = "";
                    if (c.moveToFirst()) {
                        do {
                            gettitle = c.getString(1); //Id de la columna de Titulo
                        }while (c.moveToNext());
                    }
                    if (gettitle.equals(title)) {
                        TITLE.requestFocus();
                        msj = "El titulo de la tarea ya existe";
                        Mensaje(msj);
                    } else {
                        DB.addTask(title,date_i,time_i,date_f,time_f,content);
                        actividad(title,date_i,time_i,date_f,time_f,content);
                    }
                }
            }
        } else {
            if (type.equals("edit")) {
                Add.setText("Update tarea");
                if (title.equals("")) {
                    msj = "Ingrese un titulo";
                    TITLE.requestFocus();
                    Mensaje(msj);
                } else {
                    if (content.equals("")) {
                        msj = "Ingrese la tarea";
                        CONTENT.requestFocus();
                        Mensaje(msj);
                    } else {
                        DB.UpdateTask(title,date_i,time_i,date_f,time_f,content,getTitle);
                        actividad(title,date_i,time_i,date_f,time_f,content);
                    }
                }
            }
        }
    }

    public void Mensaje(String msj) {
        Toast toas = Toast.makeText(this, msj, Toast.LENGTH_SHORT);
        toas.setGravity(Gravity.CENTER_VERTICAL, 0,0);
        toas.show();
    }

    // Manda a la vista de la nota los datos
    public void actividad (String title, String date_i, String date_f, String time_i, String time_f, String content) {
        Intent intent = new Intent(AgregarTareaProgramada.this,VerTarea.class);
        intent.putExtra("title",title);
        intent.putExtra("date_i",date_i);
        intent.putExtra("date_f",date_f);
        intent.putExtra("time_i",time_i);
        intent.putExtra("time_f",time_f);
        intent.putExtra("content",content);
        startActivity(intent);
    }

}

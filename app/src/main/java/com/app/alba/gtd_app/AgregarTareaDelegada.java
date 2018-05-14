package com.app.alba.gtd_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Create by Alba
 */
public class AgregarTareaDelegada extends AppCompatActivity{

    Button Add;
    EditText TITLE, CONTENT, HORA_IN, FECHA_IN, HORA_FIN, FECHA_FIN;
    String type, getTitle;

    private static final int SALIR = Menu.FIRST;

    AdaptadorBD DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_t_programada);

        Add = (Button)findViewById(R.id.button_guardar);
        TITLE = (EditText)findViewById(R.id.editText_titulo);
        CONTENT = (EditText)findViewById(R.id.editText_nota);
        FECHA_IN = (EditText)findViewById(R.id.editText_inicio_fecha_tp);
        FECHA_FIN = (EditText)findViewById(R.id.editText_fehca_fin_tp);
        HORA_IN = (EditText)findViewById(R.id.editText_hora_inicio_tp);
        HORA_FIN = (EditText)findViewById(R.id.editText_hora_fin_tp);
        Bundle bundle = this.getIntent().getExtras();

        String content;
        getTitle = bundle.getString("title");
        content = bundle.getString("content");
        type = bundle.getString("type");

        if (type.equals("add")) {
            Add.setText("Add tarea");
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
    }

    @Override
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

                Intent intent = new Intent(AgregarTareaDelegada.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //Eliminar actividades anteriores | Crear nueva actividad
                startActivity(intent);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
                        actividad(title,content);
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
                        actividad(title,content);
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

    public void actividad (String title, String content) {
        Intent intent = new Intent(AgregarTareaDelegada.this,VerTarea.class);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        startActivity(intent);
    }
}

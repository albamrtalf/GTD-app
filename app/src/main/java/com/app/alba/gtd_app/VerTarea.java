package com.app.alba.gtd_app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;

public class VerTarea extends AppCompatActivity {

    private static final int EDITAR = Menu.FIRST; //Elemento que se ha seleccionado
    private static final int BORRAR = Menu.FIRST + 1;
    private static final int SALIR = Menu.FIRST + 2;

    String title, date_i, date_f, time_i, time_f, content;
    TextView TITLE, DATE_I, DATE_F, TIME_I, TIME_F, CONTENT;
    AdaptadorBD BD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vew_tarea);

        Bundle bundle = this.getIntent().getExtras();

        title = bundle.getString("title");
        date_i = bundle.getString("date_i");
        date_f = bundle.getString("date_f");
        time_i = bundle.getString("time_i");
        time_f = bundle.getString("time_f");
        content = bundle.getString("content");

        TITLE = (TextView)findViewById(R.id.textView_titulo);
        DATE_I = (TextView)findViewById(R.id.textView_fecha_inicio);
        DATE_F = (TextView)findViewById(R.id.textView_fecha_fin);
        TIME_I = (TextView)findViewById(R.id.textView_hora_inicio);
        TIME_F = (TextView)findViewById(R.id.textView_hora_fin2);
        CONTENT = (TextView)findViewById(R.id.textView_contenico);

        TITLE.setText(title);
        DATE_I.setText(date_i);
        DATE_F.setText(date_f);
        TIME_I.setText(time_i);
        TIME_F.setText(time_f);
        CONTENT.setText(content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);

        menu.add(1,EDITAR,0,R.string.menu_editar);
        menu.add(2,BORRAR,0,R.string.menu_eliminar);
        menu.add(3,SALIR,0,R.string.menu_salir);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case EDITAR:
                actividad("edit");
                return true;
            case BORRAR:
                alert();
                return true;
            case SALIR:
                actividad("delete");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actividad(String f) {
        if (f.equals("edit")) {
            String type = "edit";
            Intent intent = new Intent(VerTarea.this,AgregarTareaProgramada.class);
            intent.putExtra("type",type);
            intent.putExtra("title",title);
            intent.putExtra("date_i",date_i);
            intent.putExtra("date_f",date_f);
            intent.putExtra("time_i",time_i);
            intent.putExtra("time_f",time_f);
            intent.putExtra("content",content);
            startActivity(intent);
        } else {
            if (f.equals("delete")) {
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                Intent intent = new Intent(VerTarea.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //Eliminar actividades anteriores | Crear nueva actividad
                startActivity(intent);
            }
        }
    }

    private void alert() {
        AlertDialog alerta;
        alerta = new AlertDialog.Builder(this).create();
        alerta.setTitle("Mensaje de confirmacion");
        alerta.setMessage("¿Desea eliminar la tarea?");
        alerta.setButton(Dialog.BUTTON_POSITIVE, "Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete();
            }
        });
        alerta.setButton(Dialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alerta.show();
    }

    private void delete() {
        BD = new AdaptadorBD(this);
        BD.deleteTask(title);
        actividad("delete");
    }
}

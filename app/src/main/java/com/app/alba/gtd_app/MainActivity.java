package com.app.alba.gtd_app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int TPROGRAMADA = Menu.FIRST; //Elemento que se ha seleccionado
    private static final int TNOPROGRAMADA = Menu.FIRST + 1;
    private static final int TDELEGADA = Menu.FIRST + 2;
    private static final int TALGUNDIA = Menu.FIRST + 3;
    private static final int EXIST = Menu.FIRST + 4;

    ListView lista;
    TextView textLista;
    AdaptadorBD DB;
    List<String> item = null;
    String getTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textLista = (TextView) findViewById(R.id.textView_Lista);
        lista = (ListView) findViewById(R.id.ListView_ListaNotas);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getTitle = (String) lista.getItemAtPosition(position);
                alert("list");
            }
        });
        showTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(1,TPROGRAMADA,0,R.string.t_programada2);
        menu.add(2,TNOPROGRAMADA,0,R.string.t_no_programada);
        menu.add(3,TDELEGADA,0,R.string.t_delegada);
        menu.add(4,TALGUNDIA,0,R.string.t_algun_dia);
        menu.add(5,EXIST,0,R.string.menu_salir);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case TPROGRAMADA:
                actividad("addtp");
                return true;
            case TNOPROGRAMADA:
                actividad("addtnp");
                return true;
            case TDELEGADA:
                actividad("addtd");
                return true;
            case TALGUNDIA:
                //actividad("add");
                return true;
           /* case DELETE:
                alert("deletes");
                return true;*/
            case EXIST:
                finish(); //Cerrar app
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showTasks () {
        DB = new AdaptadorBD(this);
        Cursor c = DB.getTasks();
        item = new ArrayList<String>();
        String title = "";

        if (c.moveToFirst() == false) {

        } else {
            do {
                title = c.getString(1);
                item.add(title);
            } while (c.moveToNext());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, item);
        lista.setAdapter(adapter);
    }

    public String getTask() {
        String type = "", content = "";

        DB = new AdaptadorBD(this);
        Cursor c = DB.getTask(getTitle);

        if (c.moveToFirst()) {
            do {
                content = c.getString(2);
            }while (c.moveToNext());
        }

        return content;
    }

    public void actividad(String act){
        String type = "", content = "", date_i = "", time_i = "", date_f = "", time_f = "";

        if(act.equals("addtp")) {
            type="add";
            Intent intent = new Intent(MainActivity.this,AgregarTareaProgramada.class);
            intent.putExtra("type", type);
            startActivity(intent); //Lanzamos la actividad
        } else if(act.equals("addtnp")) {
           /* type = "add";
            Intent intent = new Intent(MainActivity.this, AgregarNoTareaProgramada.class);
            intent.putExtra("type", type);
            startActivity(intent); //Lanzamos la actividad*/
        } else if(act.equals("addtd")) {
            /*type = "add";
            Intent intent = new Intent(MainActivity.this, AgregarTareaDelegada.class);
            intent.putExtra("type", type);
            startActivity(intent); //Lanzamos la actividad*/
        } else if(act.equals("addtad")) {
            /*type = "add";
            Intent intent = new Intent(MainActivity.this, AgregarTareaProgramada.class);
            intent.putExtra("type", type);
            startActivity(intent); //Lanzamos la actividad*/
        } else {
            if (act.equals("edit")) {
                type = "edit";
                content = getTask();
                Intent intent = new Intent(MainActivity.this,AgregarTareaProgramada.class);
                intent.putExtra("title",getTitle);
                intent.putExtra("date_i",date_i);
                intent.putExtra("time_i",time_i);
                intent.putExtra("date_f",date_f);
                intent.putExtra("time_f",time_f);
                intent.putExtra("content",content);
                startActivity(intent);
            } else {
                if (act.equals("see")) {
                    content = getTask();
                    Intent intent = new Intent(MainActivity.this,VerTarea.class);
                    intent.putExtra("title",getTitle);
                    intent.putExtra("date_i",date_i);
                    intent.putExtra("time_i",time_i);
                    intent.putExtra("date_f",date_f);
                    intent.putExtra("time_f",time_f);
                    intent.putExtra("content",content);
                startActivity(intent);
                }
            }
        }
    }

    private void alert (String f) {
        AlertDialog alerta;
        alerta = new AlertDialog.Builder(this).create();

        if (f.equals("list")) {
            //alerta.setTitle("Titulo de la tarea: " + getTitle);
            alerta.setTitle(getTitle);
            alerta.setMessage("¿Que accion desea realizar?");
            alerta.setButton(Dialog.BUTTON_POSITIVE, "Ver", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actividad("see");
                }
            });
            alerta.setButton(Dialog.BUTTON_NEGATIVE, "Borrar", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delete("deletes");
                    Intent intent = getIntent();
                    startActivity(intent);
                }
            });
            alerta.setButton(Dialog.BUTTON_NEUTRAL, "Editar", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actividad("edit");
                }
            });
           // alerta.show();
        } else {
            if (f.equals("deletes")) {
                //alerta.setTitle("Confirmar message");
                alerta.setMessage("¿Desea borrar esta tarea?");
                alerta.setButton(Dialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alerta.setButton(Dialog.BUTTON_POSITIVE, "Borrar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete("deletes");
                        Intent intent = getIntent();
                        startActivity(intent);
                    }
                });
                alerta.setButton(Dialog.BUTTON_NEUTRAL, "Editar tarea", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actividad("edit");
                    }
                });
            }
        }
        alerta.show();
    }

    private void delete(String f) {
        DB = new AdaptadorBD(this);

        if (f.equals("delete")) {
            DB.deleteTask(getTitle);
        } else {
            if (f.equals("deletes")) {
                DB.deleteTasks();
            }
        }
    }
}

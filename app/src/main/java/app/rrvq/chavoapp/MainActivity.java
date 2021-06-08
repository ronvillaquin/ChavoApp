package app.rrvq.chavoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.HashMap;
import java.util.Map;


// las imagenes las pegamos en drawable
// los audios creamos un new directorio en res con el nombre de raw y pegamos audios
// cambiamos los colores en colors.xml
// cambiamos el icono de la app en la carpeta app new image asses
//*****************************************************************************************/
//creamos la conexion a sQLite para asi poder guardar los datos del juego que deseemos
//creaos en la carpeta java en la carpeta de los java que es com.example.chavoapp
//creamos un new aruchivo java class con el nombre AdminSQLiteOpenHelper
//*****************************************************************************************/

public class MainActivity extends AppCompatActivity {

    // agregamos los nombres o varibles para los componentes
     ImageView img_cambio_personaje;
     EditText caja_nombre;
     TextView mejores_puntos;

     LinearLayout linearBanner;
     Button btnDescargar;
     CountDownTimer countDownTimer;

   // int aux = 1; //para poner o quitar la pista en el menubar

    int num_aleatorio = (int) (Math.random() * 10); // para generar numeros aleatorios para el cambio de las imgview


     AdView mAdView;

     String locale="otro";


    //***************************METODO ON CREATE QUE INICIA CON EL ACTIVITY****************************************/
    /*****************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //*****************************casting para recibir lo que el usuario ingrese************************************/
        // hacemos el casting de las variables relacionadas con su id
        img_cambio_personaje = (ImageView)findViewById(R.id.img_cambio_personaje);
        caja_nombre = (EditText)findViewById(R.id.caja_nombre);
        mejores_puntos = (TextView)findViewById(R.id.mejores_puntos);
        linearBanner = findViewById(R.id.linearBanner);
        btnDescargar = findViewById(R.id.btnDescargar);
        //*****************************************************************************************/

        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        locale = tm.getNetworkCountryIso();



        //***********************************imagenes rotativas*********************************/
        // estructuras condicionales para mostrar la imagen al cerar y abrir el app
        int id;
        if (num_aleatorio == 0 || num_aleatorio == 10){
            // con este metodo obtenemos la ruta de la imagen que deseamos mostrar
            id = getResources().getIdentifier("cambio1", "drawable", getPackageName());
            img_cambio_personaje.setImageResource(id);
        }else
        if (num_aleatorio == 1 || num_aleatorio == 9){
            id = getResources().getIdentifier("cambio2", "drawable", getPackageName());
            img_cambio_personaje.setImageResource(id);
        }else
        if (num_aleatorio == 2 || num_aleatorio == 8){
            id = getResources().getIdentifier("cambio3", "drawable", getPackageName());
            img_cambio_personaje.setImageResource(id);
        }else
        if (num_aleatorio == 3 || num_aleatorio == 7){
            id = getResources().getIdentifier("cambio4", "drawable", getPackageName());
            img_cambio_personaje.setImageResource(id);
        }else
        if (num_aleatorio == 4 || num_aleatorio == 6){
            id = getResources().getIdentifier("cambio5", "drawable", getPackageName());
            img_cambio_personaje.setImageResource(id);
        }else
        if (num_aleatorio == 5){
            id = getResources().getIdentifier("cambio4", "drawable", getPackageName());
            img_cambio_personaje.setImageResource(id);
        }
        //*****************************************************************************************/

        //*******************************base de datos para score*******************************/
        //aqui llamamos a la base de datos colocand los mismo nombres que colocamos en su creacion
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        //para poder abrir la escritura en la base de datos
        SQLiteDatabase BD = admin.getWritableDatabase();
        //hacemos una consuta para el score Cursor
        Cursor consulta = BD.rawQuery(
                "select * from puntaje where score = (select max(score) from puntaje)", null);
        // una condicion para que sepa que hacer si ya consiguio un untaje o si aun esta vacia
        //le decimos que busque si consigue algo que realize la instruccion
        if (consulta.moveToFirst()){
            //aqui le decimos que recupere el nombre se coloca 0 porque nombre esta en la primera columna de la tabla
            // y empiezan desde 0
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            guardarDatos(locale);
            // ahora le decimos que tiene que poner los valores dentro del txtview
            mejores_puntos.setText(getResources().getString(R.string.record)+" " + temp_score + " -- " + temp_nombre);
            //cuando se deja de usar la base de datos se cierra
            BD.close();
        }else {  // en caso de que no consiga nada le decimos que cierre la conexion a la base de datos
            BD.close();
        }

        //*****************************************************************************************/

        //**********************************reproductor de musica**********************************/
        // agregamos la pista de fondo
        /*mp = MediaPlayer.create(this, R.raw.alphabet_song);
        mp.start();
        //looping para repetir la pista al terminar
        mp.setLooping(true);*/
        //*****************************************************************************************/

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


       /* mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3019606122027900/8827586637");
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); // anuncio de prueba
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });*/


        BannerMIO();
        btnDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.rrvq.aritmetica&hl=es_PE&gl=US");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });



    }

    public void BannerMIO(){

        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                linearBanner.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFinish() {

                linearBanner.setVisibility(View.GONE);

            }
        }.start();

    }

    public void guardarDatos(final String pais) {

        String url = getResources().getString(R.string.urlguardadatos);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("pais", pais);

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    /*public void showintersial(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }*/
    //*****************************************************************************************/




    //******************************BOTON COMENZAR***********************************/
    /*****************************************************************************************/
    // accion para asignar al onclick del boton comenzar
    public void btn_comenzar(View vista){
        String c_nombre = caja_nombre.getText().toString();

        // aqui comparamos y le decimos si nmbre no tiene nada dentro que ejecute
        if (!c_nombre.equals("")){

//            mostrar anuncio comleto para cambiar de activity  ********************************************
//            showintersial();


            //si consigue texto hayq eu detener el audio para cambiar de activity
            /*mp.stop();
            mp.release();*/
            // creamos otro activity para poder cambiar de activity en app new acti empy
            Intent intent = new Intent(this, Main2Activity_Nivel1.class);
            //debemos enviar el nombre del jugador al sigueinte activity
            intent.putExtra("jugador", c_nombre);  //key "jugador" es la llave para idetidicar a dond elo enviamos
            //ahora le decimos que corra la siguiente activyty
            startActivity(intent);
            //y terminamos el activyty que esta corriendo
            finish();
        }else {
            Toast.makeText(this, getResources().getString(R.string.ingresarNombre), Toast.LENGTH_LONG).show();

            //creamos la funsion para que abre el teclado si no ha introducido un nombre
            caja_nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(caja_nombre, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    //*****************************************************************************************/

    /*@Override
    public void onBackPressed() {
        finish();
        *//*Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        // con la de arriba se elimian todas todas menos la que se llamo
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);  // con esta elimina solo las q estan delante de la que se llamo
        startActivity(intent);*//*
    }*/

}

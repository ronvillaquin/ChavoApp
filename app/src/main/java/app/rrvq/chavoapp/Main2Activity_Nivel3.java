package app.rrvq.chavoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class Main2Activity_Nivel3 extends AppCompatActivity implements View.OnClickListener {

     TextView tv_jugador, tv_score;
     ImageView img_vidas, img_compa1, img_compa2, img_operacion;
     TextView caja_respuesta;
     MediaPlayer  mp_great, mp_bad;  //para reproducir musica de fondo en el activity

    int score;  // para ir almacenando el puntaje del jugador
    int numAleatorio1, numAleatorio2, resultado; // para las imagenes con numeros aleatorios
    int vidas = 3; // para las img de las vidas se nicializa en 3
    String nombre_jugador; // qui es donde recibiremos el nombre desde el activity de inicio
    String S_score; // la que contendra el escore para ser enviado al sigueinte activity
    String S_vidas; // las vidas que van quedando para pasarlas al siguiente activity

    //vector string o de cadena de caracteres para colocar los nombres de las imagenes para asi
    // trabajarlos mediantes numeros aleatorios las posiciones
    String numero[] = {"cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};

    AdView mAdView;

    Button[] castingNumeros;
    ImageButton castinIborrar;

    private InterstitialAd mInterstitialAd;
    Boolean Online;


    //***************************METODO ON CREATE QUE INICIA CON EL ACTIVITY****************************************/
    //*****************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2__nivel3);

        //********************************icono en action bar*************************************/
        //ahora colocaremos el icono en el action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        //*****************************************************************************************/
        //**********************************reproductor de musica**********************************/
        // agregamos la pista de fondo
        /*mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        //looping para repetir la pista al terminar
        mp.setLooping(true);*/

        //audio para respuesta correcta
        mp_great = MediaPlayer.create(this, R.raw.wonderful);
        //audio para respuesta incorrecta
        mp_bad = MediaPlayer.create(this, R.raw.bad);
        //*****************************************************************************************/

        Toast.makeText(this, getResources().getString(R.string.nivel3), Toast.LENGTH_LONG).show();

        tv_jugador = findViewById(R.id.tv_jugador);
        tv_score = findViewById(R.id.tv_score);
        img_vidas = findViewById(R.id.img_vidas);
        img_compa1 = findViewById(R.id.img_compa1);
        img_compa2 = findViewById(R.id.img_comp2);
        img_operacion = findViewById(R.id.img_operacion);
        caja_respuesta = findViewById(R.id.caja_respuesta);
        castinIborrar = findViewById(R.id.ib_borrar);

        castingNumeros = new Button[]{
                findViewById(R.id.btn_uno), findViewById(R.id.btn_dos),
                findViewById(R.id.btn_tres), findViewById(R.id.btn_cuatro),
                findViewById(R.id.btn_cinco), findViewById(R.id.btn_seis),
                findViewById(R.id.btn_siete), findViewById(R.id.btn_ocho),
                findViewById(R.id.btn_nueve), findViewById(R.id.btn_cero)};

        for (int i = 0; i < 10; i++) {
            castingNumeros[i].setOnClickListener(this);
        }

        castinIborrar.setOnClickListener(this);

        //***********************Recepcion de variable de nombre***********************************/
        //otenemos la variable con el nombre que viene desde el primer activity
        //llamamos la variable para recibir y alogar el key que queremos recibir
        nombre_jugador = getIntent().getStringExtra("jugador");
        //ahora le indicamos donde lo pondremos o mostraremos
        tv_jugador.setText(nombre_jugador);

        S_score = getIntent().getStringExtra("score");
        score = Integer.parseInt(S_score);
        tv_score.setText(score+"");

        S_vidas = getIntent().getStringExtra("vidas");
        vidas = Integer.parseInt(S_vidas);
        switch (vidas){
            case 3:
                img_vidas.setImageResource(R.drawable.tresvidas);
                break;
            case 2:
                img_vidas.setImageResource(R.drawable.dosvidas);
                break;
            case 1:
                img_vidas.setImageResource(R.drawable.unavida);
                break;
        }
        //*****************************************************************************************/

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interticial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //***********************llamamos al metodo de numeros aleatorios***********************************/
        numAleatorio();
        //*****************************************************************************************/

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }
    /*****************************************************************************************/

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public void numAleatorio(){
        if (score <= 29){
            //creamos dos numeros aleatorios
            numAleatorio1 = (int) (Math.random() * 10);
            numAleatorio2 = (int) (Math.random() * 10);
            //sumamos los numeros aleatorios
            resultado = numAleatorio1 - numAleatorio2;

            if (resultado >= 0) {
                //recorremos el arreglo para mostrar las img de numeros
                for (int i = 0; i < numero.length; i++) {
                    // aqui le desimos que se pare en la posicion del arreglo y me muestre ese nombre de la carpeta drawable
                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());
                    if (numAleatorio1 == i) {
                        img_compa1.setImageResource(id);
                    }
                    if (numAleatorio2 == i) {
                        img_compa2.setImageResource(id);
                    }
                }
            }else {
                // recursividad para que repita el metodo
                numAleatorio();
            }

        }else{

            Online = isOnline(getApplicationContext());

            if (Online){


                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            // pasamos al sigueinte activity o nivel si se cumple el core
                            Intent intent = new Intent(getApplicationContext(), Main2Activity_Nivel4.class);
                            //convertimos a string las variables de entero que deseamos enviar como score y vidas
                            S_score = String.valueOf(score);
                            S_vidas = String.valueOf(vidas);
                            // enviamos los datos que deseamos nombre score y vidas
                            intent.putExtra("jugador", nombre_jugador);
                            intent.putExtra("score", S_score); //no el int sore porque el int score es solo almacenar temporal
                            intent.putExtra("vidas", S_vidas);
                            //iniciamos el siguiente activity
                            startActivity(intent);
                            //detenemos el activity que estamos
                            finish();
                            // detenemos los audios
            /*mp.stop();
            mp.release();*/
                        }

                    });

                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    Intent intent = new Intent(getApplicationContext(), Main2Activity_Nivel4.class);
                    //convertimos a string las variables de entero que deseamos enviar como score y vidas
                    S_score = String.valueOf(score);
                    S_vidas = String.valueOf(vidas);
                    // enviamos los datos que deseamos nombre score y vidas
                    intent.putExtra("jugador", nombre_jugador);
                    intent.putExtra("score", S_score); //no el int sore porque el int score es solo almacenar temporal
                    intent.putExtra("vidas", S_vidas);
                    //iniciamos el siguiente activity
                    startActivity(intent);
                    //detenemos el activity que estamos
                    finish();
                }
            }else {
                // pasamos al sigueinte activity o nivel si se cumple el core
                Intent intent = new Intent(this, Main2Activity_Nivel4.class);
                //convertimos a string las variables de entero que deseamos enviar como score y vidas
                S_score = String.valueOf(score);
                S_vidas = String.valueOf(vidas);
                // enviamos los datos que deseamos nombre score y vidas
                intent.putExtra("jugador", nombre_jugador);
                intent.putExtra("score", S_score); //no el int sore porque el int score es solo almacenar temporal
                intent.putExtra("vidas", S_vidas);
                //iniciamos el siguiente activity
                startActivity(intent);
                //detenemos el activity que estamos
                finish();
                // detenemos los audios
            /*mp.stop();
            mp.release();*/
            }
        }
    }

    //*****************************************************************************************/
    /*************************BOTON COMPROBAR*********************************************/
    public void btn_Comprobar(View vista){
        //recibimos lo que el usuario introdujo en respuesta
        String c_respuesta = caja_respuesta.getText().toString();

        //con equals le preguntamos si la variable trar caracteres
        if (!c_respuesta.equals("")){
            int c_respuesta_int = Integer.parseInt(c_respuesta);
            //comparamos con la variable resultado que sumanos de los aleatorios en el metdo numAleatorios
            if (c_respuesta_int == resultado){
                mp_great.start();
                score++;
                //me saltaba error para poder solucionarlo tuve que concatenarle ""
                tv_score.setText(score+"");
                caja_respuesta.setText("");
                baseDeDatos();
            }else {
                mp_bad.start();
                vidas--;
                baseDeDatos();

                switch (vidas){
                    case 3:
                        img_vidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this, getResources().getString(R.string.queda2), Toast.LENGTH_SHORT).show();
                        img_vidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this, getResources().getString(R.string.queda1), Toast.LENGTH_SHORT).show();
                        img_vidas.setImageResource(R.drawable.unavida);
                        break;
                    case 0:


                        Toast.makeText(this, getResources().getString(R.string.queda0), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        /*mp.stop();
                        mp.release();*/
                        break;
                }
                caja_respuesta.setText("");
            }
            //tiene que cambiar las imagenes de la suma independiente mente si respondio bn o mal
            //por eso lalmaamos al metodo numero aleatorio
            numAleatorio();
        }else {
            Toast.makeText(this, getResources().getString(R.string.escriberespuesta), Toast.LENGTH_SHORT).show();
        }
    }
    //*****************************************************************************************/

    //*****************************************************************************************/
    /*************************BOTON ATRAS*********************************************/
    public void baseDeDatos(){
        //para que guarde el score en caso de que cierre el activity o se temrien las vidas
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        //consultamos si existen rgistros para si es mayor al score que tiene lo modifique o si no tiene que lo guarde
        // hacemos una consulta anidada
        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null);
        // ahora comparamos con el puntaje que lleva hasta el momento en caso de que se acaben las vidas o cierre el progrmaa
        //la clase movetofistr nos permite saber si hubo una respuesta de la base de datos osea i encontro resultados
        if (consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            //convertimos a entero el string score
            int mejorScore = Integer.parseInt(temp_score);

            //esta condicion compara que el score del jugador actualsea mayor a cualquier score registrado
            if (score > mejorScore){
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", nombre_jugador);
                modificacion.put("score", score);
                BD.update("puntaje", modificacion, "score="+ mejorScore, null);
            }
            BD.close();
        }else {
            ContentValues insertar = new ContentValues();
            insertar.put("nombre", nombre_jugador);
            insertar.put("score", score);
            BD.insert("puntaje", null, insertar);
            BD.close();
        }
    }
    /*****************************************************************************************/

    public void mostrarNPrecionado(String numero){

        String etNumero = caja_respuesta.getText().toString();

        etNumero = etNumero+ numero.charAt(0);

        caja_respuesta.setText(etNumero);

    }

    @Override
    public void onClick(View v) {



        //Calculadora con iconos seleccionables
        switch (v.getId()) {
            case R.id.ib_borrar:

                String borrarNumero = caja_respuesta.getText().toString();
                String muestra = "";

                if (borrarNumero.equals("")){
                    caja_respuesta.setText("");
                }else {

                    for (int i=0; i<borrarNumero.length()-1; i++){

                        muestra = muestra + borrarNumero.charAt(i);

                    }

                    caja_respuesta.setText(muestra);
                }



                break;
            case R.id.btn_cero:
                mostrarNPrecionado("0");
                break;
            case R.id.btn_uno:
                mostrarNPrecionado("1");
                break;
            case R.id.btn_dos:
                mostrarNPrecionado("2");
                break;
            case R.id.btn_tres:
                mostrarNPrecionado("3");
                break;
            case R.id.btn_cuatro:
                mostrarNPrecionado("4");
                break;
            case R.id.btn_cinco:
                mostrarNPrecionado("5");
                break;
            case R.id.btn_seis:
                mostrarNPrecionado("6");
                break;
            case R.id.btn_siete:
                mostrarNPrecionado("7");
                break;
            case R.id.btn_ocho:
                mostrarNPrecionado("8");
                break;
            case R.id.btn_nueve:
                mostrarNPrecionado("9");
                break;
        }

    }

    //*****************************************************************************************/
    /*************************BOTON ATRAS*********************************************/
    // para evitar que usen el boton de regresar de android paraq ue no reguresa los activitys
    //la palabra override es para sobre escribir metodos que ya estan establecidos
    @Override
    public void onBackPressed() {
        Online = isOnline(getApplicationContext());

        if (Online){
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getResources().getString(R.string.interticial));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());


            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        // Load the next interstitial.
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        // con la de arriba se elimian todas todas menos la que se llamo
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);  // con esta elimina solo las q estan delante de la que se llamo
                        startActivity(intent);
                    }

                });

            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                // con la de arriba se elimian todas todas menos la que se llamo
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);  // con esta elimina solo las q estan delante de la que se llamo
                startActivity(intent);
            }
        }else {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            // con la de arriba se elimian todas todas menos la que se llamo
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);  // con esta elimina solo las q estan delante de la que se llamo
            startActivity(intent);
        }
    }
}

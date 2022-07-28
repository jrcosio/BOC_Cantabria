package com.jrblanco.boccantabria.view

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jrblanco.boccantabria.R
import com.jrblanco.boccantabria.databinding.ActivityMainBinding
import com.jrblanco.boccantabria.model.ItemBoc
import com.jrblanco.boccantabria.model.UrlBoc
import com.jrblanco.boccantabria.model.XmlPullParserHandler
import java.lang.reflect.Type
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), ListadoBocFragment.CallbackFavorito {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toogle:ActionBarDrawerToggle //Para el Drawer Navigation view
    private var seccionBOC = 0      //Para saber que sección se tiene que visualizar
    private var rssBoc = ArrayList<List<ItemBoc>>()   //Lista en la que se guarda todas las secciones del BOC

    private var listaFavoritos = ArrayList<ItemBoc>() //Lista de Favoritos

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        //Cargamos del sharedpref como esta configurado el tema oscuro
        temaOscuro()

        //Cargamos los Favoritos de disco
        this.listaFavoritos = leerFavoritos()

        var recarga:Boolean = true //Boleana utilizada como check para mostrar la animación de carga

        if (cambiadoTema()) { //En caso que se venga de cambiar el tema oscuro/claro anula obtener datos
            recarga = false
        } else {
            Thread.sleep(1000)
        }

        //Nada más iniciar la APP se descargan los datos del BOC
        obtenerDatosBoc(animacion = recarga, mostrarRecyclerView = recarga)

        //--- Inicia el activity
        setTheme(R.style.Theme_BOCCantabria)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //--- Titulo en el ActionBar
        supportActionBar?.title = " BOC Cantabria"
        //--- Icono en el ActionBar
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_escudo_cantabria)
        //--- Oculta el fondo del navigationView
        binding.bottomNavigationView.background = null

        //--- Menú drawer navigation view
        toogle = ActionBarDrawerToggle(this, binding.drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //--- Eventos Listener de las diferentes opciones ---
        //---------------------------------------------------
        //--- Método del listener del las opciones del ButtomNavigatorView
        menuButtomNavigatorView()

        //--- Listener del botón flotante
        binding.botonDeRecarga.setOnClickListener {
            obtenerDatosBoc(animacion = true)
        }

        //--- Método listener del Drawer Nav View
        binding.navView.setNavigationItemSelectedListener {
            controlOpcionesNavigatorDrawer(it)
            binding.drawerLayout.closeDrawers()
            true
        }

    }

    /**
     * Infla el menú de Opciones (Botón de Configurar)
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opt,menu)
        return true
    }

    /**
     * Método para cambiar el fragment
     *
     * @param fragment El Fragment (clase Fragment) que queremos usar
     */
    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView,fragment)
            .commit()
    }

    /**
     * Método para recoger los eventos de los dos menús del ActionBar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)) {
            return true
        }

        when (item.itemId) {
            R.id.config -> {
                cargarFragment(ConfigFragment())
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Método para las opciones del ButtomNavigationView -> (Vamos el menú de abajo)
     */
    private fun menuButtomNavigatorView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.principal -> {
                    if (!rssBoc.isNullOrEmpty()) {
                        cargarFragment(ListadoBocFragment(rssBoc[seccionBOC]))
                    } else {
                        Toast.makeText(this,R.string.no_datos,Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                R.id.favoritos -> {
                    if (!listaFavoritos.isNullOrEmpty()) {
                        cargarFragment(ListadoBocFragment(listaFavoritos))
                    } else {
                        Toast.makeText(this,R.string.no_datos_fav,Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    /**
     * Método para controlar las opciones del Navigation Drawer
     */
    private fun controlOpcionesNavigatorDrawer(item: MenuItem){
        when (item.itemId) {
            R.id.menu_disposicionesgral ->          {seccionBOC=0}
            R.id.menu_autoridades_nombramientos ->  {seccionBOC=1}
            R.id.menu_autoridades_cursos ->         {seccionBOC=2}
            R.id.menu_autoridades_otros ->          {seccionBOC=3}
            R.id.menu_contratacion ->               {seccionBOC=4}
            R.id.menu_economia_presupuestos ->      {seccionBOC=5}
            R.id.menu_economia_fiscal ->            {seccionBOC=6}
            R.id.menu_economia_seguridadsocial ->   {seccionBOC=7}
            R.id.menu_economia_otros ->             {seccionBOC=8}
            R.id.menu_expropiacion ->               {seccionBOC=9}
            R.id.menu_subvenciones ->               {seccionBOC=10}
            R.id.menu_otros_urbanismo ->            {seccionBOC=11}
            R.id.menu_otros_medioambiente ->        {seccionBOC=12}
            R.id.menu_otros_convenios ->            {seccionBOC=13}
            R.id.menu_otros_particulares ->         {seccionBOC=14}
            R.id.menu_otros_varios ->               {seccionBOC=15}
            R.id.menu_proce_subastas ->             {seccionBOC=16}
            R.id.menu_proce_otros ->                {seccionBOC=17}
            R.id.menu_elecciones ->                 {seccionBOC=18}
        }

        if (!rssBoc.isNullOrEmpty()) {
            cargarFragment(ListadoBocFragment(rssBoc[seccionBOC]))
        } else {
            Toast.makeText(this,R.string.no_datos,Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Método con el que obtenemos los datos del RSS del BOC de Cantabria
     * @param animacion Si es true durante la carga muestra una animación
     * @param mostrarRecyclerView Si es true despues de descargar los datos xml muestra el RecyclerView
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerDatosBoc(animacion:Boolean = false,mostrarRecyclerView:Boolean = true){

        //Comprueba si tiene internet el dispositivo y si no lo hay muestra mensaje y no obtiene datos
        if (!tengoInternet(this)) {
            val dialogBuilder = AlertDialog.Builder(this)
                .setTitle("Error de Conexión a Internet")
                .setMessage("En este momento es dispositivo no tiene conexión a INTERNET")
                .setCancelable(false)
                .setPositiveButton("Aceptar", DialogInterface.OnClickListener{
                    dialog, id -> dialog.cancel()
                })
                .create()
                .show()
            return
        }

        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        builder.setView(inflater.inflate(R.layout.layout_cargando,null))
        builder.setCancelable(false)

        val dialog = builder.create()

        //Si se quiere animación la muestra
        if (animacion) {
            dialog.show()
        }

        //Crea un hilo para la descarga de los datos
        thread {
            for (url in UrlBoc.listadoRSS) {

                val parser1 = XmlPullParserHandler(listaFavoritos)

                //Descarga de la web del boc.cantabria.es el RSS
                val response = URL(url.url).readText()
                //Convierte el fichero de texto XML a Lista de Objetos
                val tempListBoc = parser1.parse(response.byteInputStream())
                //Guarda la sección obtenida en el arraylist principal
                rssBoc.add(tempListBoc)
            }

            if (mostrarRecyclerView) {
                if (!rssBoc.isNullOrEmpty()) {
                    cargarFragment(ListadoBocFragment(rssBoc[seccionBOC]))
                } else {
                    Toast.makeText(this,R.string.no_datos,Toast.LENGTH_SHORT).show()
                }
            }

            runOnUiThread {
                if (animacion) dialog.dismiss()  //Destruye el dialogo de carga
            }
        }
    }

    /**
     * Método que establece como esta configurado el modo oscuro
     */
    private fun temaOscuro() {
        //Define la variable pref
        val pref: SharedPreferences? = this?.getSharedPreferences("com.jrblanco.temaoscuro",0)
        //Lee la varianle
        val activado = pref?.getBoolean("valor",false)

        if (activado!!) {
            AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.MODE_NIGHT_YES))
        } else {
            AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.MODE_NIGHT_NO))
        }
    }

    /**
     * Método que son las sharedpreferens se si vengo de un cambio de tema
     */
    private fun cambiadoTema():Boolean {
        val pref: SharedPreferences? = this?.getSharedPreferences("com.jrblanco.temaoscuro",0)
        //Lee la varianle
        val cambio = pref?.getBoolean("cambiadotema",false)

        pref?.edit()?.putBoolean("cambiadotema",false)?.apply()

        return cambio!!
    }

    /**
     * Método de la interface de ListadoBocFragment con el cual trae hasta el activity
     * el ItemBoc que se ha dicho que es favorito
     */
    override fun onFavorito(i: ItemBoc) {
        if (!this.listaFavoritos.contains(i)) {
            this.listaFavoritos.add(i)
        } else {
           this.listaFavoritos.remove(i)
        }
        guardarFavoritos(this.listaFavoritos)
    }

    /**
     * Método que lee los favoritos guardados en el dispositivo
     */
   private fun leerFavoritos(): ArrayList<ItemBoc> {
        var lista = ArrayList<ItemBoc>()

        val pref: SharedPreferences? = this.getSharedPreferences("com.jrblanco.listaFavoritos",0)
        val gson = Gson()
        val json: String? = pref?.getString("LISTAFAVORITOS",null)
        if (!json.isNullOrEmpty()) {
            val type: Type = object : TypeToken<ArrayList<ItemBoc>>() {}.type
            lista = gson.fromJson(json,type)
        }
        return lista

    }

    /**
     * Método que guarda la lista de Favoritos en el dispositivo
     */
    private fun guardarFavoritos(list: List<ItemBoc>){
        val pref: SharedPreferences? = this.getSharedPreferences("com.jrblanco.listaFavoritos",0)
        val gson = Gson()
        val json:String = gson.toJson(list)

        pref?.edit()?.putString("LISTAFAVORITOS",json)?.apply()
    }

    /**
     * Método onRestart
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRestart() {
        super.onRestart()
        obtenerDatosBoc(true,true)
    }

    /**
     * Metodo que verifica si el dispositivo tiene internet
     */
    private fun tengoInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}
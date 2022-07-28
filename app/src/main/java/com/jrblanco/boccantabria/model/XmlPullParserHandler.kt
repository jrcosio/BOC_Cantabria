package com.jrblanco.boccantabria.model

import android.os.Build
import androidx.annotation.RequiresApi
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.time.LocalDate

class XmlPullParserHandler(val listaFavoritos:ArrayList<ItemBoc>) {

    private var listaRSS = ArrayList<ItemBoc>()

    private lateinit var item_rss:ItemBoc

    private lateinit var text: String

    @RequiresApi(Build.VERSION_CODES.O)
    fun parse(inputStream: InputStream): List<ItemBoc> {
        try {
            var check = false

            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagname = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> if (tagname.equals("item", ignoreCase = true)) {
                        // inicializa el objeto item
                        item_rss = ItemBoc()
                        check = true
                    }

                    XmlPullParser.TEXT -> text = parser.text

                    XmlPullParser.END_TAG -> if (tagname.equals("item", ignoreCase = true)) {

                        listaFavoritos.forEach {
                            if (it.id.equals(item_rss.id)) {
                                item_rss.favorito = true
                            }
                        }

                        // Añade al finalizar el item a la lista el objeto
                        item_rss?.let { listaRSS.add(it) }
                        check = false
                    } else if (tagname.equals("title", ignoreCase = true) && check) {
                        val result = text.split(":")

                        if (result[0].equals("null")) {
                            item_rss!!.organismo = "MANCOMUNIDAD, CONCEJU U OTRO"
                        }else {
                            item_rss!!.organismo = result[0]    //Del title, extrae la primera parte que que ess el organismo
                        }
                        item_rss!!.descripcion = result[1].substring(1) // La segunda parte es la descipción deñ BOC

                    } else if (tagname.equals("link", ignoreCase = true) && check) {
                        item_rss!!.enlace = text.toString() //Link del BOC

                        //Para extraer el numero de ID del Link
                        item_rss!!.id = text?.substring(text?.indexOf("=")?.plus(1)!!)

                    } else if (tagname.equals("pubDate", ignoreCase = true) && check) {

                        item_rss!!.fecha = text?.toString()
                    }
                }
                eventType = parser.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        }
       // listaRSS.add(ItemBoc("","","",null,"",false))
        return listaRSS
    }
}
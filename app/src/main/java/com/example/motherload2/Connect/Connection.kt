package com.example.motherload2.Connect


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.motherload2.App
import com.example.motherload2.Character.Character
import com.example.motherload2.Character.Item
import com.example.motherload2.Character.Marchant
import com.example.motherload2.Character.Offers
import com.example.motherload2.Character.Upgrade
import com.example.motherload2.Character.Voisins
import com.example.motherload2.View.GameActivity
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.net.URLEncoder
import java.security.MessageDigest
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class Connection private constructor() {
    private val TAG = "Connection"
    private val BASE_URL = " https://test.vautard.fr/creuse_srv/"
    private lateinit var session : String
    private lateinit var signature : String
    internal var connected : Boolean = false
    private val _offers = MutableLiveData<List<Offers>>()
    val offers: LiveData<List<Offers>> get() = _offers
    private val _item = MutableLiveData<List<Item>>()
    val item: LiveData<List<Item>> get() = _item

    private val upgrades = ArrayList<Upgrade>()
    private val _upgrad = MutableLiveData<List<Upgrade>>()
    val upgrad: LiveData<List<Upgrade>> get() = _upgrad


    companion object {
        @Volatile
        private var INSTANCE: Connection? = null

        fun getInstance(): Connection {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Connection().also { INSTANCE = it }
            }
        }
    }

    fun ConnectWeb(Login: String, Password: String) {
    /*
        fonction utiliser pour se connecter au serveur en donnant en paramètre l'identifiant et le mot de passe
    */
        val encodepass = hash(Password)
        val encodedLog = URLEncoder.encode(Login, "UTF-8")

        val url = BASE_URL + "/connexion.php?login=$encodedLog&passwd=$encodepass"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Connection: Log with succes")
                            val sessionNode = doc.getElementsByTagName("SESSION").item(0)
                            val signatureNode = doc.getElementsByTagName("SIGNATURE").item(0)
                            this.session = sessionNode.textContent.trim()
                            this.signature = signatureNode.textContent.trim()
                            this.connected = true
                            Log.d("signature",this.signature)
                            Log.d("session",this.session)



                        } else {
                            Log.e(TAG, "Connection: Erreur - $status")
                            // popup with status Error
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "Connection error")
                error.printStackTrace()
            })
        // ligne importante a ne pas oublier
        App.instance.requestQueue?.add(stringRequest)
    }

    fun hash(str : String ): String {
        /*
        permet le hashage en SHA256 du mot de passe qui est ensuite envoyé au serveur
         */
        val bytes = str.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
    fun getConnected() : Boolean {
        return this.connected
    }

    fun getSession() : String{
        return this.session
    }
    fun getSignature() : String{
        return this.signature
    }


    fun changeName(name : String,perso:Character) {
        /*
        fonction pour communiquer avec le serveur afin de changer le pseudo
         */

        if (!this.connected) {// on vérifie que l'on est bien connecter au serveur et que l'on ai récupérer la session et la signature
            Log.e(TAG,"Not Connected")
            return
        }

        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")
        val encodename = URLEncoder.encode(name, "UTF-8")
        val url = BASE_URL + "/changenom.php?session=$encodeses&signature=$encodesig&nom=$encodename"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Changename: Name Changed")
                            perso.changename(name)



                        } else {
                            Log.e(TAG, "Changename: Erreur - $status")
                            // popup with Changename Error
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "Changename error")
                error.printStackTrace()
            })
        // ligne importante a ne pas oublier
        App.instance.requestQueue?.add(stringRequest)
    }

    fun deplacement(character: Character){
        /*
        on envoie au serveur les nouvelles coordonnées, il faudra appeler cette fonction tout les x temps
         */



        if (!this.connected) {
            // on vérifie que l'on soit bien connecté au serveur, que l'on ai récupérer la session et la signature
            Log.e(TAG,"Not Connected")
            return
        }


        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")
        val encodelat = URLEncoder.encode(character.getlat(), "UTF-8")
        val encodelon = URLEncoder.encode(character.getlon(),"UTF-8")


        val url =
            BASE_URL + "/deplace.php?session=$encodeses&signature=$encodesig" +
                    "&lon=$encodelon&lat=$encodelat"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Deplacement: deplacer")
                            val listElementsVoisins = doc.getElementsByTagName("VOISINS").item(0).childNodes
                            val lastId = character.CListe.lastOrNull()?.id ?: -1

                            for (i in 0 until listElementsVoisins.length) {
                                val node = listElementsVoisins.item(i)

                                if (i >lastId){
                                    Log.d(TAG,"Id = $i plus grand que $lastId")
                                    val elem = node as Element
                                    val name = elem.getElementsByTagName("NOM").item(0).textContent
                                    val lon = elem.getElementsByTagName("LONGITUDE").item(0).textContent
                                    val lat = elem.getElementsByTagName("LATITUDE").item(0).textContent
                                    character.addvoisins(Voisins(i,name,lon,lat))

                                }
                            }



                        } else {
                            Log.e(TAG, "Deplacement: Erreur - $status")
                            // popup with Deplacement Error
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "Deplacement error")
                error.printStackTrace()
            })
        // ligne importante a ne pas oublier
        App.instance.requestQueue?.add(stringRequest)
    }

    fun statusplayer(character: Character) {
        /*
        demande au serveur de récupérer des information sur notre joueur
        inventaire/argent/position/niveau de pick
         */

        if (!this.connected) {
            // on vérifie que l'on est bien connecter au serveur et que l'on ai récupérer la session et la signature
            Log.e(TAG,"Not Connected")
            return
        }

        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")

        val url =
            BASE_URL + "/status_joueur.php?session=$encodeses&signature=$encodesig"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Status_joueur: Status obtenu")
                                                                                            // PAS FINI
                            val moneyNode = doc.getElementsByTagName("MONEY").item(0)
                            val money = moneyNode.textContent.trim()
                            Log.d("items",money)
                            character.setmoney(money)
                            val pickNode = doc.getElementsByTagName("PICKAXE").item(0)
                            val pick = pickNode.textContent.trim()
                            character.setpick(pick)
                            Log.d("pick",pick)
                            val positionNode = doc.getElementsByTagName("POSITION").item(0)
                            val pose = positionNode.textContent.trim()
                            Log.d("pose",pose)

                            val itemsNode = doc.getElementsByTagName("ITEMS").item(0).childNodes
                            val lastId = character.items.lastOrNull()?.id?.toInt() ?: -1
                            for (i in 0 until itemsNode.length) {
                                val node = itemsNode.item(i)
                                if (node.nodeType == Node.ELEMENT_NODE) {
                                    val elem = node as Element
                                    val id = elem.getElementsByTagName("ITEM_ID").item(0).textContent.toInt()
                                    if (id > lastId){
                                        Log.d(TAG,"Id = $id plus grand que $lastId")
                                        //val item_id = elem.getElementsByTagName("ITEM_ID").item(0).textContent
                                        val quantity = elem.getElementsByTagName("QUANTITE").item(0).textContent
                                        //character.addquantity(quantity)
                                        val item = Item(id.toString())
                                        item_detail(id.toString(),item)
                                        character.additems(item)
                                        item.setquantity(quantity)

                                    }
                                }
                            }
                            _item.postValue(character.items)


                        } else {
                            Log.e(TAG, "status_joueur: Erreur - $status")
                            // popup with Deplacement Error
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "status_joueur error")
                error.printStackTrace()
            })

        App.instance.requestQueue?.add(stringRequest)

    }

    fun reinit_player() {
        /*
        reset le joueur a 0 au niveau du serveur, il faut mettre un signal de warning afin d'éviter les miss click
         */

        if (!this.connected) {
            // on vérifie que l'on est bien connecter au serveur et que l'on ai récupéré la session et la signature
            Log.e(TAG, "Not Connected")
            return
        }

        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")

        val url =
            BASE_URL + "/reinit_joueur.php?session=$encodeses&signature=$encodesig"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    Log.d(TAG, "reset succesful")
                }catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }

            },
            { error ->
                Log.d(TAG, "reinit_joueur error")
                error.printStackTrace()
            })

        App.instance.requestQueue?.add(stringRequest)
    }

    fun dig(character: Character,game:GameActivity){

        if (!this.connected) {
            // on vérifie que l'on est bien connecté au serveur et que l'on ai récupérer la session et la signature
            Log.e(TAG,"Not Connected")
            return
        }


        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")
        val encodelat = URLEncoder.encode(character.getlat(), "UTF-8")
        val encodelon = URLEncoder.encode(character.getlon(),"UTF-8")


        val url =
            BASE_URL + "/creuse.php?session=$encodeses&signature=$encodesig" +
                    "&lon=$encodelon&lat=$encodelat"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Creuser: succesful dig")
                            val voisinsNode = doc.getElementsByTagName("VOISINS").item(0)
                            character.setvoisins(voisinsNode.textContent.trim())
                            Log.d("voisins",character.getvoisins())
                            val dethNode = doc.getElementsByTagName("DEPTH").item(0)
                            val deth = dethNode.textContent.trim()
                            Log.d("Deth",deth)
                            val itemNode = doc.getElementsByTagName("ITEM_ID").item(0)

                            if (itemNode != null) {
                                val item = itemNode.textContent.trim()
                                character.additems(Item(item))
                                Log.d("item got", item)
                                Toast.makeText(game,"Item : $item",Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            Log.e(TAG, "Creuser: Erreur - $status")
                            // popup with creuser Error avec le status attaché
                            Toast.makeText(game,status,Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "Creuser error")
                error.printStackTrace()
            })
        // ligne importante a ne pas oublier
        App.instance.requestQueue?.add(stringRequest)

    }

    fun item_detail(item_id : String,item: Item) {
        /*
        demande le résumé d'un object au serveur
         */

        if (!this.connected) {
            // on vérifie que l'on est bien connecté au serveur et que l'on ai récupéré la session et la signature
            Log.e(TAG, "Not Connected")
            return
        }
        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")
        val encodeitem = URLEncoder.encode(item_id,"UTF-8")

        val url =
            BASE_URL + "/item_detail.php?session=$encodeses&signature=$encodesig&item_id=$encodeitem"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Detail: detail obtenu")
                            val nomNode = doc.getElementsByTagName("NOM").item(0)
                            val nom = nomNode.textContent.trim()
                            Log.d("detail_item",nom)
                            item.setnom(nom)
                            val typeNode = doc.getElementsByTagName("TYPE").item(0)
                            val type = typeNode.textContent.trim()
                            Log.d("detail_item",type)
                            item.settype(type)
                            val rareteNode = doc.getElementsByTagName("RARETE").item(0)
                            val rarete = rareteNode.textContent.trim()
                            Log.d("detail_item",rarete)
                            item.setrarete(rarete)
                            val imageNode = doc.getElementsByTagName("IMAGE").item(0)
                            val image = imageNode.textContent.trim()
                            Log.d("detail_item",image)
                            item.setimage(image)
                            val decFrNode = doc.getElementsByTagName("DESC_FR").item(0)
                            val decFr = decFrNode.textContent.trim()
                            Log.d("detail_item",decFr)
                            item.setdecFr(decFr)
                            val decEnNode = doc.getElementsByTagName("DESC_EN").item(0)
                            val decEn = decEnNode.textContent.trim()
                            Log.d("detail_item",decEn)
                            item.setdecEn(decEn)
                        } else {
                        Log.e(TAG, "item detail: Erreur - $status")
                        // popup with detail Error avec le status attaché
                        }
                    }
                }catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }

            },
            { error ->
                Log.d(TAG, "item detail error")
                error.printStackTrace()
            })

        App.instance.requestQueue?.add(stringRequest)
    }

    fun getname(item_id: String,offers: Offers){
        if (!this.connected) {
            // on vérifie que l'on est bien connecté au serveur et que l'on ai récupéré la session et la signature
            Log.e(TAG, "Not Connected")
            return
        }

        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")
        val encodeitem = URLEncoder.encode(item_id,"UTF-8")
        var nom :String = ""
        val url =
            BASE_URL + "/item_detail.php?session=$encodeses&signature=$encodesig&item_id=$encodeitem"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Detail: detail obtenu")
                            val nomNode = doc.getElementsByTagName("NOM").item(0)
                            nom = nomNode.textContent.trim()
                            Log.d("detail_item",nom)
                            offers.setname(nom)
                        } else {
                            Log.e(TAG, "item detail: Erreur - $status")
                            // popup with detail Error avec le status attaché
                        }
                    }
                }catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }

            },
            { error ->
                Log.d(TAG, "item detail error")
                error.printStackTrace()
            })
        App.instance.requestQueue?.add(stringRequest)
    }

    fun marketlist(marchant: Marchant) {
        /*
        fonction pour communiquer avec le serveur pour obtenir les offres
         */

        if (!this.connected) {// on vérifie que l'on est bien connecté au serveur et que l'on ai récupéré la session et la signature
            Log.e(TAG,"Not Connected")
            return
        }

        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")

        val url = BASE_URL + "/market_list.php?session=$encodeses&signature=$encodesig"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "market_list: market_list obtained")
                            val offersNode=doc.getElementsByTagName("OFFERS").item(0).childNodes
                            val lastId = marchant.items.lastOrNull()?.offer_id ?: -1
                            for (i in 0 until offersNode.length) {
                                val node = offersNode.item(i)
                                if (node.nodeType == Node.ELEMENT_NODE) {
                                    val elem = node as Element
                                    val id = elem.getElementsByTagName("OFFER_ID").item(0).textContent.toInt()

                                    if (id > lastId) {
                                        Log.d(TAG,"Id = $id plus grand que $lastId")
                                        val item_id = elem.getElementsByTagName("ITEM_ID").item(0).textContent
                                        val quantity = elem.getElementsByTagName("QUANTITE").item(0).textContent
                                        val price = elem.getElementsByTagName("PRIX").item(0).textContent
                                        val offre = Offers(id,item_id,quantity,price)
                                        //getname(item_id,offre)
                                        item_detail(item_id,offre.item)
                                        marchant.additem(offre)
                                        //marchant.additem(Offers(id,item_id,quantity, price))
                                    }
                                }
                            }

                            /*
                            marchant.resetM()
                            while (i < offersNode.length){
                                val itemNode = doc.getElementsByTagName("item$i").item(0)

                                val offer_idNode = itemNode.firstChild
                                val offer_id = offer_idNode.textContent.trim()
                                Log.d("offres",offer_id)
                                val itemidNode = offer_idNode.nextSibling
                                val itemid = itemidNode.textContent.trim()
                                Log.d("item",itemid)
                                val quantityNode = itemidNode.nextSibling
                                val quantity = quantityNode.textContent.trim()
                                Log.d("item",quantity)
                                val prixNode = quantityNode.nextSibling
                                val prix = prixNode.textContent.trim()
                                Log.d("item",prix)

                                val offre = Offers(offer_id,itemid,quantity,prix)
                                getname(itemid,offre)
                                marchant.additem(offre)
                                oListe.add(offre)
                                Log.d("marchant","succes")
                                i += 1
                            }*/
                            _offers.postValue(marchant.items)
                        } else {
                            Log.e(TAG, "market_list: Erreur - $status")
                            // popup with market_list Error
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "market_list error")
                error.printStackTrace()
            })
        // ligne importante a ne pas oublier
        App.instance.requestQueue?.add(stringRequest)
    }
    fun sell(id: String,quantity:String,price:String) {

        /*
        reset le joueur a 0 au niveau du serveur, il faut mettre un signal de warning afin d'éviter les miss click
         */

        if (!this.connected) {
            // on vérifie que l'on est bien connecter au serveur et que l'on ai récupéré la session et la signature
            Log.e(TAG, "Not Connected")
            return
        }
        Log.d("id",id)
        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")
        val encodeid = URLEncoder.encode(id, "UTF-8")
        val encodequantity = URLEncoder.encode(quantity, "UTF-8")
        val encodeprice = URLEncoder.encode(price, "UTF-8")

        val url =
            BASE_URL + "/market_vendre.php?session=$encodeses&signature=$encodesig&item_id=$encodeid&quantite=$encodequantity&prix=$encodeprice"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {

                            Log.d(TAG, "vente succesful")
                        } else {
                        Log.e(TAG, "vente: Erreur - $status")
                        // popup with market_list Error
                    }

                    }
                }catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }

            },
            { error ->
                Log.d(TAG, "vente error")
                error.printStackTrace()
            })

        App.instance.requestQueue?.add(stringRequest)
    }

    fun buy(id: String) {
        /*
        reset le joueur a 0 au niveau du serveur, il faut mettre un signal de warning afin d'éviter les miss click
         */

        if (!this.connected) {
            // on vérifie que l'on est bien connecter au serveur et que l'on ai récupéré la session et la signature
            Log.e(TAG, "Not Connected")
            return
        }
        Log.d("id",id)
        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")
        val encodeid = URLEncoder.encode(id, "UTF-8")
        val url =
            BASE_URL + "/market_acheter.php?session=$encodeses&signature=$encodesig&offer_id=$encodeid"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {

                            Log.d(TAG, "achat succesful")
                        } else {
                            Log.e(TAG, "Achat: Erreur - $status")
                            // popup with market_list Error
                        }

                    }
                }catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }

            },
            { error ->
                Log.d(TAG, "reinit_joueur error")
                error.printStackTrace()
            })

        App.instance.requestQueue?.add(stringRequest)
    }
    fun upgradelist(){

        if (!this.connected) {
            // on vérifie que l'on est bien connecté au serveur et que l'on ai récupérer la session et la signature
            Log.e(TAG,"Not Connected")
            return
        }


        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")


        val url =
            BASE_URL + "/recettes_pioches.php?session=$encodeses&signature=$encodesig"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Creuser: succesful list")
                            val upNode=doc.getElementsByTagName("UPGRADES").item(0).childNodes
                            val lastId = upgrades.lastOrNull()?.pick_id ?: -1
                            for (i in 0 until upNode.length) {
                                val node = upNode.item(i)
                                Log.d("it","Pick")
                                if (node.nodeType == Node.ELEMENT_NODE) {
                                    val elem = node as Element
                                    val id = elem.getElementsByTagName("PICKAXE_ID").item(0).textContent.toInt()
                                    Log.d("idpick",id.toString())
                                    if (id > lastId) {
                                        val up = Upgrade(id)
                                        val itNode = elem.getElementsByTagName("ITEMS").item(0).childNodes
                                        val lastId2 = up.items.lastOrNull()?.id?.toInt() ?: -1
                                        for (y in 0 until itNode.length) {
                                            Log.d("it","item$y")
                                            val node2 = itNode.item(y)
                                            if (node2.nodeType == Node.ELEMENT_NODE) {
                                                val elem2 = node2 as Element
                                                val id2 = elem2.getElementsByTagName("ITEM_ID").item(0).textContent.toInt()
                                                //if (id2 > lastId2) {
                                                    val quantity = elem2.getElementsByTagName("QUANTITY").item(0).toString()
                                                    val item = Item(id2.toString())
                                                    item_detail(id2.toString(),item)
                                                    up.additems(item)
                                                    item.setquantity(quantity)
                                                //}
                                            }
                                        }
                                        this.upgrades+= up
                                    }
                                }
                            }

                            _upgrad.postValue(this.upgrades)
                        } else {
                            Log.e(TAG, "Creuser: Erreur - $status")
                            // popup with creuser Error avec le status attaché
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "Creuser error")
                error.printStackTrace()
            })
        //
        App.instance.requestQueue?.add(stringRequest)

    }
}
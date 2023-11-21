package ma.ensaf.bookappkotlin2

import java.sql.Timestamp

class ModelCategory {
    // variables must  match as in firebase
    var id:String=""
    var category:String=""
    var timestamp:Long=0
    var uid :String=""

    //empty constructeur required
    constructor()

    // paramered constructor
    constructor(id: String,category: String,timestamp: Long,uid: String) {
        this.id = id
        this.category = category
        this.timestamp = timestamp
        this.uid = uid

    }



}
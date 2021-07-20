package Models

class User {
    var id:Int? = null
    var name:String? = null
    var telNumber:String? = null
    var country:String? = null
    var address:String? = null
    var password:String? = null
    var imageUri:ByteArray? = null

    constructor(
        id: Int?,
        name: String?,
        telNumber: String?,
        country: String?,
        address: String?,
        password: String?,
        imageUri: ByteArray?
    ) {
        this.id = id
        this.name = name
        this.telNumber = telNumber
        this.country = country
        this.address = address
        this.password = password
        this.imageUri = imageUri
    }

    constructor(
        name: String?,
        telNumber: String?,
        country: String?,
        address: String?,
        password: String?,
        imageUri: ByteArray?
    ) {
        this.name = name
        this.telNumber = telNumber
        this.country = country
        this.address = address
        this.password = password
        this.imageUri = imageUri
    }

    constructor()

    override fun toString(): String {
        return "User(id=$id, name=$name, telNumber=$telNumber, country=$country, address=$address, password=$password, imageUri=$imageUri)"
    }
}
package kiu.dev.kyuhiltmvvm.model

data class RandomUserModel(
    val results: MutableList<User>
) {
    data class User(
        val gender: String,
        val name: NameClass,
        val email: String,
        val phone: String,
        val cell: String,
        val picture: PictureClass
    )

    data class NameClass(
        val title: String,
        val first: String,
        val last: String
    )

    data class PictureClass(
        val large: String,
        val medium: String,
        val thumbnail: String
    )
}




import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post (
    val id: Long,
    val title: String,

    @SerialName("created_at")
    val createdAt: String,

    val content: String,

    @SerialName("user_id")
    val userID: String? = null
)

@Serializable
data class PostRequest (
    val title: String? = null,
    val content: String? = null,

    @SerialName("user_id")
    val userID: String? = null
)

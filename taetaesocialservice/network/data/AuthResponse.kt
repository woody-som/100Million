import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse (
    @SerialName("access_token")
    val accessToken: String,

    @SerialName("token_type")
    val tokenType: String,

    @SerialName("expires_in")
    val expiresIn: Long,

    @SerialName("refresh_token")
    val refreshToken: String,

    val user: User
)

@Serializable
data class User (
    val id: String,
    val aud: String,
    val role: String,
    val email: String,

    @SerialName("email_confirmed_at")
    val emailConfirmedAt: String,

    val phone: String,

    @SerialName("confirmed_at")
    val confirmedAt: String,

    @SerialName("last_sign_in_at")
    val lastSignInAt: String,

    @SerialName("app_metadata")
    val appMetadata: AppMetadata,

    @SerialName("user_metadata")
    val userMetadata: UserMetadata,

    val identities: List<Identity>,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class AppMetadata (
    val provider: String,
    val providers: List<String>
)

@Serializable
data class Identity (
    val id: String,

    @SerialName("user_id")
    val userID: String,

    @SerialName("identity_data")
    val identityData: IdentityData,

    val provider: String,

    @SerialName("last_sign_in_at")
    val lastSignInAt: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class IdentityData (
    val email: String,
    val sub: String
)

@Serializable
class UserMetadata()
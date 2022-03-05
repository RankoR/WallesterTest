package page.smirnov.wallester.core_network.data.exception

class RequestFailureException(
    val code: Int,
    message: String? = null
) : Exception(message) {

    override fun toString(): String {
        return "Response code=$code, message: $message"
    }
}

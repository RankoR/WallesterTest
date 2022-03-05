package page.smirnov.wallester.core_network.data.parser.base

interface ResponseParser<out T> {
    fun parse(rawData: String): Result<T>
}

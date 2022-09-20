import kotlin.reflect.KClass

private val mapping = mapOf<String, KClass<*>>(
    "INT" to Int::class,
    "BIGINT" to Int::class,
    "VARCHAR" to String::class
)


fun toType(column: Pair<String, String>): Pair<String, KClass<*>> {
    // We don't care about sizes
    val type = column.second.substringBefore("(")

    return column.first to (mapping[type] ?: throw Error("column type not mapped ${column.second} possibilities $mapping"))
}

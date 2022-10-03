import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import kotlin.reflect.KClass

private val mapping = mapOf(
    "INT" to INT,
    "BIGINT" to INT,
    "VARCHAR" to STRING
)


fun toType(column: String): ClassName {
    // We don't care about sizes
    val type = column.substringBefore("(")

    return  mapping[type] ?: throw Error("column type not mapped ${column} possibilities $mapping")
}

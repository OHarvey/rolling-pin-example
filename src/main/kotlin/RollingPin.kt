import com.squareup.kotlinpoet.TypeName
import java.nio.file.Path

/**
 * To Hand Roll your entities!
 */
fun main() {
    val schemaParser = SchemaParser(Path.of("C:\\Users\\olive\\projects\\rollingpin\\src\\main\\resources\\schema.sql"))
    val definitions = schemaParser.toTableDefinitions()

    definitions
        .map { (name, line) -> name to line.map { parse(it) } }
        .forEach { (name, columns) ->
            EntityStencil(name, columns).doApply().writeTo(System.out)
        }
}

data class Column(val isNullable: Boolean, val type: String, val name: String) {

    fun formattedColumnName() =  if (name.first().isLowerCase()) name else name.replaceFirstChar { it.lowercase() }
    fun asTypeDef() : TypeName {
        val mappedType = toType(type)

        return mappedType.copy(nullable = isNullable)
    }
}

fun parse(line: String): Column {
    val normalised = line.replace(",", "")
    val nullable = normalised.contains("NOT NULL")
    val components = normalised.split(" ")
    val columnName = components.first()
    val shouldBeTheType = components.drop(1).first { it != "NOT" && it != "NULL" }

    return Column(isNullable = nullable, type = shouldBeTheType, name = columnName)
}

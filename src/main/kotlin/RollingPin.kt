import java.nio.file.Path

/**
 * To Hand Roll your entities!
 */
fun main() {
    val schemaParser = SchemaParser(Path.of("C:\\Users\\olive\\projects\\rollingpin\\src\\main\\resources\\schema.sql"))
    val definitions = schemaParser.toTableDefinitions()

    definitions.forEach { (name, columns) ->
        EntityStencil(name, columns).doApply()
    }
}

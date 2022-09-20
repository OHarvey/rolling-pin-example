import com.squareup.kotlinpoet.*
import java.time.LocalDateTime

class EntityStencil(private val tableName: String, private val columns: List<Pair<String, String>>) {

    fun doApply() {
        val (database, entityName) = tableNameToClassName(tableName)

        val dbNameAnnotationSpec = AnnotationSpec.builder(ClassName("", "DbName"))
            .addMember("name = %S", database)
            .build()

        val dbTableAnnotationSpec = AnnotationSpec.builder(ClassName("", "DbTable"))
            .addMember("name = %S", entityName)
            .build()

        FileSpec.builder("com.entities.$database", entityName)
            .addFileComment("Generated on %S", LocalDateTime.now())
            .addType(
                TypeSpec.classBuilder(entityName)
                    .addModifiers(KModifier.DATA)
                    .addAnnotation(dbNameAnnotationSpec)
                    .addAnnotation(dbTableAnnotationSpec)
                    .primaryConstructor(
                        constructorFor(columns)
                    ).build()
            )
            .build().writeTo(System.out)
    }

    private fun constructorFor(columns: List<Pair<String, String>>): FunSpec {
        return FunSpec.constructorBuilder().apply {
            columns.map { toType(it) }.forEach { (name, type) ->
                addParameter(columnNameToPropertyName(name), type)
            }
        }.build()
    }

    private fun columnNameToPropertyName(columnName: String) =
        if (columnName.first().isLowerCase()) columnName else columnName.replaceFirstChar { it.lowercase() }

    private fun tableNameToClassName(tableName: String): Pair<String, String> {
        val split = tableName.split(".")
        //Basically forget the schema we don't care!
        return split.first() to split.last()
    }
}

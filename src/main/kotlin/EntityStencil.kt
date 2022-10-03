import com.squareup.kotlinpoet.*
import java.time.LocalDateTime

class EntityStencil(private val tableName: String, private val columns: List<Column>) {

    fun doApply(): FileSpec {
        val (database, entityName) = tableNameToClassName(tableName)

        val dbNameAnnotationSpec = AnnotationSpec.builder(ClassName("", "DbName"))
            .addMember("name = %S", database)
            .build()

        val dbTableAnnotationSpec = AnnotationSpec.builder(ClassName("", "DbTable"))
            .addMember("name = %S", entityName)
            .build()

       return FileSpec.builder("com.entities.$database", entityName)
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
            .build()
    }

    private fun constructorFor(columns: List<Column>): FunSpec {
        return FunSpec.constructorBuilder().apply {
            columns.forEach {
                addParameter(it.formattedColumnName(), it.asTypeDef())
            }
        }.build()
    }

    private fun tableNameToClassName(tableName: String): Pair<String, String> {
        val split = tableName.split(".")
        //Basically forget the schema we don't care!
        return split.first() to split.last()
    }
}

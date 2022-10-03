import java.nio.file.Path

class SchemaParser(path: Path) {
    private val file = path.toFile()

    fun toTableDefinitions(): MutableMap<String, List<String>> {
        val tables = mutableMapOf<String, List<String>>()
        var currentTable = ""
        val currentColumns = ArrayList<String>()
        var foundTableDefinition = false

        for (line in file.readLines().asSequence()) {
            if (line.startsWith("CREATE TABLE")) {
                foundTableDefinition = true
                currentTable = line.replace("CREATE TABLE", "").replace("(", "").trim()
            } else if (foundTableDefinition && line.endsWith(";")) {
                tables[currentTable] = ArrayList<String>().also { it.addAll(currentColumns) }
                currentColumns.clear()
                foundTableDefinition = false
            } else if (foundTableDefinition) {
                currentColumns.add(normalise(line))
            }
        }
        return tables
    }

    private fun normalise(line: String) = line.trim()
}

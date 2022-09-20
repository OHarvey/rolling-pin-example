import java.nio.file.Path

class SchemaParser(path: Path) {
    private val file = path.toFile()

    fun toTableDefinitions(): Map<String, List<Pair<String, String>>> {
        val tables = mutableMapOf<String, List<Pair<String, String>>>()
        var currentTable = ""
        val currentColumns = ArrayList<Pair<String, String>>()
        var foundTableDefinition = false

        for (line in file.readLines().asSequence()) {
            if (line.startsWith("CREATE TABLE")) {
                foundTableDefinition = true
                currentTable = line.replace("CREATE TABLE", "").replace("(", "").trim()
            } else if (foundTableDefinition && line.endsWith(";")) {
                tables[currentTable] = ArrayList<Pair<String, String>>().also { it.addAll(currentColumns) }
                currentColumns.clear()
                foundTableDefinition = false
            } else if (foundTableDefinition) {
                currentColumns.addAll(normalise(line).split(" ").zipWithNext())
            }
        }
        return tables
    }

    private fun normalise(line: String) = line.trim().replace(",", "")
}

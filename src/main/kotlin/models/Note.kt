package models

data class Note(var noteTitle: String, var notePriority: Int, var noteCategory: String, var isNoteArchived :Boolean){
    override fun toString(): String = """
    Title: $noteTitle, Priority: $notePriority, Category: $noteCategory, Archived: $isNoteArchived
    """.trimIndent()
}

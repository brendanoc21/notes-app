package models

data class Note(var noteTitle: String, var notePriority: Int, var noteCategory: String, var isNoteArchived: Boolean, var noteContents: String) {
    override fun toString(): String = """
    Title: $noteTitle, Priority: $notePriority, Category: $noteCategory, Archived: $isNoteArchived,
            Contents: $noteContents
    """.trimIndent()
}

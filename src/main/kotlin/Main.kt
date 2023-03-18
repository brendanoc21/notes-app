import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
//private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))



fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |        NOTES APP PROJECT       |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List notes                |
         > |   3) Modify notes              |
         > |   4) Search notes              |
         > |   5) Count notes               |
         > |                                |
         > |   6) Save notes                |
         > |   7) Load notes                |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addNote()
            2  -> listNotes()
            3  -> modify()
            4  -> search()
            5  -> count()
            6  -> save()
            7  -> load()
            0  -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun addNote(){
    //logger.info { "addNote() function invoked" }
    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val noteContents = readNextLine("Enter contents of the note: ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false, noteContents))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listNotes() {
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View All notes          |
                  > |   2) View Active notes       |
                  > |   3) View Archived notes     |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllNotes();
            2 -> listActiveNotes();
            3 -> listArchivedNotes();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}

fun search() {
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) Search by Title         |
                  > |   2) Search by Category      |
                  > |   3) Search by Priority      |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> searchByTitle();
            2 -> searchByCategory();
            3 -> listNotesByPriority();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}

fun modify() {
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) Update a note           |
                  > |   2) Archive a note          |
                  > |   3) Delete a note           |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> updateNote();
            2 -> archiveNote();
            3 -> deleteNote();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}

fun count() {
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) Count by priority       |
                  > |   2) Count by category       |
                  > |   3) Count by active status  |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> countNotesByPriority();
            2 -> countNotesByCategory();
            3 -> countActiveArchived();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}

fun countActiveArchived(){
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) Count Active            |
                  > |   2) Count Archived          |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> countActiveNotes();
            2 -> countArchivedNotes();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}


fun updateNote() {
    //logger.info { "updateNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readNextLine("Enter a category for the note: ")
            val noteContents = readNextLine("Enter contents of the note: ")
            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false, noteContents))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}


fun deleteNote(){
    //logger.info { "deleteNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        //pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun exitApp(){
    //logger.info { "exitApp() function invoked" }
    exit(0)
}

fun listActiveNotes(){
    println(noteAPI.listActiveNotes())
}

fun listArchivedNotes(){
    println(noteAPI.listArchivedNotes())
}

fun listAllNotes() {
    println(noteAPI.listAllNotes())
}

fun listNotesByPriority(){
    val priority = readNextInt("Enter the priority(1-5) you wish to search: ")
    println(noteAPI.listNotesBySelectedPriority(priority))
}

fun countNotesByPriority(){
    val priority = readNextInt("Enter the priority(1-5) you wish to count: ")
    println(noteAPI.numberOfNotesByPriority(priority))
}

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun archiveNote() {
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        if (noteAPI.archiveNote(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun searchByTitle() {
    val input = readNextLine("Enter title of the note: ")
    if(noteAPI.searchByTitle(input) == ""){
        println("No notes of title found")
    }
    else{
        println(noteAPI.searchByTitle(input))
    }
}

fun searchByCategory() {
    val input = readNextLine("Enter category of the note: ")
    if(noteAPI.searchByCategory(input) == ""){
        println("No notes of category found")
    }
    else{
        println(noteAPI.searchByCategory(input))
    }
}

fun countNotesByCategory(){
    val category = readNextLine("Enter the category you wish to count: ")
    println(noteAPI.numberOfCategory(category))
}

fun countActiveNotes(){
    println(noteAPI.numberOfActiveNotes())
}

fun countArchivedNotes(){
    println(noteAPI.numberOfArchivedNotes())
}
package controllers

import models.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))
    private var emptyNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))

    @BeforeEach
    fun setup() {
        learnKotlin = Note("Learning Kotlin", 5, "College", false, "I am taking lessons on coding")
        summerHoliday = Note("Summer Holiday to France", 1, "Holiday", false, "The holiday will be fun")
        codeApp = Note("Code App", 4, "Work", true, "I am coding an app")
        testApp = Note("Test App", 4, "Work", false, "I am testing an app")
        swim = Note("Swim - Pool", 3, "Hobby", true, "There is a swimming pool nearby")

        // adding 5 Note to the notes api
        populatedNotes!!.add(learnKotlin!!)
        populatedNotes!!.add(summerHoliday!!)
        populatedNotes!!.add(codeApp!!)
        populatedNotes!!.add(testApp!!)
        populatedNotes!!.add(swim!!)
    }

    @AfterEach
    fun tearDown() {
        learnKotlin = null
        summerHoliday = null
        codeApp = null
        testApp = null
        swim = null
        populatedNotes = null
        emptyNotes = null
    }

    @Nested
    inner class AddNotes {
        @Test
        fun `adding a Note to a populated list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false, "I am taking lessons on coding")
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false, "I am taking lessons on coding")
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.add(newNote))
            assertEquals(1, emptyNotes!!.numberOfNotes())
            assertEquals(newNote, emptyNotes!!.findNote(emptyNotes!!.numberOfNotes() - 1))
        }
    }

    @Nested
    inner class ListNotes {

        @Test
        fun `listAllNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listAllNotes().lowercase().contains("no notes"))
        }

        @Test
        fun `listAllNotes returns Notes when ArrayList has notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listAllNotes().lowercase()
            assertTrue(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("swim"))
            assertTrue(notesString.contains("summer holiday"))
        }
    }

    @Nested
    inner class DeleteNotes {

        @Test
        fun `deleting a Note that does not exist, returns null`() {
            assertNull(emptyNotes!!.deleteNote(0))
            assertNull(populatedNotes!!.deleteNote(-1))
            assertNull(populatedNotes!!.deleteNote(5))
        }

        @Test
        fun `deleting a note that exists delete and returns deleted object`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(swim, populatedNotes!!.deleteNote(4))
            assertEquals(4, populatedNotes!!.numberOfNotes())
            assertEquals(learnKotlin, populatedNotes!!.deleteNote(0))
            assertEquals(3, populatedNotes!!.numberOfNotes())
        }

        @Test
        fun `listActiveNotes returns no active notes stored when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
            assertTrue(
                emptyNotes!!.listActiveNotes().lowercase().contains("no active notes")
            )
        }

        @Test
        fun `listActiveNotes returns active notes when ArrayList has active notes stored`() {
            assertEquals(3, populatedNotes!!.numberOfActiveNotes())
            val activeNotesString = populatedNotes!!.listActiveNotes().lowercase()
            assertTrue(activeNotesString.contains("learning kotlin"))
            assertFalse(activeNotesString.contains("code app"))
            assertTrue(activeNotesString.contains("summer holiday"))
            assertTrue(activeNotesString.contains("test app"))
            assertFalse(activeNotesString.contains("swim"))
        }

        @Test
        fun `listArchivedNotes returns no archived notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
            assertTrue(
                emptyNotes!!.listArchivedNotes().lowercase().contains("no archived notes")
            )
        }

        @Test
        fun `listArchivedNotes returns archived notes when ArrayList has archived notes stored`() {
            assertEquals(2, populatedNotes!!.numberOfArchivedNotes())
            val archivedNotesString = populatedNotes!!.listArchivedNotes().lowercase(Locale.getDefault())
            assertFalse(archivedNotesString.contains("learning kotlin"))
            assertTrue(archivedNotesString.contains("code app"))
            assertFalse(archivedNotesString.contains("summer holiday"))
            assertFalse(archivedNotesString.contains("test app"))
            assertTrue(archivedNotesString.contains("swim"))
        }

        @Test
        fun `listNotesBySelectedPriority returns No Notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(
                emptyNotes!!.listNotesBySelectedPriority(1).lowercase().contains("no notes")
            )
        }

        @Test
        fun `listNotesBySelectedPriority returns no notes when no notes of that priority exist`() {
            // Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val priority2String = populatedNotes!!.listNotesBySelectedPriority(2).lowercase()
            assertTrue(priority2String.contains("no notes"))
            assertTrue(priority2String.contains("2"))
        }

        @Test
        fun `listNotesBySelectedPriority returns all notes that match that priority when notes of that priority exist`() {
            // Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val priority1String = populatedNotes!!.listNotesBySelectedPriority(1).lowercase()
            assertTrue(priority1String.contains("1 note"))
            assertTrue(priority1String.contains("priority 1"))
            assertTrue(priority1String.contains("summer holiday"))
            assertFalse(priority1String.contains("swim"))
            assertFalse(priority1String.contains("learning kotlin"))
            assertFalse(priority1String.contains("code app"))
            assertFalse(priority1String.contains("test app"))

            val priority4String = populatedNotes!!.listNotesBySelectedPriority(4).lowercase(Locale.getDefault())
            assertTrue(priority4String.contains("2 note"))
            assertTrue(priority4String.contains("priority 4"))
            assertFalse(priority4String.contains("swim"))
            assertTrue(priority4String.contains("code app"))
            assertTrue(priority4String.contains("test app"))
            assertFalse(priority4String.contains("learning kotlin"))
            assertFalse(priority4String.contains("summer holiday"))
        }
    }

    @Nested
    inner class UpdateNotes {
        @Test
        fun `updating a note that does not exist returns false`() {
            assertFalse(populatedNotes!!.updateNote(6, Note("Updating Note", 2, "Work", false, "I am updating a note")))
            assertFalse(populatedNotes!!.updateNote(-1, Note("Updating Note", 2, "Work", false, "I am updating a note")))
            assertFalse(emptyNotes!!.updateNote(0, Note("Updating Note", 2, "Work", false, "I am updating a note")))
        }

        @Test
        fun `updating a note that exists returns true and updates`() {
            // check note 5 exists and check the contents
            assertEquals(swim, populatedNotes!!.findNote(4))
            assertEquals("Swim - Pool", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(3, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("Hobby", populatedNotes!!.findNote(4)!!.noteCategory)

            // update note 5 with new information and ensure contents updated successfully
            assertTrue(populatedNotes!!.updateNote(4, Note("Updating Note", 2, "College", false, "I am updating a note")))
            assertEquals("Updating Note", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(2, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("College", populatedNotes!!.findNote(4)!!.noteCategory)
        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.store()

            // Loading the empty notes.xml file into a new object
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            // Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            // Loading notes.xml into a different collection
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            // Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }
    }

    @Test
    fun `saving and loading an empty collection in JSON doesn't crash app`() {
        // Saving an empty notes.json file.
        val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
        storingNotes.store()

        // Loading the empty notes.json file into a new object
        val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
        loadedNotes.load()

        // Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
        assertEquals(0, storingNotes.numberOfNotes())
        assertEquals(0, loadedNotes.numberOfNotes())
        assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
    }

    @Test
    fun `saving and loading an loaded collection in JSON doesn't loose data`() {
        // Storing 3 notes to the notes.json file.
        val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
        storingNotes.add(testApp!!)
        storingNotes.add(swim!!)
        storingNotes.add(summerHoliday!!)
        storingNotes.store()

        // Loading notes.json into a different collection
        val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
        loadedNotes.load()

        // Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
        assertEquals(3, storingNotes.numberOfNotes())
        assertEquals(3, loadedNotes.numberOfNotes())
        assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
        assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
        assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
    }

    @Nested
    inner class ArchiveNotes {
        @Test
        fun `archiving a note that does not exist returns false`() {
            assertFalse(populatedNotes!!.archiveNote(6))
            assertFalse(populatedNotes!!.archiveNote(-1))
            assertFalse(emptyNotes!!.archiveNote(0))
        }

        @Test
        fun `archiving an active note that exists returns true and archives`() {
            assertFalse(populatedNotes!!.findNote(1)!!.isNoteArchived)
            assertTrue(populatedNotes!!.archiveNote(1))
            assertTrue(populatedNotes!!.findNote(1)!!.isNoteArchived)
            assertTrue(populatedNotes!!.archiveNote(1))
            assertFalse(populatedNotes!!.findNote(1)!!.isNoteArchived)
        }
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfNotesCalculatedCorrectly() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(0, emptyNotes!!.numberOfNotes())
        }

        @Test
        fun numberOfArchivedNotesCalculatedCorrectly() {
            assertEquals(2, populatedNotes!!.numberOfArchivedNotes())
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
        }

        @Test
        fun numberOfActiveNotesCalculatedCorrectly() {
            assertEquals(3, populatedNotes!!.numberOfActiveNotes())
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
        }

        @Test
        fun numberOfNotesByPriorityCalculatedCorrectly() {
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(1))
            assertEquals(0, populatedNotes!!.numberOfNotesByPriority(2))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(3))
            assertEquals(2, populatedNotes!!.numberOfNotesByPriority(4))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(5))
            assertEquals(0, emptyNotes!!.numberOfNotesByPriority(1))
        }

        @Test
        fun numberOfNotesByCategoryCalculatedCorrectly() {
            assertEquals(2, populatedNotes!!.numberOfCategory("work"))
            assertEquals(1, populatedNotes!!.numberOfCategory("college"))
            assertEquals(1, populatedNotes!!.numberOfCategory("holiday"))
            assertEquals(1, populatedNotes!!.numberOfCategory("hobby"))
            assertEquals(0, populatedNotes!!.numberOfCategory("play"))
        }
    }

    @Nested
    inner class SearchingMethods {
        @Test
        fun `searchByTitle returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByTitle("Learning Kotlin").lowercase().contains(""))
        }
        @Test
        fun `searchByTitle acquires correct title`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.searchByTitle("learning kotlin").lowercase().contains("college"))
            assertFalse(populatedNotes!!.searchByTitle("learning kotlin").lowercase().contains("work"))
            assertTrue(populatedNotes!!.searchByTitle("summer holiday to france").lowercase().contains("holiday"))
            assertTrue(populatedNotes!!.searchByTitle("code app").lowercase().contains("work"))
            assertTrue(populatedNotes!!.searchByTitle("test app").lowercase().contains("work"))
            assertTrue(populatedNotes!!.searchByTitle("swim - pool").lowercase().contains("hobby"))
        }

        @Test
        fun `searchByCategory returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByCategory("college").lowercase().contains(""))
        }
        @Test
        fun `searchByCategory acquires correct category`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.searchByCategory("college").lowercase().contains("learning kotlin"))
            assertFalse(populatedNotes!!.searchByCategory("college").lowercase().contains("summer"))
            assertTrue(populatedNotes!!.searchByCategory("holiday").lowercase().contains("summer"))
            assertTrue(populatedNotes!!.searchByCategory("work").lowercase().contains("code app"))
            assertTrue(populatedNotes!!.searchByCategory("work").lowercase().contains("test app"))
            assertTrue(populatedNotes!!.searchByCategory("hobby").lowercase().contains("swim"))
        }
    }
}

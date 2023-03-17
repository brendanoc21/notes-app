package controllers

import models.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI()
    private var emptyNotes: NoteAPI? = NoteAPI()

    @BeforeEach
    fun setup(){
        learnKotlin = Note("Learning Kotlin", 5, "College", false)
        summerHoliday = Note("Summer Holiday to France", 1, "Holiday", false)
        codeApp = Note("Code App", 4, "Work", true)
        testApp = Note("Test App", 4, "Work", true)
        swim = Note("Swim - Pool", 3, "Hobby", false)

        //adding 5 Note to the notes api
        populatedNotes!!.add(learnKotlin!!)
        populatedNotes!!.add(summerHoliday!!)
        populatedNotes!!.add(codeApp!!)
        populatedNotes!!.add(testApp!!)
        populatedNotes!!.add(swim!!)
    }

    @AfterEach
    fun tearDown(){
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
            val newNote = Note("Study Lambdas", 1, "College", false)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false)
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
    inner class ShowActiveNotes {

        @Test
        fun `showActiveNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.showActiveNotes().lowercase().contains("no notes"))
        }

        @Test
        fun `showActiveNotes returns Notes when ArrayList has active notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.showActiveNotes().lowercase()
            assertTrue(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("swim"))
            assertTrue(notesString.contains("summer holiday"))

            assertFalse(notesString.contains("code app"))
            assertFalse(notesString.contains("test app"))
        }
    }

    @Nested
    inner class ShowArchivedNotes {

        @Test
        fun `showArchivedNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.showArchivedNotes().lowercase().contains("no notes"))
        }

        @Test
        fun `showArchivedNotes returns Notes when ArrayList has archived notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.showArchivedNotes().lowercase()
            assertFalse(notesString.contains("learning kotlin"))
            assertFalse(notesString.contains("swim"))
            assertFalse(notesString.contains("summer holiday"))

            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("test app"))
        }
    }

    @Nested
    inner class CountActiveNotes {

        @Test
        fun `countActiveNotes returns 0 when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.countActiveNotes().equals(0))
        }

        @Test
        fun `countActiveNotes returns amount when ArrayList has active notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesInt = populatedNotes!!.countActiveNotes()
            assertTrue(notesInt.equals(3))
        }
    }

    @Nested
    inner class CountArchivedNotes {

        @Test
        fun `countArchivedNotes returns 0 when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.countArchivedNotes().equals(0))
        }

        @Test
        fun `countArchivedNotes returns amount when ArrayList has active notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesInt = populatedNotes!!.countArchivedNotes()
            assertTrue(notesInt.equals(2))
        }
    }

    @Nested
    inner class ShowNotesByPriority {

        @Test
        fun `listNotesBySelectedPriority returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listNotesBySelectedPriority(1).lowercase().contains("no notes"))
            assertTrue(emptyNotes!!.listNotesBySelectedPriority(2).lowercase().contains("no notes"))
            assertTrue(emptyNotes!!.listNotesBySelectedPriority(3).lowercase().contains("no notes"))
            assertTrue(emptyNotes!!.listNotesBySelectedPriority(4).lowercase().contains("no notes"))
            assertTrue(emptyNotes!!.listNotesBySelectedPriority(5).lowercase().contains("no notes"))
        }

        @Test
        fun `listNotesBySelectedPriority returns Notes when ArrayList has appropriate notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            var notesString = populatedNotes!!.listNotesBySelectedPriority(1).lowercase()
            assertTrue(notesString.contains("summer holiday"))
            assertFalse(notesString.contains("code app"))
            assertFalse(notesString.contains("test app"))
            assertFalse(notesString.contains("learning kotlin"))
            assertFalse(notesString.contains("swim"))


            notesString = populatedNotes!!.listNotesBySelectedPriority(2).lowercase()
            assertFalse(notesString.contains("summer holiday"))
            assertFalse(notesString.contains("code app"))
            assertFalse(notesString.contains("test app"))
            assertFalse(notesString.contains("learning kotlin"))
            assertFalse(notesString.contains("swim"))

            notesString = populatedNotes!!.listNotesBySelectedPriority(3).lowercase()
            assertFalse(notesString.contains("summer holiday"))
            assertFalse(notesString.contains("code app"))
            assertFalse(notesString.contains("test app"))
            assertFalse(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("swim"))

            notesString = populatedNotes!!.listNotesBySelectedPriority(4).lowercase()
            assertFalse(notesString.contains("summer holiday"))
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("test app"))
            assertFalse(notesString.contains("learning kotlin"))
            assertFalse(notesString.contains("swim"))

            notesString = populatedNotes!!.listNotesBySelectedPriority(5).lowercase()
            assertFalse(notesString.contains("summer holiday"))
            assertFalse(notesString.contains("code app"))
            assertFalse(notesString.contains("test app"))
            assertTrue(notesString.contains("learning kotlin"))
            assertFalse(notesString.contains("swim"))
        }
    }

    @Nested
    inner class NumberOfNotesByPriority {

        @Test
        fun `numberOfNotesByPriority returns 0 when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.countArchivedNotes().equals(0))
        }

        @Test
        fun `numberOfNotesByPriority returns amount when ArrayList has appropriate notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            var notesInt = populatedNotes!!.numberOfNotesByPriority(1)
            assertTrue(notesInt.equals(1))

            notesInt = populatedNotes!!.numberOfNotesByPriority(2)
            assertTrue(notesInt.equals(0))

            notesInt = populatedNotes!!.numberOfNotesByPriority(3)
            assertTrue(notesInt.equals(1))

            notesInt = populatedNotes!!.numberOfNotesByPriority(4)
            assertTrue(notesInt.equals(2))

            notesInt = populatedNotes!!.numberOfNotesByPriority(5)
            assertTrue(notesInt.equals(1))
        }
    }
}

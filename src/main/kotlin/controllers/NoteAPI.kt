package controllers

import models.Note

class NoteAPI {
    private var notes = ArrayList<Note>()

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun listAllNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                listOfNotes += "${i}: ${notes[i]} \n"
            }
            listOfNotes
        }
    }

    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun showActiveNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                if(!notes[i].isNoteArchived) {
                    listOfNotes += "${i}: ${notes[i]} \n"
                }
            }
            listOfNotes
        }
    }

    fun showArchivedNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                if(notes[i].isNoteArchived) {
                    listOfNotes += "${i}: ${notes[i]} \n"
                }
            }
            listOfNotes
        }
    }

    fun countArchivedNotes(): Int {
        return if (notes.isEmpty()) {
            0
        } else {
            var amountOfNotes = 0
            for (i in notes.indices) {
                if(notes[i].isNoteArchived) {
                    amountOfNotes ++
                }
            }
            amountOfNotes
        }
    }

    fun countActiveNotes(): Int {
        return if (notes.isEmpty()) {
            0
        } else {
            var amountOfNotes = 0
            for (i in notes.indices) {
                if(!notes[i].isNoteArchived) {
                    amountOfNotes ++
                }
            }
            amountOfNotes
        }
    }
}

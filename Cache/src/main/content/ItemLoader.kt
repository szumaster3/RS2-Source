package content

import com.alex.tools.ItemCopy

object ItemLoader {

    fun add() {
        val copy = ItemCopy.create().startAt(20000)
        copy.addItems({ name = "Devastator" })
        copy.addNoteItem()
        copy.addItems({ name = "Pliers" })
        copy.addNoteItem()
        copy.save()
    }
}
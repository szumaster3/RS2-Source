package content.data

object RandomEvent {
    /**
     * Returns the event path for logging out.
     */
    fun logout(): String = "/save:${javaClass.simpleName}-logout"

    /**
     * Returns the event path for saving the original location.
     */
    fun save(): String = "/save:original-loc"

    /**
     * Returns the event path for saving the score.
     */
    fun score(): String = "/save:${javaClass.simpleName}:score"
}

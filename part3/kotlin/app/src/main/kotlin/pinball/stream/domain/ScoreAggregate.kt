package pinball.stream.domain

const val DONE = "DONE"
const val GAME_OVER = "GAME OVER"
val DONE_MESSAGES = arrayOf(DONE, GAME_OVER)

class ScoreAggregate {
    private var done: Boolean = false
    private val aggregate: ScoreRecord = ScoreRecord(0, "0", "0")

    fun isDone(): Boolean {
        return done
    }

    fun process(key: String, value: String): ScoreAggregate {
        if (aggregate.gameId == "0") {
            aggregate.gameId = key;
        }
        if (DONE_MESSAGES.contains(value)) {
            this.done = true
        } else {
            aggregate.score += value.toLong(10)
        }
        return this
    }

    fun getScore(): ScoreRecord {
        return aggregate
    }
}
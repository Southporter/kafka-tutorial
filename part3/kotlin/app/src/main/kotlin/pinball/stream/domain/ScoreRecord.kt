package pinball.stream.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class ScoreRecord(
    @JsonProperty var score: Long,
    @JsonProperty var userId: String,
    @JsonProperty var gameId: String,
)


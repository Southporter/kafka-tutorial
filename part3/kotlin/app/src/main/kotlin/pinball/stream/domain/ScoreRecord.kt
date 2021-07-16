package pinball.stream.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class ScoreRecord(
    @JsonProperty val score: Long
)

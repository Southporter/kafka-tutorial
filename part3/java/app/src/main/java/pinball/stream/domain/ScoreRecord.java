package pinball.stream.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScoreRecord {
    @JsonProperty
    public Long score;

    public ScoreRecord() {
        score = 0L;
    }

    public ScoreRecord(Integer init) {
        score = Long.valueOf(init);
    }
}

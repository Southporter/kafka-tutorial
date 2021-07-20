package kafka.tutorial.scala
package domain

import com.fasterxml.jackson.annotation.JsonProperty

case class ScoreRecord(
                      @JsonProperty("score") score: Long
                      )

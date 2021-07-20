package pinball.stream.aggregators

import org.apache.kafka.streams.kstream.Aggregator
import pinball.stream.domain.ScoreAggregate

class ScoreAggregator : Aggregator<String, String, ScoreAggregate> {
    override fun apply(key: String?, value: String?, aggregate: ScoreAggregate): ScoreAggregate {
        if (aggregate == null) {

        }
        if (value == null) {
            return aggregate
        }
        return aggregate.process(key!!, value)
    }
}
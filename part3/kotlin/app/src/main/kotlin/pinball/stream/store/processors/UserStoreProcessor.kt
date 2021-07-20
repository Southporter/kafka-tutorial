package pinball.stream.store.processors

import org.apache.kafka.streams.processor.Processor
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.state.KeyValueStore
import pinball.stream.UserStoreName

class UserStoreProcessor : Processor<String, String> {
    private lateinit var store: KeyValueStore<String, String>
    override fun init(context: ProcessorContext?) {
        if (context != null) {
            store = context.getStateStore(UserStoreName)
        }
    }

    override fun process(key: String?, value: String?) {
        if (key != null && value != null) {
            store.put(key, value)
        }
    }

    override fun close() {
    }
}
package pinball.stream.store.processors

import org.apache.kafka.streams.processor.Processor
import org.apache.kafka.streams.processor.ProcessorSupplier

class UserStoreProcessorSupplier: ProcessorSupplier<String, String> {
    override fun get(): Processor<String, String> {
        return UserStoreProcessor()
    }
}
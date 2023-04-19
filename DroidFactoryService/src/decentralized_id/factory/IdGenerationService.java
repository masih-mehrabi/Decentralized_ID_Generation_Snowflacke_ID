package decentralized_id.factory;

import java.time.Instant;

public class IdGenerationService {

	private static final int FACTORY_ID_BITS = 4;
	private static final int SEQUENCE_BITS = 6;

	private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;


	private long lastTimestamp = -1L;
	private long sequence = 0L;

	public synchronized long generateId(int factoryId) {
		long currentTimestamp = timestamp();
		
		
		// TODO implement the ID generation algorithm as described.
		
		if (currentTimestamp < lastTimestamp) {
			throw new IllegalStateException("Clock moved backwards.");
		}
		
		if (currentTimestamp == lastTimestamp) {
			sequence = (sequence + 1) & MAX_SEQUENCE;
			if (sequence == 0) {
				// sequence overflowed, wait until next millisecond
				currentTimestamp = waitNextMillisecond(currentTimestamp);
			}
		} else {
			// new millisecond, reset sequence
			sequence = 0L;
		}
		
		lastTimestamp = currentTimestamp;
		
		return ((currentTimestamp - Instant.EPOCH.toEpochMilli()) << (FACTORY_ID_BITS + SEQUENCE_BITS))
				       | (factoryId << SEQUENCE_BITS)
				       | sequence;
	}
	
	// Get current timestamp in milliseconds and the difference to the UNIX epoch Jan, 1st 1970.
	private long timestamp() {
		return Instant.now().toEpochMilli();
	}
	private long waitNextMillisecond(long currentTimestamp) {
		while (timestamp() <= currentTimestamp) {
			// wait until next millisecond
		}
		return timestamp();
	}
	
	
	
	
	
	
}

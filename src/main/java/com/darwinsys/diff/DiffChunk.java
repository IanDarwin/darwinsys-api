package com.darwinsys.diff;

public class DiffChunk<T> {
	private DiffType type;
	private T data;
	
	/**
	 * Construct a DiffChunk
	 * @param data The data
	 * @param type The type
	 */
	public DiffChunk(T data, DiffType type) {
		super();
		this.data = data;
		this.type = type;
	}

	/** Get the data in a chunk.
	 * @return The data.
	 */
	public T getData() {
		return data;
	}

	/** Get the type of data in a chunk.
	 * @return The type.
	 */
	public DiffType getType() {
		return type;
	}
} 

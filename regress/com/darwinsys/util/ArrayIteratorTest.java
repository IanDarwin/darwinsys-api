	/** Simple tryout */
	public static void main(String unusedArgv[]) {
		ArrayIterator it = new ArrayIterator();
		while (it.hasNext())
			System.out.println(it.next());
		System.out.println("Testing error handling: expect an exception:");
		try {
			it.next();		// EXPECT RUNTIME ERROR
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Got expected exception -- OK!");
		}
	}

public abstract class MyFilter {
	MyFilter next;
	public abstract void write(byte[] data) throws MyFilterException;
	public void setNext(MyFilter next) {
		this.next = next;
	}
}

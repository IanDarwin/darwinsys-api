public class MyListModel extends java.util.ArrayList 
implements javax.swing.ListModel {

	public Object getElementAt(int index) {
		return get(index);
	}

	public int getSize() {
		return size();
	}

	public void removeListDataListener(javax.swing.event.ListDataListener l)  { 
	}

	public void addListDataListener(javax.swing.event.ListDataListener l)  {
	}
}

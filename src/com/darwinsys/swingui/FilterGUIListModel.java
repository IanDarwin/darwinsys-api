import java.util.*;
import javax.swing.event.*;

public class MyListModel extends ArrayList 
implements javax.swing.ListModel {

	protected Object source;

	MyListModel(Object source) {
		this.source = source;
	}

	public Object getElementAt(int index) {
		return get(index);
	}

	public int getSize() {
		return size();
	}

	ArrayList listeners = new ArrayList();

	public void removeListDataListener(javax.swing.event.ListDataListener l)  { 
		listeners.remove(l);
	}

	public void addListDataListener(javax.swing.event.ListDataListener l)  {
		listeners.add(l);
	}

	void notifyListeners() {
		// no attempt at optimziation
		ListDataEvent le = new ListDataEvent(source,
			ListDataEvent.CONTENTS_CHANGED, 0, getSize());
		for (int i=0; i<listeners.size(); i++) {
			((ListDataListener)listeners.get(i)).contentsChanged(le);
		}
	}
	// REMAINDER ARE OVERRIDES JUST TO CALL NOTIFYLISTENERS


	public boolean add(Object o)  {
		boolean b = super.add(o);
		if (b)
			notifyListeners();
		return b;
	}

	public void add(int index, Object element) {
		super.add(index, element);
		notifyListeners();
	}

	public boolean addAll(Collection o)  {
		boolean b = super.add(o);
		if (b)
          notifyListeners();
		return b;
	}

	public void clear()  {
		super.clear();
		notifyListeners();
	}

	public Object remove(int i) {
		Object o = super.remove(i);
		notifyListeners();
		return o;
	}

	public boolean remove(Object o) {
		boolean b = super.remove(o);
		if (b)
			notifyListeners();
		return b;
	}

	public Object set(int index, Object element) {
		Object o = super.set(index, element);
		notifyListeners();
		return o;
	}
}

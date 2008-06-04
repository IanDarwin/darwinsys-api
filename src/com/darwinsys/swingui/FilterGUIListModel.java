package com.darwinsys.swingui;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/** FilterGUIListModel combines an ArrayList with a ListModel for ease of use.
 */
public class FilterGUIListModel<E> extends ArrayList<E> implements ListModel {

	private static final long serialVersionUID = 4997952964701465432L;
	
	Object source;

	FilterGUIListModel(Object src) {
		source = src;
	}

	public Object getElementAt(int index) {
		return get(index);
	}

	public int getSize() {
		return size();
	}

	ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>();

	public void removeListDataListener(ListDataListener l)  { 
		listeners.remove(l);
	}

	public void addListDataListener(ListDataListener l)  {
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


	public boolean add(E o)  {
		boolean b = super.add(o);
		if (b)
			notifyListeners();
		return b;
	}

	public void add(int index, E element) {
		super.add(index, element);
		notifyListeners();
	}

	public boolean addAll(Collection<? extends E> o)  {
		boolean b = super.addAll(o);
		if (b)
          notifyListeners();
		return b;
	}

	public void clear()  {
		super.clear();
		notifyListeners();
	}

	public E remove(int i) {
		E o = super.remove(i);
		notifyListeners();
		return o;
	}

	public boolean remove(Object o) {
		boolean b = super.remove(o);
		if (b)
			notifyListeners();
		return b;
	}

	public E set(int index, E element) {
		E o = super.set(index, element);
		notifyListeners();
		return o;
	}
}

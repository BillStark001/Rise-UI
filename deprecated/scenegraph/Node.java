package com.billstark001.riseui.math.scenegraph;

import java.util.ArrayList;

public class Node {

	private Node up;
	private Node prev;
	private Node next;
	private Node down;
	
	// Dirty
	protected static final int DATA_COUNT = 32;
	protected boolean[] dirty_flag = new boolean[DATA_COUNT];
	
	// Copy
	public static final int COPY_NORMAL = 0;
	public static final int COPY_NO_HIERARCHY = 1;
	public static final int COPY_NO_ANIMATION = 2;
	public static final int COPY_NO_TAG = 3;
	public static final int COPY_NO_STATUS = 4;
	
	public Node() {
		
	}
	
	public Node(Node up) {
		if (up != null) up.InsertDown(this);
	}
	
	// Graph Maintenance
	// Current Node
	public Node getUp() {return up;}
	public boolean isAncestor(Node n) {
		boolean flag = false;
		while (!flag) {
			n = n.getUp();
			if (n == this) flag = true;
			if (n == null) break;
		}
		return flag;
	}
	public boolean isDescendant(Node n) {
		if (n == null) return false; 
		return n.isAncestor(this); 
	}
	public boolean removeFromList() {
		if (up == null) return false;
		if (prev == null && next == null) return false;
		Node cachep = this.prev, cachen = this.next;
		if (cachep != null) cachep.next = cachen;
		if (cachen != null) cachen.next = cachep;
		this.prev = this.next = this.up = null;
		return true;
	}

	// Previous Node
	public Node getPrev() {return prev;}
	public Node getFirst() {
		Node cache = this.prev;
		if (cache != null) {
			while (cache.getPrev() != null) cache = cache.getPrev();
			return cache;
		} else return this;
	}
	public boolean insertPrev(Node n) {
		if (n == null) return false;
		if (n.getPrev() != null || n.getNext() != null || n.getUp() != null) return false;
		n.prev = this.getPrev();
		n.next = this;
		n.up = this.up;
		if (this.getPrev() != null) this.getPrev().next = n;
		this.prev = n;
		return true;
	}
	public boolean removePrev() {
		if (prev == null) return false;
		Node cachep = prev.prev, cache = prev;
		if (cachep != null) cachep.next = this;
		this.prev = cachep;
		cache.prev = cache.next = cache.up = null;
		return true;
	}
	public boolean setPrev(Node n) {
		if (n == null) return false;
		if (n.getPrev() != null || n.getNext() != null || n.getUp() != null) return false;
		removePrev();
		return insertPrev(n);
	}

	// Next Node
	public Node getNext() {return next;}
	public Node getLast() {
		Node cache = this.next;
		if (cache != null) {
			while (cache.getNext() != null) cache = cache.getNext();
			return cache;
		} else return this;
	}
	public boolean insertNext(Node n) {
		if (n == null) return false;
		if (n.getPrev() != null || n.getNext() != null || n.getUp() != null) return false;
		n.next = this.getNext();
		n.prev = this;
		n.up = this.up;
		if (this.getNext() != null) this.getNext().prev = n;
		this.next = n;
		return true;
	}
	public boolean removeNext() {
		if (next == null) return false;
		Node cachen = next.next, cache = next;
		if (cachen != null) cachen.prev = this;
		this.next = cachen;
		cache.next = cache.prev = cache.up = null;
		return true;
	}
	public boolean setNext(Node n) {
		if (n == null) return false;
		if (n.getPrev() != null || n.getNext() != null || n.getUp() != null) return false;
		removeNext();
		return insertNext(n);
	}

	// Down Node
	public Node getDown() {return down;}
	public Node getDownLast() {
		Node cache = this.down;
		if (this.down != null)
			while (cache.getNext() != null) cache = cache.getNext();
		return cache;
	}
	public Node[] getDowns() {
		ArrayList<Node> ans = new ArrayList<Node>();
		if (this.down != null) {
			Node cache = this.down;
			while (cache.getNext() != null) {
				ans.add(cache);
				cache = cache.getNext();
			}
		} else return new Node[0];
		return ans.toArray(new Node[0]);
	}
	public boolean InsertDown(Node n) {
		if (n == null) return false;
		if (n.getPrev() != null || n.getNext() != null || n.getUp() != null) return false;
		if (this.down != null) this.down.insertPrev(n);
		this.down = n;
		n.up = this;
		return true;
	}
	public boolean InsertDownLast(Node n) {
		if (n == null) return false;
		if (n.getPrev() != null || n.getNext() != null || n.getUp() != null) return false;
		if (this.down != null) {
			Node cache = this.down;
			while (cache.getNext() != null) cache = cache.getNext();
			cache.insertNext(n);
		} else {
			this.down = n;
		}
		n.up = this;
		return true;
	}
	
	// Dirty Flags
	public void setDirty(int flag) {this.dirty_flag[flag] = true;}
	public boolean getDirty(int flag) {return this.dirty_flag[flag];}
	
	public Node copy(int mode) {
		Node ans = new Node();
		ans.up = up;
		ans.down = down;
		ans.prev = prev;
		ans.next = next;
		switch (mode) {
		case COPY_NO_ANIMATION:
			
			break;
		case COPY_NO_HIERARCHY:
			ans.up = ans.down = ans.prev = ans.next = null;
			break;
		case COPY_NO_TAG:
			
			break;
		case COPY_NO_STATUS:
			
			break;
		}
		return ans;
	}
	
}

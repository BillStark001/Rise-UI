package com.dddviewr.collada.content.visualscene;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dddviewr.collada.content.nodes.Node;
import com.dddviewr.collada.format.Base;

public class VisualScene extends Base {
	protected String name;
	protected Map<Integer, Node> nodes = new HashMap<Integer, Node>();

	public VisualScene(int id, String name) {
		super(id);
		this.setName(name);
	}
	public VisualScene(String id, String name) {
		super(id);
		this.setName(name);
	}
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node[] getNodes() {
		return this.nodes.values().toArray(new Node[0]);
	}
	
	public int[] getIds() {
		int[] ans = new int[nodes.size()];
		Integer[] tmp = nodes.keySet().toArray(new Integer[0]);
		for(int i = 0; i < ans.length; ++i) ans[i] = tmp[i];
		return ans;
	}
	
	public Node getNode(int id) {
		return this.nodes.get(id);
	}
	
	public String toString() {
		return "VisualScene (id: " + this.getId() + ", name: "
				+ this.name + ")";
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		for (Node n : this.nodes.values())
			n.dump(out, indent + 1);
	}

	public void addNode(Node n) {
		this.nodes.put(n.getId(), n);
	}
}
package com.dddviewr.collada.content.nodes;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.content.visualscene.BaseXform;
import com.dddviewr.collada.content.visualscene.InstanceController;
import com.dddviewr.collada.content.visualscene.InstanceGeometry;
import com.dddviewr.collada.content.visualscene.InstanceNode;
import com.dddviewr.collada.content.visualscene.Matrix;
import com.dddviewr.collada.format.Base;

public class Node extends Base {
	protected String name;
	protected String sid;
	protected boolean type;
	protected List<BaseXform> xforms = new ArrayList<BaseXform>();
	protected List<InstanceGeometry> instanceGeometry = new ArrayList<InstanceGeometry>();
	protected InstanceController instanceController;
	protected InstanceNode instanceNode;
	protected List<Node> childNodes = new ArrayList<Node>();
	protected Matrix matrix;

	public Node(int id, String name, String sid, String type) {
		super(id);
		this.name = name;
		this.sid = sid;
		this.setType(type);
	}
	public Node(String id, String name, String sid, String type) {
		super(id);
		this.name = name;
		this.sid = sid;
		this.setType(type);
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public boolean isJoint() {
		return this.type;
	}
	
	public String getType() {
		if (isJoint()) return "JOINT";
		else return "NODE";
	}

	public void setType(String type) {
		if (type == null) {
			this.type = false;
			return;
		}
		else if (type.equals("JOINT")) this.type = true;
		else this.type = false;
	}
	
	public void setIsJoint(boolean b) {
		this.type = b;
	}

	public BaseXform[] getXforms() {
		return this.xforms.toArray(new BaseXform[0]);
	}

	public List<InstanceGeometry> getInstanceGeometry() {
		return this.instanceGeometry;
	}

	public void setInstanceGeometry(List<InstanceGeometry> instanceGeometry) {
		this.instanceGeometry = instanceGeometry;
	}
	
	public void addInstanceGeometry(InstanceGeometry instanceGeometry) {
		this.instanceGeometry.add(instanceGeometry);
	}

	public InstanceController getInstanceController() {
		return this.instanceController;
	}

	public void setInstanceController(InstanceController instanceController) {
		this.instanceController = instanceController;
	}
	
	public InstanceNode getInstanceNode() {
		return this.instanceNode;
	}

	public void setInstanceNode(InstanceNode instanceNode) {
		this.instanceNode = instanceNode;
	}

	public List<Node> getChildNodesList() {
		return new ArrayList<Node>(this.childNodes);
	}
	
	public Node[] getChildNodes() {
		return this.childNodes.toArray(new Node[0]);
	}
	
	public Matrix getMatrix() {
		return this.matrix;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
	
	public String toString() {
		return String.format("Node (id: %d, name: %s, sid: %s, type: %s)", 
				this.getId(), this.getName(), this.getSid(), this.getType());
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		for (BaseXform xform : this.xforms) {
			xform.dump(out, indent + 1);
		}
		for (InstanceGeometry geom : this.instanceGeometry) {
			geom.dump(out, indent + 1);
		}
		if (this.instanceController != null)
			this.instanceController.dump(out, indent + 1);
		if (this.instanceNode != null)
			this.instanceNode.dump(out, indent + 1);
		for (Node n : this.childNodes)
			n.dump(out, indent + 1);
	}

	public void addXform(BaseXform xform) {
		this.xforms.add(xform);
	}

	public void addNode(Node n) {
		this.childNodes.add(n);
	}
}
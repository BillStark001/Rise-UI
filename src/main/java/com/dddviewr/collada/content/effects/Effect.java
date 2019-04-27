package com.dddviewr.collada.content.effects;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.format.Base;

public class Effect extends Base {
	protected String id;
	protected List<NewParam> newParams = new ArrayList<NewParam>();
	protected EffectMaterial effectMaterial;

	public Effect(String id) {
		super(id);
	}

	public EffectMaterial getEffectMaterial() {
		return this.effectMaterial;
	}

	public void setEffectMaterial(EffectMaterial effectMerial) {
		this.effectMaterial = effectMerial;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		Base.dump(out, indent, this.newParams);
		if (this.effectMaterial != null)
			this.effectMaterial.dump(out, indent + 1);
	}

	public void addNewParam(NewParam newParam) {
		this.newParams.add(newParam);
	}

	public NewParam findNewParam(int sid) {
		if (this.newParams == null)
			return null;
		for (NewParam p : this.newParams) {
			if (sid == p.getSid()) return p;
		}
		return null;
	}
}
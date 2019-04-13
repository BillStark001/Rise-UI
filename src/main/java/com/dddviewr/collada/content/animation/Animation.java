package com.dddviewr.collada.content.animation;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.Source;
import com.dddviewr.collada.format.Base;

public class Animation extends Base {
	protected String id;
	protected List<Source> sources = new ArrayList<Source>();
	protected Sampler sampler;
	protected Channel channel;

	public Animation(String id) {
		this.id = id;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		for (Source s : this.sources) {
			s.dump(out, indent + 1);
		}
		if (this.sampler != null)
			this.sampler.dump(out, indent + 1);
		if (this.channel != null)
			this.channel.dump(out, indent + 1);
	}

	public void addSource(Source src) {
		this.sources.add(src);
	}

	public List<Source> getSources() {
		return new ArrayList<Source>(this.sources);
	}

	public Sampler getSampler() {
		return this.sampler;
	}

	public void setSampler(Sampler sampler) {
		this.sampler = sampler;
	}

	public Channel getChannel() {
		return this.channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Source getSource(int id) {
		for (Source s : this.sources) {
			if (s.getId() == id) return s;
		}
		return null;
	}

	public Source getSourceFromSemantic(String sem) {
		for (Input inp : this.sampler.getInputs()) {
			if (inp.getSemantic().equals(sem)) {
				return getSource(inp.getSource());
			}
		}
		return null;
	}
}
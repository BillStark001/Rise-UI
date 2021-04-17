package com.billstark001.riseui.base.fields;

import com.billstark001.riseui.computation.Matrix;
import com.dddviewr.collada.states.newparam;

public class OprFuncSimple<T, S> extends Operator<T> {

	private Operator<S> source;
	
	public Operator<S> getSource() {return this.source;}
	public void setSource(Operator<S> source) {this.source = source;} 
	public void setSource(S source) {this.setSource(new OprConstSimple<S>(source));} 
	
	@FunctionalInterface
	public interface Generator<T, S> {T gen(S source);}
	
	private Generator<T, S> generator;
	
	public Generator<T, S> getGenerator() {return this.generator;}
	public void setGenerator(Generator<T, S> gen) {this.generator = gen;} 

	public OprFuncSimple(Operator<S> source, Generator<T, S> gen) {
		this.setSource(source);
		this.setGenerator(gen);
	}
	
	public OprFuncSimple<T, S> rasterize(double time) {
		return new OprFuncSimple<T, S>(new OprConstSimple<S>(this.getSource().get(time)), this.getGenerator());
	}
	
	@Override
	public T get(double time) {
		S src = source.get(time);
		return this.getGenerator().gen(src);
	}

	@Override
	public boolean containsFrames() {
		return source.containsFrames();
	}

	@Override
	public double getStartTime() {
		return source.getStartTime();
	}

	@Override
	public double getEndTime() {
		return source.getEndTime();
	}
	
	private Class datatype = null;
	@Override
	public Class getDataType() {
		if (this.datatype == null) {
			T tmpt = this.get(getStartTime());
			if (tmpt != null) this.datatype = tmpt.getClass();
		}
		return this.datatype;
	}

}

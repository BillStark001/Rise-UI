package com.billstark001.riseui.base.fields;

import com.billstark001.riseui.computation.Matrix;
import com.dddviewr.collada.states.newparam;

public class FieldGenSimple<T, S> extends Field<T> {

	private Field<S> source;
	
	public Field<S> getSource() {return this.source;}
	public S getSource(double time) {return this.source.get(time);}
	public void setSource(Field<S> source) {this.source = source;} 
	public void setSource(S source) {this.source = new FieldSimple<S>(source);} 
	
	@FunctionalInterface
	public interface Generator<T, S> {T gen(S source);}
	
	private Generator<T, S> generator;
	
	public Generator<T, S> getGenerator() {return this.generator;}
	public void setGenerator(Generator<T, S> gen) {this.generator = gen;} 

	public FieldGenSimple(Field<S> source, Generator<T, S> gen) {
		this.setSource(source);
		this.setGenerator(gen);
	}
	
	public FieldGenSimple<T, S> rasterize(double time) {
		return new FieldGenSimple<T, S>(new FieldSimple<S>(this.getSource(time)), this.getGenerator());
	}
	
	@Override
	public T get(double time) {
		S src = source.get(time);
		return generator.gen(src);
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

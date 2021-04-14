package com.billstark001.riseui.base.fields;

import com.billstark001.riseui.base.fields.FieldGenSimple.Generator;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Utils3D;
import com.billstark001.riseui.computation.Vector;
import com.sun.jna.platform.win32.WinBase.SYSTEM_INFO.PI;

import scala.tools.nsc.doc.model.ModelFactory.PackageImpl;

public class FieldUtils {

	public static class KeyFrame<T> implements Comparable<KeyFrame<T>> {
		public double time;
		public T val;
		public Interpolation<T> interp;
		
		public KeyFrame(double time, T val, Interpolation<T> interp) {
			this.time = time;
			this.val = val;
			this.interp = interp;
		}
		
		public KeyFrame(KeyFrame<T> f) {
			this.time = f.time;
			this.val = f.val;
			this.interp = f.interp;
		}
		
		public KeyFrame(double time, T val) {this(time, val, new Step<T>());}
		
		@Override
		public int compareTo(KeyFrame<T> o) {
			if (this.time > o.time) return 1;
			else if (this.time < o.time) return -1;
			else return 0;
	    }
		
		@Override
		public String toString() {
			String template = "F(T=%g, VAL=%g, ITP=%s)";
			return String.format(template, this.time, this.val, this.interp);
		}
	
	}
	
	public static interface Interpolation<T> {
		public T calc(double t, KeyFrame<T> f0, KeyFrame<T> f1);
	}
	public static class Step<T> implements Interpolation<T> {
		public Step() {}
		public T calc(double t, KeyFrame<T> f0, KeyFrame<T> f1) {return f0.val;}
	}
	
	public static class LinearD implements Interpolation<Double> {
		public LinearD() {}
		public Double calc(double t, KeyFrame<Double> f0, KeyFrame<Double> f1) {
			return f0.val * (1 - t) + f1.val * t;
		}
	}
	
	public static class LinearI implements Interpolation<Integer> {
		public LinearI() {}
		public Integer calc(double t, KeyFrame<Integer> f0, KeyFrame<Integer> f1) {
			return (int) (f0.val * (1 - t) + f1.val * t);
		}
	}
	
	public static class Bezier3D implements Interpolation<Double> {
		public Vector tanIn, tanOut;
		public Bezier3D(Vector tanIn, Vector tanOut) {
			this.tanIn = tanIn;
			this.tanOut = tanOut;
		}
		public Bezier3D() {
			this(Vector.UNIT0_D2, Vector.UNIT0_D2);
		}
		@Override
		public Double calc(double t, KeyFrame<Double> f0, KeyFrame<Double> f1) {
			Vector p0 = new Vector(f0.time, f0.val);
			Vector p1 = Vector.UNIT0_D2;
			if (f0.interp instanceof Bezier3D) p1 = ((Bezier3D)f0.interp).tanOut;
			Vector p2 = Vector.UNIT0_D2;
			if (f1.interp instanceof Bezier3D) p2 = ((Bezier3D)f1.interp).tanIn;
			Vector p3 = new Vector(f1.time, f1.val);
			return Utils3D.bezier3(t, p0, p1, p2, p3).get(1);
		}
		
	}
	
	public static final Generator<Matrix, Vector> GEN_POS = Utils3D::posToHomoState;
	public static final Generator<Matrix, Vector> GEN_ROT = Utils3D::rotToHomoState;
	public static final Generator<Matrix, Vector> GEN_SCL = Utils3D::sclToHomoState;
	
	public static Vector rotZoom(Vector rot) {return rot.mult(Math.PI / 180);}
	public static final Generator<Vector, Vector> GEN_ROTZOOM = FieldUtils::rotZoom;
	
	//public static FieldGenSingle<Vector, Vector> zoom(Field<Vector> f, double factor) {
	//	Vector zm(Vector v) {return v.mult(factor);}
	//	return new FieldGenSingle<Vector, Vector> (f, zm);
	//}
}

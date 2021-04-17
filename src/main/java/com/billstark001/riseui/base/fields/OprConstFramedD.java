package com.billstark001.riseui.base.fields;

import java.util.Arrays;

import com.billstark001.riseui.computation.Utils3D;
import com.billstark001.riseui.computation.Vector;

public class OprConstFramedD extends OprConstFramed<Double> {

	public static class KeyFrame implements Comparable<KeyFrame> {
		public double time;
		public double val;
		public Interpolation interp;
		public Vector tanIn;
		public Vector tanOut;
		
		public KeyFrame(double time, double val, Interpolation interp, Vector tanIn, Vector tanOut) {
			this.time = time;
			this.val = val;
			this.interp = interp;
			this.tanIn = tanIn;
			this.tanOut = tanOut;
		}
		
		public KeyFrame(KeyFrame f) {
			this.time = f.time;
			this.val = f.val;
			this.interp = f.interp;
			this.tanIn = f.tanIn;
			this.tanOut = f.tanOut;
		}
		
		public KeyFrame(double time, double val) {this(time, val, Interpolation.LINEAR, Vector.UNIT0_D2, Vector.UNIT0_D2);}
		
		@Override
		public int compareTo(KeyFrame o) {
			if (this.time > o.time) return 1;
			else if (this.time < o.time) return -1;
			else return 0;
	    }
		
		@Override
		public String toString() {
			String template = "F(%s INTERP, T=%g, VAL=%g, TAN=%s, %s)";
			return String.format(template, this.interp, this.time, this.val, this.tanIn, this.tanOut);
		}
	
	}
	
	public static enum Interpolation {
		STEP,
		LINEAR,
		BEZIER3;
	}
	
	private final KeyFrame[] frames;
	
	public OprConstFramedD(KeyFrame[] framesIn) {
		super(0.);
		int fc = framesIn.length;
		KeyFrame[] frames = new KeyFrame[fc];
		for (int i = 0; i < fc; ++i) {
			frames[i] = new KeyFrame(framesIn[i]);
		}
		Arrays.sort(frames);
		this.frames = frames;
	}
	
	public OprConstFramedD(double[] times, Double[] t_val, Interpolation[] interps, Vector[] tanIn, Vector[] tanOut) {
		super(0.);
		int fc = 2147483647;
		fc = Math.min(fc, times.length);
		fc = Math.min(fc, t_val.length);
		fc = Math.min(fc, interps.length);
		fc = Math.min(fc, tanIn.length);
		fc = Math.min(fc, tanOut.length);
		KeyFrame[] frames = new KeyFrame[fc];
		for (int i = 0; i < fc; ++i) {
			frames[i] = new KeyFrame(times[i], t_val[i], interps[i], tanIn[i], tanOut[i]);
		}
		Arrays.sort(frames);
		this.frames = frames;
	}
	
	public OprConstFramedD(double[] times, double[] vals) {
		super(0.);
		int fc = 2147483647;
		fc = Math.min(fc, times.length);
		fc = Math.min(fc, vals.length);
		KeyFrame[] frames = new KeyFrame[fc];
		for (int i = 0; i < fc; ++i) {
			frames[i] = new KeyFrame(times[i], vals[i]);
		}
		Arrays.sort(frames);
		this.frames = frames;
	}
	
	public OprConstFramedD(double vals) {
		super(0.);
		KeyFrame[] frames = new KeyFrame[1];
		frames[0] = new KeyFrame(0, vals);
		this.frames = frames;
	}
	
	@Override
	public boolean containsFrames() {return this.frames.length > 0;}
	
	private boolean timeFitsIndex(double time, int index) {
		return this.frames[index].time <= time && this.frames[index + 1].time > time;
	}
	
	private int findIndexByTime(double time) {
		if (time < this.frames[0].time) return -1;
		else if (time >= this.frames[this.frames.length - 1].time) return this.frames.length - 1;
		else {
			// left included, right excluded
			int left = 0, right = this.frames.length - 1;
			int cur = (int) Math.floor((left + right) / 2);
			while (!timeFitsIndex(time, cur)) {
				if (this.frames[cur].time == time && this.frames[cur + 1].time == time) {
					cur -= 1;
					continue;
				}
				if (this.frames[cur].time > time) right = cur;
				else if (this.frames[cur + 1].time < time) left = cur;
				cur = (int) Math.floor((left + right) / 2);
			}
			return cur;
		}
	}

	@Override
	public Double get(double time) {
		int index = this.findIndexByTime(time);
		if (index == -1) return this.frames[0].val;
		if (index == -2 || index >= this.frames.length - 1) return this.frames[this.frames.length - 1].val;
		else {
			KeyFrame f0 = this.frames[index];
			KeyFrame f1 = this.frames[index + 1];
			double interp_t = (time - f0.time) / (f1.time - f0.time);
			Vector p0 = new Vector(f0.time, f0.val);
			Vector p1 = f0.tanOut;
			Vector p2 = f1.tanIn;
			Vector p3 = new Vector(f1.time, f1.val);
			if (f0.interp.equals(Interpolation.BEZIER3))
				return Utils3D.bezier3(interp_t, p0, p1, p2, p3).get(1);
			else if (f0.interp.equals(Interpolation.LINEAR))
				return Utils3D.linear(interp_t, p0, p3).get(1);
			else
				return f0.val;
		}
	}

	@Override
	public double getStartTime() {
		if (!this.containsFrames()) return 0;
		else return this.frames[0].time;
	}

	@Override
	public double getEndTime() {
		if (!this.containsFrames()) return 0;
		else return this.frames[this.frames.length - 1].time;
	}

	@Override
	public Class getDataType() {
		return Double.class;
	}
	

}

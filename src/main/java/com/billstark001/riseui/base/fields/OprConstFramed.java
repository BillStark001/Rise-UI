package com.billstark001.riseui.base.fields;

import java.util.Arrays;

import com.billstark001.riseui.base.fields.OprUtils.Interpolation;
import com.billstark001.riseui.base.fields.OprUtils.KeyFrame;

public class OprConstFramed<T> extends Operator<T> {
	

	private final KeyFrame<T>[] frames;
	
	public OprConstFramed(KeyFrame<T>[] framesIn) {
		int fc = framesIn.length;
		KeyFrame<T>[] frames = new KeyFrame[fc];
		for (int i = 0; i < fc; ++i) {
			frames[i] = new KeyFrame<T>(framesIn[i]);
		}
		Arrays.sort(frames);
		this.frames = frames;
	}
	
	public OprConstFramed(double[] times, T[] vals, Interpolation<T>[] interps) {
		int fc = 2147483647;
		fc = Math.min(fc, times.length);
		fc = Math.min(fc, vals.length);
		fc = Math.min(fc, interps.length);
		KeyFrame<T>[] frames = new KeyFrame[fc];
		for (int i = 0; i < fc; ++i) {
			frames[i] = new KeyFrame<T>(times[i], vals[i], interps[i]);
		}
		Arrays.sort(frames);
		this.frames = frames;
	}
	
	public OprConstFramed(double[] times, T[] vals) {
		int fc = 2147483647;
		fc = Math.min(fc, times.length);
		fc = Math.min(fc, vals.length);
		KeyFrame<T>[] frames = new KeyFrame[fc];
		for (int i = 0; i < fc; ++i) {
			frames[i] = new KeyFrame<T>(times[i], vals[i]);
		}
		Arrays.sort(frames);
		this.frames = frames;
	}
	
	public OprConstFramed(T vals) {
		KeyFrame<T>[] frames = new KeyFrame[1];
		frames[0] = new KeyFrame<T>(0, vals);
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
	public T get(double time) {
		int index = this.findIndexByTime(time);
		if (index == -1) return this.frames[0].val;
		if (index == -2 || index >= this.frames.length - 1) return this.frames[this.frames.length - 1].val;
		else {
			KeyFrame<T> f0 = this.frames[index];
			KeyFrame<T> f1 = this.frames[index + 1];
			double interp_t = (time - f0.time) / (f1.time - f0.time);
			return f0.interp.calc(interp_t, f0, f1);
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

	private Class datatype = null;
	@Override
	public Class getDataType() {
		if (this.datatype == null) {
			T tmpt = this.get(getStartTime());
			if (tmpt != null) this.datatype = tmpt.getClass();
		}
		return this.datatype;
	}
	
	@Override
	public String toString() {
		return String.format("%s<%s> (ContainsFrames: %s)", this.getClass().getSimpleName(), this.getDataType().getSimpleName(), this.containsFrames());
	}
	
}

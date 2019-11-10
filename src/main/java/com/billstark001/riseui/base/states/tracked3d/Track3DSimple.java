package com.billstark001.riseui.base.states.tracked3d;

import java.util.ArrayList;
import java.util.Arrays;

import com.billstark001.riseui.base.states.simple3d.State3DBase;
import com.billstark001.riseui.base.states.simple3d.State3DSimple;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Utils3D;

public class Track3DSimple extends Track3DBase {

	public static double DEFAULT_INTERVAL = 1 / 60;
	
	public static class KeyFrame implements Comparable<KeyFrame> {
		public double time;
		public Matrix val;
		
		public KeyFrame(double time, Matrix val) {
			this.time = time;
			this.val = val;
		}
		
		public KeyFrame(KeyFrame f) {
			this.time = f.time;
			this.val = f.val;
		}
		
		@Override
		public int compareTo(KeyFrame o) {
			if (this.time > o.time) return 1;
			else if (this.time < o.time) return -1;
			else return 0;
	    }
	
	}
	
	private boolean linear = false;
	public void setLinearInterp(boolean val) {this.linear = val;}
	public boolean isLinearInterp() {return this.linear;}
	public void setLinearInterp() {this.linear = true;}
	public void setStepInterp() {this.linear = false;}
	
	private final KeyFrame[] frames;
	
	public Track3DSimple(KeyFrame[] framesIn) {
		int fc = framesIn.length;
		KeyFrame[] frames = new KeyFrame[fc];
		for (int i = 0; i < fc; ++i) {
			frames[i] = new KeyFrame(framesIn[i]);
		}
		Arrays.sort(frames);
		this.frames = frames;
	}
	
	public Track3DSimple(double[] times, Matrix[] vals) {
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
	
	public Track3DSimple(ArrayList<KeyFrame> framesIn) {
		int fc = framesIn.size();
		KeyFrame[] frames = new KeyFrame[fc];
		for (int i = 0; i < fc; ++i) {
			frames[i] = new KeyFrame(framesIn.get(i));
		}
		Arrays.sort(frames);
		this.frames = frames;
	}
	
	public static Track3DSimple render(Track3DBase track) {return render(track, track.getStartTime(), track.getEndTime(), DEFAULT_INTERVAL);}
	public static Track3DSimple render(Track3DBase track, double tinterval) {return render(track, track.getStartTime(), track.getEndTime(), tinterval);}
	public static Track3DSimple render(Track3DBase track, double tstart, double tend) {return render(track, tstart, tend, DEFAULT_INTERVAL);}
	public static Track3DSimple render(Track3DBase track, double tstart, double tend, int fps) {return render(track, tstart, tend, 1 / fps);}
	public static Track3DSimple render(Track3DBase track, double tstart, double tend, double tinterval) {
		if (track == null) return null;
		if (tstart > tend || tinterval <= 0) return null;
		ArrayList<KeyFrame> frames = new ArrayList<KeyFrame>();
		for (double time = tstart; time <= tend; time += tinterval) {
			KeyFrame ft = new KeyFrame(time, track.get(time));
			frames.add(ft);
		}
		return new Track3DSimple(frames);
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
	public Matrix get(double time) {
		int index = this.findIndexByTime(time);
		if (index == -1) return this.frames[0].val;
		if (index == -2 || index >= this.frames.length - 1) return this.frames[this.frames.length - 1].val;
		else {
			KeyFrame f0 = this.frames[index];
			KeyFrame f1 = this.frames[index + 1];
			double interp_t = (time = f0.time) / (f1.time - f0.time);
			if (this.isLinearInterp())
				return Utils3D.linear(interp_t, f0.val, f1.val);
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
	public State3DBase getSimpleState(double time) {
		return new State3DSimple(this.get(time));
	}

}

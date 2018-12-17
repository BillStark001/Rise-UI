package com.billstark001.wdcs.algorithm;

import java.util.Comparator;

import com.billstark001.wdcs.algorithm.TracePos;

public class TracePos{
	public int x,y,z;
	private int f,g,h;
	private TracePos parent,target;
	public TracePos(int x,int y,int z){this.x=x;this.y=y;this.z=z;}//Target TPoint
	public TracePos(TracePos t){this.x=t.x;this.y=t.y;this.z=t.z;this.f=t.getF();this.g=t.getG();this.h=t.getH();this.parent=t.getParent();this.target=t.getTarget();}//Special TPoint
	public TracePos(int x,int y,int z,TracePos t1,TracePos t2){this.x=x;this.y=y;this.z=z;this.target=t1;calcH();this.setParent(t2);calcF();}//Regular TPoint
	public TracePos(int x,int y,int z,int g,TracePos t){this.x=x;this.y=y;this.z=z;this.g=g;this.target=t;calcH();calcF();}//Base TPoint
	public void setParent(TracePos p){this.parent=p;calcG();}
	public TracePos getParent(){return parent;}
	public TracePos getTarget(){return target;}
	public void calcH(){
		//this.h=Math.abs(target.z-this.z)+Math.abs(target.x-this.x)+Math.abs(target.y-this.y);this.h*=10;
		this.h=(int)(10*Math.sqrt((target.z-this.z)*(target.z-this.z)+(target.x-this.x)*(target.x-this.x)+(target.y-this.y)*(target.y-this.y)));
		}
	public void calcG(){
		this.g=(int)(10*Math.sqrt((parent.z-this.z)*(parent.z-this.z)+(parent.x-this.x)*(parent.x-this.x)+(parent.y-this.y)*(parent.y-this.y)));
		//this.g=Math.abs(parent.z-this.z)+Math.abs(parent.x-this.x)+Math.abs(parent.y-this.y);this.g*=10;
		this.g+=parent.g;
	}
	public void calcF(){this.f=this.g+this.h;}
	public int getF(){return this.f;}
	public int getG(){return this.g;}
	public int getH(){return this.h;}
	public boolean equals(Object o){TracePos t=(TracePos) o;return this.x==t.x&&this.y==t.y&&this.z==t.z;}
	
	public int compareto(TracePos t){if(this.f>t.f)return 1;else if(this.f==t.f)return 0;else return -1;}
	
	public static Comparator dc=new Comparator(){  
        public int compare(Object arg0, Object arg1) {   
        	TracePos t1=(TracePos)arg0;  
        	TracePos t2=(TracePos)arg1;  
            return t1.compareto(t2);  
        }  
    };
}

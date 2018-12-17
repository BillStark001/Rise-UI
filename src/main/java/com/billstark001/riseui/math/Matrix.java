package com.billstark001.riseui.math;

public final class Matrix {
	
	private final Pair size;
	private final int x, y;
	private final Vector[] elements;
	//x lines y columns
	
	public Matrix(Pair size) {
		if(!size.isDouble())this.size = size;
		else this.size = new Pair((Integer)size.getX(), (Integer)size.getY());
		this.x = (Integer) size.getX();
		this.y = (Integer) size.getY();
		elements = new Vector[x];
		for(int i = 0; i < x; ++i) {
			elements[i] = Vector.Zeros(y);
		}
	}
	public Matrix(int x, int y) {this(new Pair(x, y));}
	
	public Matrix(double[][] elements) {
		x = elements.length;
		y = elements[0].length;
		size = new Pair(x, y);
		this.elements = new Vector[x];
		for(int i = 0; i < x; ++i) {
			this.elements[i] = new Vector(elements[i]);
		}
	}
	
	public Matrix(Vector[] elements) {
		x = elements.length;
		y = Vector.maxDim(elements);
		size = new Pair(x, y);
		this.elements = new Vector[x];
		for(int i = 0; i < x; ++i) {
			if(elements[i].getDimension() != y)elements[i] = elements[i].concatenate(new double[y - elements[i].getDimension()]);
			this.elements[i] = new Vector(elements[i]);
		}
	}
	
	public Matrix(Vector element, int repeat) {
		x = repeat;
		y = element.getDimension();
		size = new Pair(x, y);
		this.elements = new Vector[x];
		for(int i = 0; i < x; ++i) this.elements[i] = element;
	}
	
	public static final Matrix I(int size) {
		double[][] temp = new double[size][size];
		for(int i = 0; i < size; ++i) temp[i][i] = 1;
		return new Matrix(temp);
	}
	
	//Base functions
	public final Pair getSize() {return size;}
	
	public final double get(int x, int y) {return elements[y].get(x);}
	public final double get(Pair position) {return get((Integer)position.getX(), (Integer)position.getY());}
	
	public boolean equals(Matrix m) {
		if(!size.equals(m.size)) return false;
		for(int i = 0; i < x; ++i) if(!elements[i].equals(m.elements[i])) return false;
		return true;
	}
	
	public final Matrix get(int startx, int starty, int endx, int endy) {
		Vector[] temp = new Vector[endy - starty];
		for(int i = 0; i < endy - starty; ++i) {
			temp[i] = elements[i + starty].get(startx, endx);
		}
		return new Matrix(temp);
	}
	public final Matrix getLines(int start, int end) {return this.get(0, start, y, end);}
	public final Matrix getColumns(int start, int end) {return this.get(start, 0, end, x);}
	
	public final Vector getLine(int line) {return elements[line];}
	public final Vector getColumn(int column) {
		double[] temp = new double[x];
		for(int i = 0; i < x; ++i) temp[i] = elements[i].get(column);
		return new Vector(temp);
	}
	
	public final boolean isSquare() {return y == x;}
	
	public final Matrix concatenate(Matrix M, boolean LineWise) {
		Vector[] temp;
		if(LineWise){
			if(!size.getX().equals(M.size.getX()))
				try {
					throw new Exception("Can't concatenate matrices of " + size + " and " + M.size + ".");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			temp = new Vector[x];
			for(int i = 0; i < x; ++i) temp[i] = elements[i].concatenate(M.getLine(i));
		} else {
			if(!size.getY().equals(M.size.getY()))
				try {
					throw new Exception("Can't concatenate matrices of " + size + " and " + M.size + ".");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			int z = (Integer)M.getSize().getX();
			temp = new Vector[x + z];
			for(int i = 0; i < temp.length; ++i){
				if(i < z) temp[i] = elements[i];
				else temp[i] = M.getColumn(i - z);
			}
		}
		return new Matrix(temp);
	}
	
	public final String toString() {
		StringBuffer temp = new StringBuffer("[");
		for(int i = 0; i < x; ++i) {
			if (i > 0) temp.append(' ');
			temp.append(elements[i].toString(true));
			if(i != x - 1)temp.append("\n");
		}
		temp.append("]");
		return temp.toString();
	}
	
	//Math functions
	public final Matrix multScalar(double lambda) {
		Vector[] temp = new Vector[x];
		for(int i = 0; i < x; ++i) temp[i] = elements[i].mult(lambda);
		return new Matrix(temp);
	}
	
	public final Matrix T() {
		Vector[] temp = new Vector[y];
		for(int i = 0; i < y; ++i) temp[i] = this.getColumn(i);
		return new Matrix(temp);
	}
	
	public final Matrix mult(Matrix M) {
		compareSize(this, M, true);
		int z_ = (Integer)M.getSize().getY();
		M = M.T();
		double[][] temp = new double[x][z_];
		for(int i = 0; i < x; ++i) 
		for(int j = 0; j < z_; ++j) temp[i][j] = this.getLine(i).dot(M.getLine(j));
		return new Matrix(temp);
	}
	
	public final Matrix add(Matrix M) {
		compareSize(this, M, false);
		Vector[] temp = new Vector[x];
		for(int i = 0; i < x; ++i) temp[i] = elements[i].add(M.getLine(i));
		return new Matrix(temp);
	}
	
	public final Vector[] toVecArray() {
		Vector[] temp = new Vector[elements.length];
		for(int i = 0; i < temp.length; ++i) {
			temp[i] = new Vector(elements[i]);
		}
		return temp;
	}
	
	//Utils
	public static Matrix unit(int size) {
		double[][] dtemp = new double[0][0];
		for(int i = 0; i < size; ++i) {
			dtemp[i][i] = 1;
		}
		return new Matrix(dtemp);
	}
	
	public static boolean compareSize(Matrix m1, Matrix m2, boolean isMult) {
		if(isMult) {
			if(m1.size.getY().equals(m2.size.getX())) return true;
			else
				try {
					throw new Exception("No declarations of multiplication on " + m1.size + " and " + m2.size + ". The dimension y of the first matrix should be the same as the dimension x of the second matrix.");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} else {
			if(m1.size.equals(m2.size)) return true;
			else
				try {
					throw new Exception("Varisized matrices! (" + m1.size + " and " + m2.size + ")");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return false;
	}
}

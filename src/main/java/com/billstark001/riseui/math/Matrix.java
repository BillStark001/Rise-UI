package com.billstark001.riseui.math;

import java.util.AbstractCollection;

public final class Matrix {
	
	private final Pair size;
	private final int x, y;
	private final Vector[] elements;
	//x lines y columns
	
	public Matrix(Pair size) {
		this.size = size;
		this.x = size.getX();
		this.y = size.getY();
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
			this.elements[i] = elements[i];
		}
	}
	
	public Matrix(Vector element) {this(element, 0, 1, 0);}
	public Matrix(Vector element, int stack) {this(element, 0, stack, 0);}
	public Matrix(Vector element, int length, int stack, double def) {
		if (length > element.getDimension()) element = Vector.expand(element, length, def);
		this.elements = new Vector[stack];
		x = stack;
		y = element.getDimension();
		this.size = new Pair(x, y);
		for (int i = 0; i < x; ++i) this.elements[i] = element;
	}
	
	public Matrix(AbstractCollection<Vector> elements) {
		x = elements.size();
		int y = 0; for (Vector v: elements) y = Math.max(y, v.getDimension()); this.y = y;
		size = new Pair(x, y);
		this.elements = new Vector[x];
		int i = 0;
		for (Vector v: elements){
			if(v.getDimension() != y) v = v.concatenate(new double[y - v.getDimension()]);
			this.elements[i] = v;
			++i;
		}
	}
	
	private static final double[][] di1 = {{1}};
	private static final double[][] di2 = {{1, 0}, {0, 1}};
	private static final double[][] di3 = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
	private static final double[][] di4 = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
	public static final Matrix I1 = new Matrix(di1);
	public static final Matrix I2 = new Matrix(di2);
	public static final Matrix I3 = new Matrix(di3);
	public static final Matrix I4 = new Matrix(di4);
	
	//Base functions
	public final Pair getSize() {return size;}
	
	public final double get(int line, int column) {return elements[line].get(column);}
	public final double get(Pair position) {return get(position.getX(), position.getY());}
	
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
			if(!(size.getX() == M.size.getX()))
				try {
					throw new Exception("Can't concatenate matrices of " + size + " and " + M.size + ".");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			temp = new Vector[x];
			for(int i = 0; i < x; ++i) temp[i] = elements[i].concatenate(M.getLine(i));
		} else {
			if(!(size.getY() == M.size.getY()))
				try {
					throw new Exception("Can't concatenate matrices of " + size + " and " + M.size + ".");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			int z = M.getSize().getX();
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
		return elements;
	}
	
	public final double[][] to2DArray() {
		double[][] ans = new double[this.getSize().getX()][this.getSize().getY()];
		for (int i = 0; i < this.getSize().getX(); ++i)
		for (int j = 0; j < this.getSize().getY(); ++j) {
			ans[i][j] = this.get(i, j);
		}
		return ans;
	}
	
	//Utils
	public static Matrix I(int size) {return unit(size);}
	public static Matrix unit(int size) {
		if (size < 1) return null;
		if (size == 1) return I1;
		if (size == 2) return I2;
		if (size == 3) return I3;
		if (size == 4) return I4;
		double[][] dtemp = new double[size][size];
		for(int i = 0; i < size; ++i) {
			dtemp[i][i] = 1;
		}
		return new Matrix(dtemp);
	}
	
	public static Matrix homoExtend(Matrix m, int dimension) {
		if (m.size.getX() != m.getSize().getY()) return null;
		if (dimension < m.size.getX()) return null;
		if (dimension == m.size.getX()) return m;
		double[][] dans = new double[dimension][dimension];
		for (int i = 0; i < m.size.getX(); ++i) for (int j = 0; j < m.size.getX(); ++j) {
			dans[i][j] = m.get(i, j);
		}
		for (int i = m.size.getX(); i < dimension; ++i) dans[i][i] = 1;
		Matrix ans = new Matrix(dans);
		return ans;
	}
	
	public static Matrix expandLine(Matrix m, int length, double def) {
		if (length < m.size.getX()) return null;
		if (length == m.size.getX()) return m;
		Vector[] dans = new Vector[length];
		Vector vt = new Vector(def, m.getSize().getY(), true);
		for (int i = 0; i < m.getSize().getX(); ++i) dans[i] = m.getLine(i);
		for (int i = m.getSize().getX(); i < length; ++i) dans[i] = vt;
		return new Matrix(dans);
	}
	public static Matrix expandColumn(Matrix m, int length, double def) {
		if (length < m.size.getY()) return null;
		if (length == m.size.getY()) return m;
		double[][] dans = new double[m.getSize().getX()][length];
		for (int i = 0; i < m.getSize().getX(); ++i) {
			for (int j = 0; j < m.size.getY(); ++j) {
				//System.out.println(new Pair(i, j));
				dans[i][j] = m.get(i, j);
			}
			for (int j = m.size.getY(); j < length; ++j) {
				//System.out.println(new Pair(i, j));
				dans[i][j] = def;
			}
		}
		return new Matrix(dans);
	}
	
	public static boolean compareSize(Matrix m1, Matrix m2, boolean isMult) {
		if(isMult) {
			if(m1.size.getY() == m2.size.getX()) return true;
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
	
	public static Matrix inverse(Matrix m) {
		if (!m.isSquare()) return null;
		int dim = m.getSize().getX();
		double[][] src = m.to2DArray();

		int i, j, row, col, k;
		double max, temp;
		int[] p = new int[dim];
		double[][] b = new double[dim][dim];
		for (i = 0; i < dim; i++) {
			p[i] = i;
			b[i][i] = 1;
		}

		for (k = 0; k < dim; ++k) {
			// find the pivot
			max = 0;
			row = col = i;
			for (i = k; i < dim; i++)
				for (j = k; j < dim; j++) {
					temp = Math.abs(b[i][j]);
					if (max < temp) {
						max = temp;
						row = i;
						col = j;
					}
				}
			// switch line and column, turn pivot to pos(k, k)
			if (row != k) {
				for (j = 0; j < dim; j++) {
					temp = src[row][j];
					src[row][j] = src[k][j];
					src[k][j] = temp;
					temp = b[row][j];
					b[row][j] = b[k][j];
					b[k][j] = temp;
				}
				i = p[row];
				p[row] = p[k];
				p[k] = i;
			}
			if (col != k) {
				for (i = 0; i < dim; i++) {
					temp = src[i][col];
					src[i][col] = src[i][k];
					src[i][k] = temp;
				}
			}
			// process
			for (j = k + 1; j < dim; j++)
				src[k][j] /= src[k][k];
			for (j = 0; j < dim; j++)
				b[k][j] /= src[k][k];
			src[k][k] = 1;

			for (j = k + 1; j < dim; j++) {
				for (i = 0; i < k; i++)
					src[i][j] -= src[i][k] * src[k][j];
				for (i = k + 1; i < dim; i++)
					src[i][j] -= src[i][k] * src[k][j];
			}
			for (j = 0; j < dim; j++) {
				for (i = 0; i < k; i++)
					b[i][j] -= src[i][k] * b[k][j];
				for (i = k + 1; i < dim; i++)
					b[i][j] -= src[i][k] * b[k][j];
			}
			for (i = 0; i < k; i++)
				src[i][k] = 0;
			src[k][k] = 1;
		}
		// recover mat. order
		for (j = 0; j < dim; j++)
			for (i = 0; i < dim; i++)
				src[p[i]][j] = b[i][j];
		return new Matrix(src);
	}
}

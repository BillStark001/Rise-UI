package com.billstark001.riseui.computation;

public class UtilsLinalg {

	/**
	 * Solve the Determinant.
	 * @param M Matrix
	 * @return double, the solution of the determinant.
	 *   * M must be square, otherwise return NAN.
	 */
	public static double solveDeterminant(Matrix M) {
		if (!M.isSquare()) return Math.sqrt(-1);
		boolean sym_swi_mark = false;
		int dim = M.getShape().getX();
		Vector[] src = M.toVecArray();
		
		double cur_val, tmp_val;
		
		// process i-th line
		// use Gaussian elimination to transfer to a down triangle matrix
		for (int i = 0; i < dim; ++i) {
			
			cur_val = src[i].get(i);
			// if cur_val == 0 find the first != 0 if can't find return 0
			if (cur_val == 0) {
				int row_not0 = -1;
				for (int k = i + 1; k < dim; ++k) {
					if (src[k].get(i) != 0) {
						cur_val = src[k].get(i);
						Vector tmp = src[i];
						src[i] = src[k];
						src[k] = tmp;
						sym_swi_mark = !sym_swi_mark;
						row_not0 = k;
						break;
					}
				}
				if (row_not0 == -1) return 0;
			}
			
			// Gaussian elimination
			for (int j = i + 1; j < dim; ++j) {
				if (i == j) continue;
				tmp_val = src[j].get(i);
				if (tmp_val == 0) continue;
				src[j] = src[j].add(src[i].mult(-tmp_val / cur_val)); // eliminate
			}
		}
		
		// Calculate value
		double ans = 1;
		if (sym_swi_mark) ans = -1;
		for (int i = 0; i < dim; ++i) ans *= src[i].get(i);
		return ans;
		
	}
	
	/**
	 * Solve a homogeneous equation.
	 * @param M Matrix
	 * @return Vector, a normalized linear-independent solution of the equation.
	 *   * M must be square, otherwise return null.
	 */
	public static Vector solveHomoEq(Matrix M) {
		double zero_threshold = 1e-15;
		Vector ans = Vector.UNIT0_D2;
		while (ans.getLength() == 0 && zero_threshold < 1e-7) {
			ans = solveHomoEq(M, zero_threshold);
			zero_threshold *= 10;
		}
		return ans;
	}
	
	/**
	 * Solve a homogeneous equation.
	 * @param M Matrix
	 * @param zero_threshold double less than this value will be proceed as 0. 
	 * @return Vector, a normalized linear-independent solution of the equation.
	 *   * M must be square, otherwise return null.
	 */
	public static Vector solveHomoEq(Matrix M, double zero_threshold) {
		
		if (!M.isSquare()) return null;
		int dim = M.getShape().getX();
		Vector[] src = M.toVecArray();
		double[] cst = new double[dim];
		
		double cur_val, tmp_val;
		
		// process i-th line
		// use Gaussian elimination to transfer to a down triangle matrix
		for (int i = 0; i < dim; ++i) {
			
			cur_val = src[i].get(i);
			// if cur_val == 0 find the first != 0 if can't find return 0
			if (cur_val == 0) {
				int row_not0 = -1;
				for (int k = i + 1; k < dim; ++k) {
					if (src[k].get(i) != 0) {
						cur_val = src[k].get(i);
						Vector tmp = src[i];
						src[i] = src[k];
						src[k] = tmp;
						row_not0 = k;
						break;
					}
				}
			}
			
			// Gaussian elimination
			for (int j = i + 1; j < dim; ++j) {
				if (i == j) continue;
				tmp_val = src[j].get(i);
				if (tmp_val == 0) continue;
				src[j] = src[j].add(src[i].mult(-tmp_val / cur_val)); // eliminate
			}
		}
		
		// for (Vector v: src) System.out.println(v);System.out.println();
		
		// Calculate value
		double srci;
		for (int i = dim - 1; i > -1; --i) {
			srci = src[i].get(i);
			if (Math.abs(srci) < zero_threshold) cst[i] = srci = 1;
			else cst[i] *= 1 / srci;
			for (int j = 0; j < i; ++j) {
				cst[j] -= cst[i] * src[j].get(i);
			}
		}
		
		// System.out.println(new Vector(cst));
		
		return new Vector(cst).normalize();
		
	}
	
	/**
	 * Solve a linear equation using Gaussian elimination method.
	 * @param A (n, n)-like matrix, the coefficient matrix of a linear equation.
	 * @param b (n)-like vector, the constant series of the same equation. 
	 *   * each dimension's length must be the same. 
	 * @return Vector, the solution of the equation.
	 *   * null will be returned if inputs are problematic.
	 *   * null will be returned if the equation consists of zero or multiple solutions.
	 */
	public static Vector solveLineEq(Matrix A, Vector b) {
		if (!A.isSquare()) return null;
		if (A.getShape().getX() != b.getDimension()) return null;
		if (A.getShape().getX() == 0) return null;
		
		int dim = b.getDimension();
		Vector[] src = A.toVecArray();
		double[] cst = b.toArray();
		
		double cur_val, tmp_val;
		
		// process i-th line
		// use Gaussian elimination to transfer to a down triangle matrix
		for (int i = 0; i < dim; ++i) {
			
			cur_val = src[i].get(i);
			// if cur_val == 0 find the first != 0 if can't find return 0
			if (cur_val == 0) {
				int row_not0 = -1;
				for (int k = i + 1; k < dim; ++k) {
					if (src[k].get(i) != 0) {
						cur_val = src[k].get(i);
						
						Vector tmp = src[i];
						src[i] = src[k];
						src[k] = tmp;
						
						double ctmp = cst[i];
						cst[i] = cst[k];
						cst[k] = ctmp;
						
						row_not0 = k;
						break;
					}
				}
				if (row_not0 == -1) return null;
			}
			
			// Gaussian elimination
			for (int j = i + 1; j < dim; ++j) {
				if (i == j) continue;
				tmp_val = src[j].get(i);
				if (tmp_val == 0) continue;
				src[j] = src[j].add(src[i].mult(-tmp_val / cur_val)); // eliminate
				cst[j] -= cst[i] * (tmp_val / cur_val);
			}
		}
		
		// Calculate value
		for (int i = dim - 1; i >= 0; --i) {
			cst[i] *= 1 / src[i].get(i);
			for (int j = 0; j < i; ++j) {
				cst[j] -= cst[i] * src[j].get(i);
			}
		}
		return new Vector(cst);
		
	}
	
	/**
	 * Solve a linear equation using Gaussian elimination method.
	 * @param M: (n, n+1)-like matrix, the coefficient-constant matrix of a linear equation.
	 * @return Vector, the solution of the equation.
	 *   * null will be returned if inputs are problematic.
	 *   * null will be returned if the equation consists of zero or multiple solutions.
	 */
	public static Vector solveLineEq(Matrix M) {
		Pair shape = M.getShape();
		if (shape.getY() - shape.getX() != 1) return null;
		if (shape.getX() == 0) return null;
		return solveLineEq(M.get(0, 0, shape.getX(), shape.getX()), M.getColumn(shape.getX()));
	}
	
	/**
	 * QR Decomposition
	 * @param Matrix M
	 * @return Matrix[Q, R]
	 */
	public static Matrix[] qrDecomp(Matrix M) {
		if (!M.isSquare()) return null;
		
		int dim = M.getShape().getX();
		Vector[] mtmp = M.T().toVecArray();
		Vector[] qtmp = new Vector[dim];
		double[][] rtmp = new double[dim][dim];
        Vector vtmp;
        
        for (int k = 0; k < dim; ++k) {
        	vtmp = mtmp[k];
            for (int j = 0; j < k; ++j) {
            	if (qtmp[j] == null) qtmp[j] = new Vector(dim);
                double t = mtmp[k].dot(qtmp[j]);
                rtmp[j][k] = 0;
                rtmp[k][j] = t;
                vtmp = vtmp.add(qtmp[j].mult(-t));
            }
            rtmp[k][k] = vtmp.getLength();
            qtmp[k] = vtmp.mult(1 / rtmp[k][k]);
        }
        
        Matrix Q = new Matrix(qtmp).T();
        Matrix R = new Matrix(rtmp).T();
        Matrix[] ans = {Q, R};
        return ans;
    }
	
	@Deprecated
	private static Matrix[] qrDecompOrig(Matrix M) {
		if (!M.isSquare()) return null;
		
		int dim = M.getShape().getX();
		double[][] mtmp = M.to2DArray();
		double[][] qtmp = new double[dim][dim];
		double[][] rtmp = new double[dim][dim];
		
        double[] vtmp = new double[dim];

        for (int k = 0; k < dim; ++k) {
            for (int i = 0; i < dim; ++i)
                vtmp[i] = mtmp[i][k];

            for (int j = 0; j < k; ++j) {
                double t = 0.0;
                for (int i = 0; i < dim; ++i)
                    t += mtmp[i][k] * qtmp[i][j];
                rtmp[j][k] = t;
                rtmp[k][j] = 0.0;
                for (int i = 0; i < dim; ++i)
                    vtmp[i] -= t * qtmp[i][j];
            }

            double s = 0.0;
            for (int i = 0; i < dim; ++i)
                s += vtmp[i] * vtmp[i];
            rtmp[k][k] = Math.sqrt(s);
            for (int i = 0; i < dim; ++i)
                qtmp[i][k] = vtmp[i] / rtmp[k][k];
        }
        
        Matrix Q = new Matrix(qtmp);
        Matrix R = new Matrix(rtmp);
        //System.out.println(Q);
        //System.out.println(R);
        //System.out.println(Q.mult(R));
        //System.out.println(R.mult(Q));
        Matrix[] m = {Q, R};
        return m;
    }
	
	public static Vector eigen(Matrix M) {return eigen(M, 128);}
	public static Vector eigen(Matrix M, int iter) {
		if (!M.isSquare()) return null;
		Matrix m;
		double e = 1;
		while (iter-- > 0) {
			//System.out.println(M.getDiag());
			Matrix[] qr = qrDecomp(M);
			m = qr[1].mult(qr[0]);
			//System.out.println(M.getDiag().subtract(m.getDiag()));
			e = (M.getDiag().subtract(m.getDiag())).getMahattanDis();
			//System.out.println(String.format("%d, %f", iter, e));
			if (e < 1e-15) {
				M = m; break;
			}
			M = m;
		}
		return M.getDiag();
	}
	
	public static Matrix getEigenVectors(Matrix M, Vector eigen) {
		if (!M.isSquare()) return null;
		if (M.getShape().getX() != eigen.getDimension()) return null;
		if (M.getShape().getX() == 0) return null;
		
		int dim = eigen.getDimension();
		Matrix ismpl = Matrix.identity(dim);
		double[] eigens = eigen.toArray();
		// Arrays.parallelSort(eigens);
		Vector[] ans = new Vector[dim];
		
		for (int i = 0; i < dim; ++i) {
			Matrix tmp = ismpl.mult(-eigens[i]).add(M);
			// System.out.println("tmp");
			// System.out.println(tmp);
			// System.out.println(solveHomoEq(tmp));
			ans[i] = solveHomoEq(tmp);
		}
		
		return new Matrix(ans);
	}
	
	/**
	 * Singular value decomposition of a Matrix.
	 * SVD: Find 3 Matrices U, E, V satisfying M=U@E@V.T().
	 * @param M the Matrix to be decomposed.
	 * @return 3 Matrices U, E, V.
	 */
	public static Matrix[] SVD(Matrix M) {
		if (M.getShape().getX() < M.getShape().getY()) M = M.T();
		
		Matrix u = null, e = null, v = null;
		
		Matrix mmt = M.mult(M.T()); // x*x
		Matrix mtm = M.T().mult(M); // y*y
		Vector emmt = eigen(mmt); // x
		Vector emtm = eigen(mtm); // y
		Matrix memmt = getEigenVectors(mmt, emmt);
		Matrix memtm = getEigenVectors(mtm, emtm);
		
		boolean e_rev_flag = false;
		if (solveDeterminant(memmt) < 0) {
			Vector[] vt = memmt.toVecArray();
			vt[0] = vt[0].mult(-1);
			memmt = new Matrix(vt);
			e_rev_flag = !e_rev_flag;
		}
		if (solveDeterminant(memtm) < 0) {
			Vector[] vt = memtm.toVecArray();
			vt[0] = vt[0].mult(-1);
			memtm = new Matrix(vt);
			e_rev_flag = !e_rev_flag;
		}
		if (e_rev_flag) {
			double[] et = emtm.toArray();
			et[0] = -et[0];
			emtm = new Vector(et);
		}
		
		u = memmt.T();
		e = Matrix.identityExtend(emtm).power(0.5);
		v = memtm;
		
		Matrix[] ans = {u, e, v};
		return ans;
	}
	
	/**
	 * Solve a Tridiagnoal Matrix Equation.
	 * Tridiagnoal matrices are square matrices which only consist of elements on their diagonal and two adjacent lines.
	 * e. g. a 6*6 Tridiagonal Matrix looks like this:
	 * [[b0, c0,  0,  0,  0,  0]
	 *  [a1, b1, c1,  0,  0,  0] 
	 *  [ 0, a2, b2, c2,  0,  0] 
	 *  [ 0,  0, a3, b3, c3,  0] 
	 *  [ 0,  0,  0, a4, b4, c4] 
	 *  [ 0,  0,  0,  0, a5, b5]]
	 * The equation looks like TX=Y, where T is the Tridiagonal Matrix(N*N).
	 * @param a_ Vector(N-1) of [a1, a2, ...., a[n-1]].
	 * @param b_ Vector(N) of [b0, b1, ...., b[n-1]].
	 * @param c_ Vector(N-1) of [c0, c1, ...., c[n-2]].
	 * @param Y Vector(N) of the Y in the equation above.
	 * @return Vector(N) of the X in the equation above.
	 */
	public static Vector solveTridiagnoal(Vector a_, Vector b_, Vector c_, Vector Y) {
		int n = b_.getDimension();
		if (a_.getDimension() != n - 1) {
			try {
				throw new LengthMismatchException(n - 1, a_.getDimension());
			} catch (LengthMismatchException e) {
				e.printStackTrace();
				return null;
			}
		} else if (c_.getDimension() != n - 1) {
			try {
				throw new LengthMismatchException(n - 1, c_.getDimension());
			} catch (LengthMismatchException e) {
				e.printStackTrace();
				return null;
			}
		} else if (Y.getDimension() != n) {
			try {
				throw new LengthMismatchException(n - 1, Y.getDimension());
			} catch (LengthMismatchException e) {
				e.printStackTrace();
				return null;
			}
		}
		a_ = a_.insert(0, 0);
		double[] a = a_.toArray();
		double[] b = b_.toArray();
		double[] c = c_.toArray();
		double[] y = Y.toArray();
		double[] r = new double[n - 1];
		double[] p = new double[n];
		r[0] = c_.get(0) / b_.get(0);
		p[0] = Y.get(0) / b_.get(0);
		for (int i = 1; i < n; ++i) {
			if (i != n - 1) 
				r[i] = c[i] / (b[i] - a[i] * r[i-1]);
			p[i] = (y[i] - a[i] * p[i-1]) / (b[i] - a[i] * r[i-1]);
		}
		double[] x = new double[n];
		x[n-1] = p[n-1];
		for (int i = n - 2; i > -1; --i) {
			x[i] = p[i] - r[i] * x[i+1];
		}
		return new Vector(x);
	}

}

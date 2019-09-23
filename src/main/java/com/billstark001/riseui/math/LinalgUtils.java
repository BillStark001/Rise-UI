package com.billstark001.riseui.math;

import java.util.Arrays;

public class LinalgUtils {

	/*
	 * Solve the Determinant.
	 * Parameters:
	 *   M: Matrix
	 * Returns:
	 *   double, the solution of the determinant.
	 *   * M must be square, otherwise return 1137833.
	 */
	public static double solveDeterminant(Matrix M) {
		if (!M.isSquare()) return 1137833;
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
	
	/*
	 * Solve a homogeneous equation.
	 * Parameters:
	 *   M: Matrix
	 * Returns:
	 *   Vector, a normalized linear-independent solution of the equation.
	 *   * M must be square, otherwise return null.
	 */
	public static Vector solveHomoEq(Matrix M) {
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
		
		//for (Vector v: src) System.out.println(v);System.out.println();
		
		// Calculate value
		double srci;
		for (int i = dim - 1; i >= 0; --i) {
			srci = src[i].get(i);
			if (Math.abs(srci) < 1e-13) cst[i] = srci = 1;
			else cst[i] *= 1 / srci;
			for (int j = 0; j < i; ++j) {
				cst[j] -= cst[i] * src[j].get(i);
			}
		}
		
		//System.out.println(new Vector(cst));
		
		return new Vector(cst).normalize();
		
	}
	
	/*
	 * Solve a linear equation using Gaussian elimination method.
	 * Parameters:
	 *   A: (n, n)-like matrix, the coefficient matrix of a linear equation.
	 *   b: (n)-like vector, the constant series of the same equation.
	 *   * each dimension's length must be the same. 
	 * Returns:
	 *   Vector, the solution of the equation.
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
	
	/*
	 * Solve a linear equation using Gaussian elimination method.
	 * Parameters:
	 *   M: (n, n+1)-like matrix, the coefficient-constant matrix of a linear equation.
	 * Returns:
	 *   Vector, the solution of the equation.
	 *   * null will be returned if inputs are problematic.
	 *   * null will be returned if the equation consists of zero or multiple solutions.
	 */
	public static Vector solveLineEq(Matrix M) {
		Pair shape = M.getShape();
		if (shape.getY() - shape.getX() != 1) return null;
		if (shape.getX() == 0) return null;

		int dim = M.getShape().getX();
		Vector[] src = M.toVecArray();
		
		double cur_val, tmp_val;
		
		// process i-th line
		// use Gaussian elimination to transfer to a down triangle matrix
		for (int i = 0; i < dim; ++i) {
			
			
			//for (Vector v: src) System.out.println(v);System.out.println();
			
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
				if (row_not0 == -1) return null;
			}
			
			//for (Vector v: src) System.out.println(v);System.out.println();
			
			// Gaussian elimination
			for (int j = i + 1; j < dim; ++j) {
				if (i == j) continue;
				tmp_val = src[j].get(i);
				if (tmp_val == 0) continue;
				src[j] = src[j].add(src[i].mult(-tmp_val / cur_val)); // eliminate
			}
			
		}
		
		
		// Calculate value
		double[] cst = new double[dim];
		for (int i = 0; i < dim; ++i) cst[i] = src[i].get(dim);
		for (int i = dim - 1; i >= 0; --i) {
			cst[i] *= 1 / src[i].get(i);
			for (int j = 0; j < i; ++j) {
				cst[j] -= cst[i] * src[j].get(i);
			}
		}
		return new Vector(cst);
		
	}
	
	/*
	 * QR Decomposition
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
	
	public static Matrix eigenMatrix(Matrix M, Vector eigen) {
		if (!M.isSquare()) return null;
		if (M.getShape().getX() != eigen.getDimension()) return null;
		if (M.getShape().getX() == 0) return null;
		
		int dim = eigen.getDimension();
		Matrix ismpl = Matrix.unit(dim);
		double[] eigens = eigen.toArray();
		Arrays.parallelSort(eigens);
		Vector[] ans = new Vector[dim];
		
		for (int i = 0; i < dim; ++i) {
			Matrix tmp = ismpl.multScalar(-eigens[i]).add(M);
			//System.out.println(tmp);
			//System.out.println(solveHomoEq(tmp));
			ans[i] = solveHomoEq(tmp);
		}
		
		return new Matrix(ans);
	}
	
	public static Matrix[] SVD(Matrix M) {
		Matrix u = null, e = null, v = null;
		Matrix mmt = M.mult(M.T());
		Matrix mtm = M.T().mult(M);
		Vector emmt = eigen(mmt);
		Vector emtm = eigen(mtm);
		Matrix memmt = eigenMatrix(mmt, emmt);
		Matrix memtm = eigenMatrix(mtm, emtm);
		/*
		System.out.println(mmt);
		System.out.println(mtm);
		System.out.println(emmt);
		System.out.println(emtm);
		System.out.println(memmt);
		System.out.println(memtm);
		*/
		//double[][] _e = new double[M.getShape().getX()][M.getShape().getY()];
		double[] v_ = new double[emtm.getDimension()];
		for (int i = 0; i < v_.length; ++i) v_[i] = Math.sqrt(emtm.get(i));
		u = memmt.T();
		e = new Matrix(new Vector(v_));
		v = memtm.T();
		
		Matrix[] ans = {u, e, v};
		return ans;
	}

}

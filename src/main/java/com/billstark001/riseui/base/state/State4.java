package com.billstark001.riseui.base.state;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Pair;
import com.billstark001.riseui.math.ShapeMismatchException;

import scala.actors.threadpool.Arrays;

public class State4 {

	private Matrix statemat;
	private String name;
	
	protected State4(Matrix mat) {
		try {
			changeState(mat);
		} catch (ShapeMismatchException e) {
			e.printStackTrace();
			resetState();
		}
	resetName();}
	protected State4() {resetState();resetName();}
	
	protected void changeState(Matrix mat) throws ShapeMismatchException {
		if (mat == null) throw ShapeMismatchException.Expect(new Pair(4, 4), mat);
		if (mat.getShape().equals(new Pair(3, 3)) || mat.getShape().equals(new Pair(2, 2))) {
			mat = Matrix.homoExtend(mat, 4);
			statemat = mat;
		} else if (mat.getShape().equals(new Pair(4, 4))) {
			statemat = mat;
		} else {
			throw ShapeMismatchException.Expect(new Pair(4, 4), mat);
		}
	}
	protected void resetState() {statemat = Matrix.I4;}
	
	public Matrix getState() {if (statemat == null) return Matrix.I4; else return statemat;}
	
	public void setName(String name) {this.name = name;}
	public String getName() {return name;}
	public void resetName() {setName(this.getClass().getSimpleName());}
	
	/*
	 * Compose 2 states A and B to a new state C.
	 * States must be Matrices(4, 4) representing 3D spaces homogeneously.
	 * C = A @ B (@: Multiplication of matrices)
	 * Parameters:
	 *   A: Matrix(4, 4)
	 *   B: Matrix(4, 4)
	 * Returns:
	 *   C: Matrix(4, 4)
	 */
	public static Matrix stateCompose(Matrix A, Matrix B) {
		return A.mult(B);
	}
	public static SimpleState stateCompose(State4 A, State4 B) {
		return new SimpleState(stateCompose(A.getState(), B.getState()));
	}
	
	/*
	 * Decompose state A from state C.
	 * States must be Matrices(4, 4) representing 3D spaces homogeneously.
	 * C = A @ B (@: Multiplication of matrices)
	 * Hence C @ B^(-1) = A
	 * Parameters:
	 *   C: Matrix(4, 4)
	 *   B: Matrix(4, 4)
	 * Returns:
	 *   A: Matrix(4, 4)
	 */
	public static Matrix stateDecomposeA(Matrix C, Matrix B) {
		return C.mult(Matrix.inverse(B));
	}
	public static SimpleState stateDecomposeA(State4 C, State4 B) {
		return new SimpleState(stateDecomposeA(C.getState(), B.getState()));
	}
	
	/*
	 * Decompose state B from state C.
	 * States must be Matrices(4, 4) representing 3D spaces homogeneously.
	 * C = A @ B (@: Multiplication of matrices)
	 * Hence A^(-1) @ C = B
	 * Parameters:
	 *   C: Matrix(4, 4)
	 *   A: Matrix(4, 4)
	 * Returns:
	 *   B: Matrix(4, 4)
	 */
	public static Matrix stateDecomposeB(Matrix C, Matrix A) {
		return Matrix.inverse(A).mult(C);
	}
	public static SimpleState stateDecomposeB(State4 C, State4 A) {
		return new SimpleState(stateDecomposeB(C.getState(), A.getState()));
	}
	
	@Override
	public String toString() {
		Matrix state = this.getState();
		return Arrays.deepToString(state.toVecArray());
		//String str_tmp = "ORIGIN: %s, X: %s, Y: %s, Z: %s";
		//return String.format(str_tmp, state.getLine(3).get(0, 3).toString(), state.getLine(0).get(0, 3).toString(), state.getLine(1).get(0, 3).toString(), state.getLine(2).get(0, 3).toString());
	}

}

package com.billstark001.riseui.base.nodestate;

import java.nio.FloatBuffer;

import com.billstark001.riseui.base.fields.OprConstSimple;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Pair;
import com.billstark001.riseui.computation.ShapeMismatchException;
import com.billstark001.riseui.computation.UtilsLinalg;

import scala.actors.threadpool.Arrays;

public abstract class State3DBase extends OprConstSimple<Matrix> {
	
	private boolean dirty = true;
	protected void markClean() {this.dirty = false;}
	public void markDirty() {
		this.dirty = true;
		this.state_cache = null;
		this.buffer_cache = null;
		this.det_cache = null;
	}
	public boolean isDirty() {return this.dirty;}
	
	private Matrix state_cache;
	private FloatBuffer buffer_cache;
	private Double det_cache = null;
	
	protected State3DBase(Matrix mat) {
		this.set(mat);
	}
	
	public State3DBase() {super(DEFAULT);}

	@Override
	protected boolean set(Matrix mat) {
		try {
			super.set(checkState(mat));
		} catch (ShapeMismatchException e) {
			e.printStackTrace();
			return false;
		}
		markDirty();
		return true;
	}
	
	public abstract Matrix calcState();
	
	@Override
	public Matrix get() {
		if (this.isDirty() || state_cache == null) {
			state_cache = calcState();
			this.markClean();
		}
		return state_cache;
	}
	
	public Matrix getRaw() {
		return super.get();
	}
	
	public FloatBuffer getBuffered() {
		if (this.isDirty() || buffer_cache == null) {
			buffer_cache = this.get().storeBufferF().asReadOnlyBuffer();
		}
		return buffer_cache;
	}
	
	public double getDeterminant() {
		if (this.isDirty() || this.det_cache == null) {
			det_cache = UtilsLinalg.solveDeterminant(this.get());
		}
		return det_cache;
	}
	
	public FloatBuffer getBufferedRaw() {
		return this.get().storeBufferF().asReadOnlyBuffer();
	}
	
	public double getDeterminantRaw() {
		return UtilsLinalg.solveDeterminant(this.get());
	}

	protected static Matrix checkState(Matrix mat) throws ShapeMismatchException {
		Matrix statemat = DEFAULT;
		if (mat == null) throw ShapeMismatchException.Expect(new Pair(4, 4), mat);
		if (mat.getShape().equals(new Pair(3, 3)) || mat.getShape().equals(new Pair(2, 2)) || mat.getShape().equals(new Pair(1, 1))) {
			mat = Matrix.homoExtend(mat, 4);
			statemat = mat;
		} else if (mat.getShape().equals(new Pair(4, 4))) {
			statemat = mat;
		} else {
			throw ShapeMismatchException.Expect(new Pair(4, 4), mat);
		}
		return statemat;
	}
	protected void resetState() {this.set(DEFAULT);}
	
	public static final Matrix DEFAULT = Matrix.I4;
	@Override
	public Matrix getDefault() {return DEFAULT;}
	
	// Static Methods
	
	/**
	 * Compose 2 states A and B to a new state C.
	 * States must be Matrices(4, 4) representing 3D spaces homogeneously.
	 * C = A @ B (@: Multiplication of matrices)
	 * Parameters:
	 * @param A Matrix(4, 4)
	 * @param B Matrix(4, 4)
	 * @return C Matrix(4, 4)
	 */
	public static Matrix stateCompose(Matrix A, Matrix B) {
		return A.mult(B);
	}
	public static State3DSimple stateCompose(State3DBase A, State3DBase B) {
		return new State3DSimple(stateCompose(A.get(), B.get()));
	}
	
	/**
	 * Decompose state A from state C.
	 * States must be Matrices(4, 4) representing 3D spaces homogeneously.
	 * C = A @ B (@: Multiplication of matrices)
	 * Hence C @ B^(-1) = A
	 * @param C Matrix(4, 4)
	 * @param B Matrix(4, 4)
	 * @return A Matrix(4, 4)
	 */
	public static Matrix stateDecomposeA(Matrix C, Matrix B) {
		return C.mult(Matrix.inverse(B));
	}
	public static State3DSimple stateDecomposeA(State3DBase C, State3DBase B) {
		return new State3DSimple(stateDecomposeA(C.get(), B.get()));
	}
	
	/**
	 * Decompose state B from state C.
	 * States must be Matrices(4, 4) representing 3D spaces homogeneously.
	 * C = A @ B (@: Multiplication of matrices)
	 * Hence A^(-1) @ C = B
	 * @param C Matrix(4, 4)
	 * @param A Matrix(4, 4)
	 * @return B Matrix(4, 4)
	 */
	public static Matrix stateDecomposeB(Matrix C, Matrix A) {
		return Matrix.inverse(A).mult(C);
	}
	public static State3DSimple stateDecomposeB(State3DBase C, State3DBase A) {
		return new State3DSimple(stateDecomposeB(C.get(), A.get()));
	}
	
	public String toString() {
		return String.format("%s<%s>: %s", this.getClass().getSimpleName(), this.getDataType().getSimpleName(), Arrays.deepToString(this.get().toVecArray()));
		//String str_tmp = "ORIGIN: %s, X: %s, Y: %s, Z: %s";
		//return String.format(str_tmp, state.getLine(3).get(0, 3).toString(), state.getLine(0).get(0, 3).toString(), state.getLine(1).get(0, 3).toString(), state.getLine(2).get(0, 3).toString());
	}

}

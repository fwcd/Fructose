package com.fwcd.fructose.quantum.gates.unary;

import com.fwcd.fructose.math.Complex;
import com.fwcd.fructose.math.ComplexMatrix;
import com.fwcd.fructose.quantum.gates.MatrixGate;

/**
 * Inverts around the hypercomplex y-axis.
 * 
 * @author fredrik
 *
 */
public class PauliYGate extends MatrixGate {
	@Override
	protected ComplexMatrix getMatrix() {
		return new ComplexMatrix(new Complex[][] {
				{Complex.ZERO, Complex.I.invertImag()},
				{Complex.I, Complex.ZERO}
		});
	}

	@Override
	public String getSymbol() {
		return "Y";
	}
}
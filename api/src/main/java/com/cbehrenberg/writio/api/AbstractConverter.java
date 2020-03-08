package com.cbehrenberg.writio.api;

import java.util.function.Function;

public abstract class AbstractConverter<S, D> {

	private Function<S, D> srcToDstFunc;

	private Function<D, S> dstToSrcFunc;

	public AbstractConverter(Function<S, D> srcToDstFunc, Function<D, S> dstToSrcFunc) {
		this.srcToDstFunc = srcToDstFunc;
		this.dstToSrcFunc = dstToSrcFunc;
	}

	public D convertFromSrc(S src) {
		return srcToDstFunc.apply(src);
	}

	public S convertFromDst(D dst) {
		return dstToSrcFunc.apply(dst);
	}
}

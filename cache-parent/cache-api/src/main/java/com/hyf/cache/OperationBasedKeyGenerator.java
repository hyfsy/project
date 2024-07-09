package com.hyf.cache;

/**
 * 基于{@link BaseCacheOperation}的key生成器
 * 
 * @author baB_hyf
 * @date 2022/02/08
 */
public abstract class OperationBasedKeyGenerator<O extends BaseCacheOperation> implements KeyGenerator
{

    protected final O operation;

    public OperationBasedKeyGenerator(O operation) {
        this.operation = operation;
    }

}

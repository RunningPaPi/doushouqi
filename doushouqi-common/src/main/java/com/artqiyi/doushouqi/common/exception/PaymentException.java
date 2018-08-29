package com.artqiyi.doushouqi.common.exception;

/** 
 * 支付类异常
 *
 * @author wushuang
 * @since 2018-05-12
 */
public class PaymentException extends RuntimeException {
	
    public PaymentException(String msg) {
        super(msg);
    }

    public PaymentException(String msg, Throwable cause) {
        super(msg, cause);
    }


}

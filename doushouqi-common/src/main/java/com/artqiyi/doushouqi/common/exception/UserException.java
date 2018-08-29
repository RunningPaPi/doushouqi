
package com.artqiyi.doushouqi.common.exception;

/** 
 * 类或接口作用描述 
 *
 * @author wushuang
 * @since 2018-05-21
 */
public class UserException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserException(String msg) {
        super(msg);
    }

    public UserException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

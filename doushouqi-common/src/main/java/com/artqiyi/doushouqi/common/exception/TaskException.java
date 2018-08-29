
package com.artqiyi.doushouqi.common.exception;

/** 
 * 类或接口作用描述 
 *
 * @author wushuang
 * @since 2018-05-21
 */
public class TaskException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaskException(String msg) {
        super(msg);
    }

    public TaskException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

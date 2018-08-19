/**
 * 
 */
package com.ddf.springmvc.annotation.configuration.exception;

/**
 * 定义异常消息的代码，get方法返回实际值，这个值需要在exception.properties、exception_zh_CN.properties、
 * exception_en_US中配置，请根据实际情况在对应的Locale资源文件中配置，至少配置exception.properties
 * @author DDF 2017年12月1日
 *
 */
public enum GlobalExceptionEnum implements GlobalExceptionCodeResolver {
	SYS_ERROR("SYS_ERROR"),
	PLACEHOLDER_DEMO("PLACEHOLDER_DEMO"),
	USER_EXIST("USER_EXIST")

	;

	private String code;

	GlobalExceptionEnum (String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
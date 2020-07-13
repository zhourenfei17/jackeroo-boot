package cn.hub.jackeroo.vo;

import cn.hub.jackeroo.enums.ResultStatusCode;

/**
 * 移动端api接口返回的数据模型
 * 
 * @author
 */
public class Result<T> {

	private int code; // 返回的代码，0表示成功，其他表示失败

	private String msg; // 成功或失败时返回的错误信息

	private T data; // 成功时返回的数据信息

	public Result(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Result(ResultStatusCode resultStatusCode, T data) {
		this(resultStatusCode.getCode(), resultStatusCode.getMsg(), data);
	}

	public Result(int code, String msg) {
		this(code, msg, null);
	}

	public Result(ResultStatusCode resultStatusCode) {
		this(resultStatusCode, null);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}

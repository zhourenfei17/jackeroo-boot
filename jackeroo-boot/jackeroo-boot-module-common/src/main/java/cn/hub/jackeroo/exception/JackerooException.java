package cn.hub.jackeroo.exception;

import cn.hub.jackeroo.enums.ResultStatusCode;
import lombok.Getter;

@Getter
public class JackerooException extends RuntimeException {

    private ResultStatusCode statusCode;

	public JackerooException(String message) {
		super(message);
	}

	public JackerooException(ResultStatusCode statusCode){
        super(statusCode.getMsg());
        this.statusCode = statusCode;
    }
}

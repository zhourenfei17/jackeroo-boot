package cn.hub.jackeroo.persistence;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.vo.Result;

public abstract class BaseController {

    protected Result ok(){
        return new Result(ResultStatusCode.OK);
    }

    protected Result ok(Object object){
        return new Result(ResultStatusCode.OK, object);
    }

    protected Result result(ResultStatusCode code, Object object){
        return new Result(code, object);
    }

    protected Result result(ResultStatusCode code){
        return new Result(code);
    }

    protected Result error(String msg){
        return new Result(Constant.SYSTEM_ERROR_CODE, msg);
    }
}

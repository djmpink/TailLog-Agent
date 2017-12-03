package cn.teamstack.logger.model;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zhouli
 * @date 2017/8/18
 */
public class QueryCmd {

    /**
     * 自定义参数
     * Json格式
     * TODO 预留
     */
    public JSONObject params;

    /**
     * 命令
     */
    public String content;

    /**
     * ssh配置信息（选填）
     */
    public SSH ssh;
}

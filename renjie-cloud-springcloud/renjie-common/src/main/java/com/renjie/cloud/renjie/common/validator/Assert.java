package com.renjie.cloud.renjie.common.validator;

import com.renjie.cloud.renjie.common.exception.RRException;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @Author oyg
 * @Date 2018/8/5/18:00
 */
public abstract class Assert {

    public static void isBlank(Object o, String message){
        if (o == null){
            throw new RRException(message);
        }

        if (o instanceof String){
            if (StringUtils.isBlank((String)o)){
                throw new RRException(message);
            }
        }
        if (o instanceof List){
            List list = (List) o;
            if (list.size() == 0){
                throw new RRException(message);
            }
        }

    }
}

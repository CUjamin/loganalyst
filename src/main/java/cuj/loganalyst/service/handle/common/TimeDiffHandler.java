package cuj.loganalyst.service.handle.common;

import cuj.loganalyst.common.ParamKey;
import cuj.loganalyst.util.LogUtils;
import cuj.loganalyst.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cujamin
 * @Date: 2019/3/4 11:52
 * @Description:
 */
public class TimeDiffHandler implements Handler{
    private final static Logger log = LoggerFactory.getLogger(TimeDiffHandler.class);
    private final static String spitKey1 = " INFO";

    public List<String> handle(List<List<String>> dataLists, Map<String, Object> params){
        List<String> resultDataList = new LinkedList<>();
        if(dataLists.size()>1){
            log.info("dataLists > 1 end ");
        }else {
            log.info(" [start analysis ... ] ");
            List<String> dataList = dataLists.remove(0);
            String pattern = LogUtils.getPatternOrder(ParamKey.CONTAIN_WORD);
            log.info("pattern:"+pattern);
            if(null!=dataList&&dataList.size()>0)
            {
                boolean isFirst = true;
                String tempA="";
                String tempB="";
                for(String tempStr:dataList)
                {
                    if(LogUtils.containWordOrder(tempStr, pattern))
                    {
                        if(isFirst){
                            tempB = tempStr;
                            isFirst = false;
                            continue;
                        }else {
                            tempA = tempB;
                            tempB = tempStr;
                        }
                        resultDataList.add(handleWord(tempA, tempB));
                    }
                }
                log.info(" [ handle data number: "+resultDataList.size()+" ] ");
            }
            log.info(" [start analysis end ] ");
        }
        return resultDataList;
    }


    private String handleWord(String tempStrA, String tempStrB) {
        String timeStrA = getTimeStr(tempStrA);
        String timeStrB = getTimeStr(tempStrB);
        long timeDif = 0L;
        try{
            timeDif = TimeUtil.difTime(timeStrA , timeStrB);
        }catch (ParseException pe){
            log.error("解析时间串发生异常",pe);
        }
        String strA = timeStrA.replace(",",".");
        String strB = timeStrB.replace(",",".");
        return String.format("%s,%s,%d", strA, strB, timeDif);
    }

    private String getTimeStr(String tempStr)
    {
        String[] tempStrArray = tempStr.split(spitKey1);
        return tempStrArray[0];
    }
}

import com.sss.common.mapper.GlobalSetMapper;
import com.sss.rms.SssRmsServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest(classes = SssRmsServiceApplication.class)
public class GlobalSetTest {

    @Resource
    private GlobalSetMapper setMapper;

    @Test
    void insert(){
        try{
            setMapper.insert("test");
            setMapper.insert("test");
        } catch (DuplicateKeyException e){
            log.warn("has");
        }

    }
}

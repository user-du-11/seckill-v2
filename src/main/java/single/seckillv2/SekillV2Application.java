package single.seckillv2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("single.seckillv2.Mapper")
public class SekillV2Application {

    public static void main(String[] args) {
        SpringApplication.run(SekillV2Application.class, args);
    }

}

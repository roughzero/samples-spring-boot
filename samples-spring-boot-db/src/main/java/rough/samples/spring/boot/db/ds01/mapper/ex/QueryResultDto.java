package rough.samples.spring.boot.db.ds01.mapper.ex;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class QueryResultDto {
    private BigDecimal cnt;
    private Date currentTime;
}

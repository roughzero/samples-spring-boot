package rough.samples.spring.boot.db.ds01.mapper;


import rough.samples.spring.boot.db.ds01.model.TestBlob;

import java.math.BigDecimal;
import java.util.List;

public interface TestBlobMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TEST_BLOB
     *
     * @mbg.generated Tue Aug 11 10:28:24 CST 2020
     */
    int deleteByPrimaryKey(BigDecimal testBlobId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TEST_BLOB
     *
     * @mbg.generated Tue Aug 11 10:28:24 CST 2020
     */
    int insert(TestBlob record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TEST_BLOB
     *
     * @mbg.generated Tue Aug 11 10:28:24 CST 2020
     */
    TestBlob selectByPrimaryKey(BigDecimal testBlobId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TEST_BLOB
     *
     * @mbg.generated Tue Aug 11 10:28:24 CST 2020
     */
    List<TestBlob> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TEST_BLOB
     *
     * @mbg.generated Tue Aug 11 10:28:24 CST 2020
     */
    int updateByPrimaryKey(TestBlob record);
}
package rough.samples.spring.boot.db.ds01.model;

import java.math.BigDecimal;

public class TestBlob {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TEST_BLOB.TEST_BLOB_ID
     *
     * @mbg.generated Tue Aug 11 10:28:24 CST 2020
     */
    private BigDecimal testBlobId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TEST_BLOB.COL_BLOB
     *
     * @mbg.generated Tue Aug 11 10:28:24 CST 2020
     */
    private byte[] colBlob;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TEST_BLOB.TEST_BLOB_ID
     *
     * @return the value of TEST_BLOB.TEST_BLOB_ID
     *
     * @mbg.generated Tue Aug 11 10:28:24 CST 2020
     */
    public BigDecimal getTestBlobId() {
        return testBlobId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TEST_BLOB.TEST_BLOB_ID
     *
     * @param testBlobId the value for TEST_BLOB.TEST_BLOB_ID
     *
     * @mbg.generated Tue Aug 11 10:28:24 CST 2020
     */
    public void setTestBlobId(BigDecimal testBlobId) {
        this.testBlobId = testBlobId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TEST_BLOB.COL_BLOB
     *
     * @return the value of TEST_BLOB.COL_BLOB
     *
     * @mbg.generated Tue Aug 11 10:28:24 CST 2020
     */
    public byte[] getColBlob() {
        return colBlob;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TEST_BLOB.COL_BLOB
     *
     * @param colBlob the value for TEST_BLOB.COL_BLOB
     *
     * @mbg.generated Tue Aug 11 10:28:24 CST 2020
     */
    public void setColBlob(byte[] colBlob) {
        this.colBlob = colBlob;
    }
}
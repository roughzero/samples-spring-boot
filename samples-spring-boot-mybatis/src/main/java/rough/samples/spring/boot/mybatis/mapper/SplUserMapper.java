package rough.samples.spring.boot.mybatis.mapper;

import java.util.List;
import rough.samples.spring.boot.mybatis.model.SplUser;

public interface SplUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SPL_USER
     *
     * @mbg.generated Tue Jan 21 19:48:16 CST 2020
     */
    int deleteByPrimaryKey(String userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SPL_USER
     *
     * @mbg.generated Tue Jan 21 19:48:16 CST 2020
     */
    int insert(SplUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SPL_USER
     *
     * @mbg.generated Tue Jan 21 19:48:16 CST 2020
     */
    SplUser selectByPrimaryKey(String userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SPL_USER
     *
     * @mbg.generated Tue Jan 21 19:48:16 CST 2020
     */
    List<SplUser> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SPL_USER
     *
     * @mbg.generated Tue Jan 21 19:48:16 CST 2020
     */
    int updateByPrimaryKey(SplUser record);
}
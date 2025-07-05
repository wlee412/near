package com.example.demo.mapper;

import com.example.demo.model.MentalHealthItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MentalHealthMapper {
    void insert(MentalHealthItem item);
    
    List<MentalHealthItem> selectAll();

    // 중복 데이터 존재 여부 확인
    boolean exists(@Param("chtTtlNm") String chtTtlNm,
                   @Param("chtSeNm") String chtSeNm,
                   @Param("chtXCn") String chtXCn,
                   @Param("chtYCn") String chtYCn);
    
    List<MentalHealthItem> selectYoungOnly();  // 미취학 

	List<MentalHealthItem> selectOldOnly();  //대학생

	List<Map<String, Object>> selectAvgByAgeGroup();
   

    
}

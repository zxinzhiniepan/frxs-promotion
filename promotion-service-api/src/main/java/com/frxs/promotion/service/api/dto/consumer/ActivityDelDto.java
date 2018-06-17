package com.frxs.promotion.service.api.dto.consumer;

import com.frxs.promotion.service.api.dto.AbstractSuperDto;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @author fygu
 * @version $Id: ActivityDelDto.java,v 0.1 2018年02月08日 10:30 $Exp
 */
@Data
public class ActivityDelDto extends AbstractSuperDto implements Serializable {

  private List<Long> activityIds;

  private Long areaId;

}

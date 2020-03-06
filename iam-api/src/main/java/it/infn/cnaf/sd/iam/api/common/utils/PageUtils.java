package it.infn.cnaf.sd.iam.api.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageUtils {

  private PageUtils() {
    // empty method
  }

  public static PageRequest buildPageRequest(Integer count, Integer startIndex, int maxPageSize,
      Sort sort) {
    int validCount = 0;
    int validStartIndex = 0;

    if (count == null) {
      validCount = maxPageSize;
    } else {
      validCount = count;
      if (count < 0) {
        validCount = 0;
      } else if (count > maxPageSize) {
        validCount = maxPageSize;
      }
    }

    if (startIndex == null) {
      validStartIndex = 0;
    } else {
      validStartIndex = startIndex;
      if (startIndex < 0) {
        validStartIndex = 0;
      }
    }

    return PageRequest.of(validStartIndex, validCount, sort);
  }
}

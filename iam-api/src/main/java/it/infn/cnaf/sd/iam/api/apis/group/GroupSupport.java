package it.infn.cnaf.sd.iam.api.apis.group;

import static java.lang.String.format;

public interface GroupSupport {

  public static final String GROUP_HAS_CHILDREN = "Group has children groups";
  public static final String GROUP_NAME_TOO_LONG_ERROR = "Group name longer than 512 characters";
  public static final String GROUP_NOT_FOUND_ERROR_TEMPLATE = "Group not found: %s";

  public default String groupNotFoundMessage(String uuidOrName) {
    return format(GROUP_NOT_FOUND_ERROR_TEMPLATE, uuidOrName);
  }

}

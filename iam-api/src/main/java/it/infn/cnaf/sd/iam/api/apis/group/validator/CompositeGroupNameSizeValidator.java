package it.infn.cnaf.sd.iam.api.apis.group.validator;

import static java.util.Objects.isNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import it.infn.cnaf.sd.iam.api.apis.group.dto.GroupDTO;

public class CompositeGroupNameSizeValidator
    implements ConstraintValidator<CompositeGroupNameSize, GroupDTO> {


  @Override
  public boolean isValid(GroupDTO value, ConstraintValidatorContext context) {

    if (isNull(value.getParentGroup()) || isNull(value.getParentGroup().getName())) {
      return true;
    }

    return value.getName() != null
        && value.getName().length() + 1 + value.getParentGroup().getName().length() < 512;
  }

}

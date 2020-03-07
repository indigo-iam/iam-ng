package it.infn.cnaf.sd.iam.api.apis.group.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.infn.cnaf.sd.iam.api.apis.group.GroupService;

@Component
@Scope("prototype")
public class GroupNameValidator implements ConstraintValidator<GroupName, String> {

  final GroupService groupService;

  @Autowired
  public GroupNameValidator(GroupService groupService) {
    this.groupService = groupService;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    return value != null && groupService.findByName(value).isPresent();
  }



}

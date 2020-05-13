package it.infn.cnaf.sd.iam.api.apis.requests.dto;

import it.infn.cnaf.sd.iam.api.apis.registrations.dto.RegistrationRequestDTO;

public class RequestOutcomeDTO {

  String message;

  RegistrationRequestDTO request;

  public RequestOutcomeDTO() {}

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public RegistrationRequestDTO getRequest() {
    return request;
  }

  public void setRequest(RegistrationRequestDTO request) {
    this.request = request;
  }

  public static RequestOutcomeDTO newOutcomeDto(String message, RegistrationRequestDTO request) {
    RequestOutcomeDTO dto = new RequestOutcomeDTO();
    dto.setMessage(message);
    dto.setRequest(request);
    return dto;
  }
}

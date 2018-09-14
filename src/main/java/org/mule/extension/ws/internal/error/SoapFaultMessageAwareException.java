package org.mule.extension.ws.internal.error;

import org.mule.runtime.api.exception.ErrorMessageAwareException;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.soap.api.exception.SoapFaultException;

import static org.mule.extension.ws.internal.error.WscError.SOAP_FAULT;

public class SoapFaultMessageAwareException extends ModuleException implements ErrorMessageAwareException {

  private final Message msg;

  public SoapFaultMessageAwareException(SoapFaultException cause) {
    super(cause.getMessage(), SOAP_FAULT, cause);
    this.msg = Message.builder().value(cause).build();
  }

  @Override
  public Message getErrorMessage() {
    return msg;
  }
}

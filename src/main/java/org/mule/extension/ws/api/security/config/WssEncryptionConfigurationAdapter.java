/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security.config;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

import org.mule.extension.ws.api.security.Constants.EncryptionDigestAlgorithmConstants;
import org.mule.extension.ws.api.security.Constants.EncryptionKeyIdentifierConstants;
import org.mule.extension.ws.api.security.Constants.EncryptionKeyTransportAlgorithmConstants;
import org.mule.extension.ws.api.security.Constants.EncryptionSymAlgorithmConstants;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

import java.util.List;
import java.util.Objects;

/**
 * Group which holds the configuration regarding encryption algorithms used on the encryption security strategy.
 *
 * @since 1.3.0
 */
@Alias("wss-encryption-algorithms-configuration")
public class WssEncryptionConfigurationAdapter {

  @Parameter
  @Optional(defaultValue = "ISSUER_SERIAL")
  @Expression(NOT_SUPPORTED)
  @Summary("The key identifier type to use for encryption.")
  private EncryptionKeyIdentifierConstants encryptionKeyIdentifier;

  @Parameter
  @Optional(defaultValue = "AES_128")
  @Expression(NOT_SUPPORTED)
  @Summary("The symmetric encryption algorithm to use.")
  private EncryptionSymAlgorithmConstants encryptionSymAlgorithm;

  @Parameter
  @Optional(defaultValue = "KEYTRANSPORT_RSAOAEP")
  @Expression(NOT_SUPPORTED)
  @Summary("The algorithm to use to encrypt the generated symmetric key.")
  private EncryptionKeyTransportAlgorithmConstants encryptionKeyTransportAlgorithm;

  @Parameter
  @Optional(defaultValue = "SHA1")
  @Expression(NOT_SUPPORTED)
  @Summary("The encryption digest algorithm to use with the key transport algorithm.")
  private EncryptionDigestAlgorithmConstants encryptionDigestAlgorithm;

  @Parameter
  @Optional
  @DisplayName("Parts")
  @Expression(NOT_SUPPORTED)
  @Summary("Lists of parts to be encrypted. If any part is specified the SOAP Body will be encrypted.")
  private List<WssPartAdapter> wssPartAdapters;

  /**
   * @return The Encryption key identifier.
   */
  public EncryptionKeyIdentifierConstants getEncryptionKeyIdentifier() {
    return encryptionKeyIdentifier;
  }

  /**
   * @return The symmetric encryption algorithm.
   */
  public EncryptionSymAlgorithmConstants getEncryptionSymAlgorithm() {
    return encryptionSymAlgorithm;
  }

  /**
   * @return The encryption key transport algorithm.
   */
  public EncryptionKeyTransportAlgorithmConstants getEncryptionKeyTransportAlgorithm() {
    return encryptionKeyTransportAlgorithm;
  }

  /**
   * @return The encryption digest algorithm.
   */
  public EncryptionDigestAlgorithmConstants getEncryptionDigestAlgorithm() {
    return encryptionDigestAlgorithm;
  }

  /**
   * @return The list of Parts to be encrypted.
   */
  public List<WssPartAdapter> getWssPartAdapters() {
    return wssPartAdapters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WssEncryptionConfigurationAdapter that = (WssEncryptionConfigurationAdapter) o;
    return encryptionKeyIdentifier == that.encryptionKeyIdentifier &&
        encryptionSymAlgorithm == that.encryptionSymAlgorithm &&
        encryptionKeyTransportAlgorithm == that.encryptionKeyTransportAlgorithm &&
        encryptionDigestAlgorithm == that.encryptionDigestAlgorithm &&
        Objects.equals(wssPartAdapters, that.wssPartAdapters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(encryptionKeyIdentifier, encryptionSymAlgorithm, encryptionKeyTransportAlgorithm,
                        encryptionDigestAlgorithm, wssPartAdapters);
  }
}

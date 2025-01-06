FROM fbascheper/soapui-mockservice-runner

COPY soapui-prj/* /home/soapui/soapui-prj/
COPY soapui-prj-fips/* /home/soapui/soapui-prj-fips/
COPY security/jks/* /home/soapui/security/
COPY wsdl/* /home/soapui/wsdl/

COPY docker-libs/xmlsec-1.5.8.jar /home/soapui/SoapUI-5.4.0/lib/xmlsec-1.4.5.jar

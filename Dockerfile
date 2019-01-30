FROM fbascheper/soapui-mockservice-runner

COPY src/test/resources/soapui-prj/* /home/soapui/soapui-prj/
COPY src/test/resources/security/* /home/soapui/security/

COPY src/test/resources/xmlsec-1.5.8.jar /home/soapui/SoapUI-5.4.0/lib/xmlsec-1.4.5.jar

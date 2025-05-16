/**
 * MSS_ProfileQueryServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.turkcelltech.mobilesignature.validation.soap;

public class MSS_ProfileQueryServiceLocator extends org.apache.axis.client.Service implements com.turkcelltech.mobilesignature.validation.soap.MSS_ProfileQueryService {

    public MSS_ProfileQueryServiceLocator() {
    }


    public MSS_ProfileQueryServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MSS_ProfileQueryServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MSS_ProfileQueryPort
    //private java.lang.String MSS_ProfileQueryPort_address = "http://mobilimza.corbuss.com.tr/MSSP/services/MSS_ProfileQueryPort";
    private java.lang.String MSS_ProfileQueryPort_address = "https://msign.turkcell.com.tr/MSSP2/services/MSS_ProfileQueryPort";
    // this default address is changed to MSSP2 and will be tested

    public java.lang.String getMSS_ProfileQueryPortAddress() {
        return MSS_ProfileQueryPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MSS_ProfileQueryPortWSDDServiceName = "MSS_ProfileQueryPort";

    public java.lang.String getMSS_ProfileQueryPortWSDDServiceName() {
        return MSS_ProfileQueryPortWSDDServiceName;
    }

    public void setMSS_ProfileQueryPortWSDDServiceName(java.lang.String name) {
        MSS_ProfileQueryPortWSDDServiceName = name;
    }

    public com.turkcelltech.mobilesignature.validation.soap.MSS_ProfileQueryPortType getMSS_ProfileQueryPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MSS_ProfileQueryPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMSS_ProfileQueryPort(endpoint);
    }

    public com.turkcelltech.mobilesignature.validation.soap.MSS_ProfileQueryPortType getMSS_ProfileQueryPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.turkcelltech.mobilesignature.validation.soap.MSS_ProfileQueryBindingStub _stub = new com.turkcelltech.mobilesignature.validation.soap.MSS_ProfileQueryBindingStub(portAddress, this);
            _stub.setPortName(getMSS_ProfileQueryPortWSDDServiceName());
            return _stub;
        }
        catch (Exception e) {
            return null;
        }
    }

    public void setMSS_ProfileQueryPortEndpointAddress(java.lang.String address) {
        MSS_ProfileQueryPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.turkcelltech.mobilesignature.validation.soap.MSS_ProfileQueryPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.turkcelltech.mobilesignature.validation.soap.MSS_ProfileQueryBindingStub _stub = new com.turkcelltech.mobilesignature.validation.soap.MSS_ProfileQueryBindingStub(new java.net.URL(MSS_ProfileQueryPort_address), this);
                _stub.setPortName(getMSS_ProfileQueryPortWSDDServiceName());
                return _stub;
            }
        }
        catch (Exception e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("MSS_ProfileQueryPort".equals(inputPortName)) {
            return getMSS_ProfileQueryPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://turkcelltech.com/mobilesignature/validation/soap", "MSS_ProfileQueryService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://turkcelltech.com/mobilesignature/validation/soap", "MSS_ProfileQueryPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MSS_ProfileQueryPort".equals(portName)) {
            setMSS_ProfileQueryPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.29 at 08:33:29 AM EEST 
//


package lt.kurti.defectregistry.wsdl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the lt.kurti.defectregistry.wsdl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetDefectsRequest_QNAME = new QName("http://www.kurti.lt/defectregistry/wsdl", "getDefectsRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: lt.kurti.defectregistry.wsdl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetUserResponse }
     * 
     */
    public GetUserResponse createGetUserResponse() {
        return new GetUserResponse();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link GetDefectResponse }
     * 
     */
    public GetDefectResponse createGetDefectResponse() {
        return new GetDefectResponse();
    }

    /**
     * Create an instance of {@link Defect }
     * 
     */
    public Defect createDefect() {
        return new Defect();
    }

    /**
     * Create an instance of {@link GetDefectsResponse }
     * 
     */
    public GetDefectsResponse createGetDefectsResponse() {
        return new GetDefectsResponse();
    }

    /**
     * Create an instance of {@link PatchUserToDefectRequest }
     * 
     */
    public PatchUserToDefectRequest createPatchUserToDefectRequest() {
        return new PatchUserToDefectRequest();
    }

    /**
     * Create an instance of {@link PutUserToDefectRequest }
     * 
     */
    public PutUserToDefectRequest createPutUserToDefectRequest() {
        return new PutUserToDefectRequest();
    }

    /**
     * Create an instance of {@link GetDefectRequest }
     * 
     */
    public GetDefectRequest createGetDefectRequest() {
        return new GetDefectRequest();
    }

    /**
     * Create an instance of {@link DeleteUserForDefectRequest }
     * 
     */
    public DeleteUserForDefectRequest createDeleteUserForDefectRequest() {
        return new DeleteUserForDefectRequest();
    }

    /**
     * Create an instance of {@link DeleteDefectRequest }
     * 
     */
    public DeleteDefectRequest createDeleteDefectRequest() {
        return new DeleteDefectRequest();
    }

    /**
     * Create an instance of {@link AddUserToDefectRequest }
     * 
     */
    public AddUserToDefectRequest createAddUserToDefectRequest() {
        return new AddUserToDefectRequest();
    }

    /**
     * Create an instance of {@link UpdateDefectRequest }
     * 
     */
    public UpdateDefectRequest createUpdateDefectRequest() {
        return new UpdateDefectRequest();
    }

    /**
     * Create an instance of {@link GetUsersForDefectRequest }
     * 
     */
    public GetUsersForDefectRequest createGetUsersForDefectRequest() {
        return new GetUsersForDefectRequest();
    }

    /**
     * Create an instance of {@link PatchDefectRequest }
     * 
     */
    public PatchDefectRequest createPatchDefectRequest() {
        return new PatchDefectRequest();
    }

    /**
     * Create an instance of {@link CreateDefectRequest }
     * 
     */
    public CreateDefectRequest createCreateDefectRequest() {
        return new CreateDefectRequest();
    }

    /**
     * Create an instance of {@link UserIdentifier }
     * 
     */
    public UserIdentifier createUserIdentifier() {
        return new UserIdentifier();
    }

    /**
     * Create an instance of {@link GetUserForDefectRequest }
     * 
     */
    public GetUserForDefectRequest createGetUserForDefectRequest() {
        return new GetUserForDefectRequest();
    }

    /**
     * Create an instance of {@link DeleteDefectResponse }
     * 
     */
    public DeleteDefectResponse createDeleteDefectResponse() {
        return new DeleteDefectResponse();
    }

    /**
     * Create an instance of {@link GetUsersResponse }
     * 
     */
    public GetUsersResponse createGetUsersResponse() {
        return new GetUsersResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.kurti.lt/defectregistry/wsdl", name = "getDefectsRequest")
    public JAXBElement<Object> createGetDefectsRequest(Object value) {
        return new JAXBElement<Object>(_GetDefectsRequest_QNAME, Object.class, null, value);
    }

}

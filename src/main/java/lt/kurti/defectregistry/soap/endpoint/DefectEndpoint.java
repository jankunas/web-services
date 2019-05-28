package lt.kurti.defectregistry.soap.endpoint;

import static lt.kurti.defectregistry.soap.Constants.DEFECT_DELETED_SUCCESSFULLY;
import static lt.kurti.defectregistry.soap.Constants.ERROR_CODE;
import static lt.kurti.defectregistry.soap.Constants.NAMESPACE_URI;
import static lt.kurti.defectregistry.soap.Constants.NOT_FOUND;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.DEFECT_NOT_FOUND_BY_ID;

import java.util.List;

import lt.kurti.defectregistry.soap.exceptions.ServiceFault;
import lt.kurti.defectregistry.soap.exceptions.ServiceFaultException;
import lt.kurti.defectregistry.soap.service.DefectService;
import lt.kurti.defectregistry.wsdl.CreateDefectRequest;
import lt.kurti.defectregistry.wsdl.Defect;
import lt.kurti.defectregistry.wsdl.DeleteDefectRequest;
import lt.kurti.defectregistry.wsdl.DeleteDefectResponse;
import lt.kurti.defectregistry.wsdl.GetDefectRequest;
import lt.kurti.defectregistry.wsdl.GetDefectResponse;
import lt.kurti.defectregistry.wsdl.GetDefectsResponse;
import lt.kurti.defectregistry.wsdl.PatchDefectRequest;
import lt.kurti.defectregistry.wsdl.UpdateDefectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class DefectEndpoint {

	private final DefectService defectService;

	@Autowired
	public DefectEndpoint(@Qualifier("soapDefectService") final DefectService defectService) {
		this.defectService = defectService;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDefectsRequest")
	@ResponsePayload
	public GetDefectsResponse getDefects() {
		GetDefectsResponse response = new GetDefectsResponse();
		final List<Defect> result = defectService.getDefects();
		result.forEach(defect -> response.getDefect().add(defect));

		return response;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDefectRequest")
	@ResponsePayload
	public GetDefectResponse getDefectById(@RequestPayload GetDefectRequest getDefectRequest) {
		final Defect defect = defectService.getDefectById(getDefectRequest.getId());

		if (defect == null) {
			throw new ServiceFaultException(ERROR_CODE, new ServiceFault(NOT_FOUND, DEFECT_NOT_FOUND_BY_ID));
		}

		GetDefectResponse getDefectResponse = new GetDefectResponse();
		getDefectResponse.setDefect(defect);
		return getDefectResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "createDefectRequest")
	@ResponsePayload
	public GetDefectResponse addDefect(@RequestPayload CreateDefectRequest createDefectRequest) {
		final Defect defect = defectService.createDefect(createDefectRequest);

		GetDefectResponse getDefectResponse = new GetDefectResponse();
		getDefectResponse.setDefect(defect);

		return getDefectResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateDefectRequest")
	@ResponsePayload
	public GetDefectResponse updateDefect(@RequestPayload UpdateDefectRequest updateDefectRequest) {
		final Defect result = defectService.updateDefect(updateDefectRequest);

		final GetDefectResponse getDefectResponse = new GetDefectResponse();

		getDefectResponse.setDefect(result);

		return getDefectResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "patchDefectRequest")
	@ResponsePayload
	public GetDefectResponse patchDefect(@RequestPayload PatchDefectRequest patchDefectRequest) {
		final Defect result = defectService.patchDefect(patchDefectRequest);

		final GetDefectResponse getDefectResponse = new GetDefectResponse();

		getDefectResponse.setDefect(result);

		return getDefectResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteDefectRequest")
	@ResponsePayload
	public DeleteDefectResponse deleteDefect(@RequestPayload DeleteDefectRequest deleteDefectRequest) {
		defectService.deleteDefectById(deleteDefectRequest.getId());

		final DeleteDefectResponse deleteDefectResponse = new DeleteDefectResponse();
		deleteDefectResponse.setMessage(DEFECT_DELETED_SUCCESSFULLY);

		return deleteDefectResponse;
	}

}

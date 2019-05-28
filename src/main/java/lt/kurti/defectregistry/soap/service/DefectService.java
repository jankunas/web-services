package lt.kurti.defectregistry.soap.service;

import java.util.List;

import lt.kurti.defectregistry.wsdl.CreateDefectRequest;
import lt.kurti.defectregistry.wsdl.Defect;
import lt.kurti.defectregistry.wsdl.PatchDefectRequest;
import lt.kurti.defectregistry.wsdl.UpdateDefectRequest;


public interface DefectService {

	Defect createDefect(CreateDefectRequest createDefectRequest);

	Defect updateDefect(UpdateDefectRequest updateDefectRequest);

	Defect patchDefect(PatchDefectRequest patchDefectRequest);

	List<Defect> getDefects();

	Defect getDefectById(Long id, final String embedded);

	Defect getDefectById(Long id);

	void deleteDefectById(Long id);
}

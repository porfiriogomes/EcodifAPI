package br.com.ecodif.qodisco;

import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

public class RepositoryDao {

	private String repositoryUrl;
	
	private StringBuilder getPrefixes(){
		StringBuilder sb = new StringBuilder();
		sb.append("PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>");
		sb.append(" PREFIX ecodif: <http://consiste.dimap.ufrn.br/ontologies/ecodif#>");
		sb.append(" PREFIX qodisco: <http://consiste.dimap.ufrn.br/ontologies/qodisco#>");

		return sb;
	}
	
	public RepositoryDao(String repositoryUrl){
		this.repositoryUrl = repositoryUrl;
	}
	
	public void insertObservationResultValue(String observationName, String data, String unit){
		StringBuilder sb = this.getPrefixes();
		
		sb.append(" INSERT DATA { ");
		sb.append(String.format(" ecodif:%s_resultvalue a ssn:ObservationValue ;", observationName));
		sb.append(String.format(" <http://purl.oclc.org/NET/ssnx/product/smart-knife#hasQuantityUnitOfMeasurement> <%s> ;", unit));
		sb.append(String.format(" ssn:hasQuantityValue %s . }", data));
				
		UpdateRequest request = UpdateFactory.create(sb.toString());
		UpdateProcessor proc = UpdateExecutionFactory.createRemote(request, repositoryUrl+"/update");
		proc.execute();
	}
	
	public void insertQoCIndicator(String observationName, String qoCCriterion, String qoCValue){
		StringBuilder sb = this.getPrefixes();
		
		sb.append(" INSERT DATA { ");
		sb.append(String.format(" ecodif:%s_qocindicator a qodisco:QoCIndicator ;", observationName));
		sb.append(String.format(" qodisco:has_qoc_criterion <%s> ; ", qoCCriterion));
		sb.append(String.format(" qodisco:has_qoc_value %s . } ", qoCValue));
		
		UpdateRequest request = UpdateFactory.create(sb.toString());
		UpdateProcessor proc = UpdateExecutionFactory.createRemote(request, repositoryUrl+"/update");
		proc.execute();
	}
	
	public void insertSensorOutput(String observationName, String sensorName){
		StringBuilder sb = this.getPrefixes();
		
		sb.append(" INSERT DATA { ");
		sb.append(String.format(" ecodif:%s_output> a ssn:SensorOutput ;", observationName));
		sb.append(String.format(" ssn:isProducedBy ecodif:%s ; ", sensorName));
		sb.append(String.format(" qodisco:has_qoc ecodif:_qocindicator ; ", observationName));
		sb.append(String.format(" ssn:hasValue ecodif:%s_resultvalue> . } ", observationName));
		
		UpdateRequest request = UpdateFactory.create(sb.toString());
		UpdateProcessor proc = UpdateExecutionFactory.createRemote(request, repositoryUrl+"/update");
		proc.execute();
	}
	
	public void insertObservation(String observationName, String date, String sensorName, String observedProperty, String address){
		StringBuilder sb = this.getPrefixes();

		sb.append(" INSERT DATA { ");
		sb.append(String.format(" ecodif:%s a ssn:Observation ;", observationName));
		sb.append(String.format(" ssn:observationResultTime '%s' ; ", date));
		sb.append(String.format(" ssn:observedBy ecodif:%s ; ", sensorName));
		sb.append(String.format(" ssn:observedProperty <%s> ; ", observedProperty ));
		sb.append(String.format(" ecodif:from <%s> ;", address));
		sb.append(String.format(" ssn:observationResult ecodif:%s_output . }", observationName));
				
		UpdateRequest request = UpdateFactory.create(sb.toString());
		UpdateProcessor proc = UpdateExecutionFactory.createRemote(request, repositoryUrl+"/update");
		proc.execute();
		
	}
}

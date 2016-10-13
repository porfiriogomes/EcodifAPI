package br.com.ecodif.qodisco;

import br.com.ecodif.qodisco.RepositoryDao;

public class RepositoryService {
	
	private RepositoryDao repositoryDao;

	public void insertObservation(String repositoryUrl, String sensorName, String data, String observedProperty, String unit, String date, String qoCCriterion, int qoCValue, String observationName, String feed){
		
		repositoryDao = new RepositoryDao(repositoryUrl);
		repositoryDao.insertObservationResultValue(observationName, data, unit);
		repositoryDao.insertQoCIndicator(observationName, qoCCriterion, ""+qoCValue);
		repositoryDao.insertSensorOutput(observationName, sensorName);
		repositoryDao.insertObservation(observationName, date, sensorName, observedProperty, feed);
	}
}

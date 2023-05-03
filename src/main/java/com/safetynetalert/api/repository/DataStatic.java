package com.safetynetalert.api.repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.jsoniter.JsonIterator;
import com.safetynetalert.api.logMessage.LogMessage;
import com.safetynetalert.api.model.Datas;

@Repository
public class DataStatic {

	private static final Logger log = LoggerFactory.getLogger(DataStatic.class);

	private static Datas originalDatas = new Datas();
	private static Datas datas = new Datas();

	public static void loadData(String filepath) {
		if (!filepath.isEmpty()) {
			String dataFile = "";

			try {
			    InputStream resource = new ClassPathResource(filepath).getInputStream();

	    	    BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
	    	    dataFile = reader.lines().collect(Collectors.joining("\n"));

				// enregistrer les données de sauvegarde dans "originalDatas"
				originalDatas = new Datas(JsonIterator.deserialize(dataFile, Datas.class));

				// copier les données de sauvegarde dans "datas"
				resetDatas();

			} catch (FileNotFoundException e) {
				log.error(LogMessage.getMessage("file '" + filepath + "' not found"));
				setNullDatas();
			} catch (IOException e) {
				log.error(LogMessage.getMessage("IOException"));
				setNullDatas();
			}
		} else {
			setNullDatas();
		}
	}

	public static Datas getDatas() {
		return datas;
	}

	public static void resetDatas() {
		datas = new Datas(originalDatas);
	}

	private static void setNullDatas() {
		originalDatas = null;
		datas = null;
	}
}

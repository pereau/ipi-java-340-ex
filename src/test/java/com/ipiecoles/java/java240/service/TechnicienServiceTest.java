package com.ipiecoles.java.java240.service;

import java.util.HashSet;

import org.assertj.core.api.Assertions;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ipiecoles.java.java340.model.Manager;
import com.ipiecoles.java.java340.model.Technicien;
import com.ipiecoles.java.java340.repository.ManagerRepository;
import com.ipiecoles.java.java340.repository.TechnicienRepository;
import com.ipiecoles.java.java340.service.TechnicienService;

@RunWith(MockitoJUnitRunner.class)
public class TechnicienServiceTest {
	
	@InjectMocks
	public TechnicienService technicienService;
	
	@Mock
	public TechnicienRepository technicienRepository;
	
	@Mock
	public ManagerRepository ManagerRepository;
	
	
	@Test
	public void testAddManager() {
		//Given
		final String MATRICULE="M12345";
		final Long ID_TECHNICIEN=1L;
		
		Technicien technicien=new Technicien("Durand","Jean","T12345",new LocalDate(),1500d,3);
		Manager manager =new Manager("Dupond","Jacques",MATRICULE,new LocalDate(),2500d,new HashSet<>());//HashSet<> correspond à l'équipe initialisée à vide
		
		//Simulation de la base de données
		Mockito.when(technicienRepository.findOne(ID_TECHNICIEN)).thenReturn(technicien);
		Mockito.when(ManagerRepository.findByMatricule(MATRICULE)).thenReturn(manager);
		Mockito.when(ManagerRepository.save(Mockito.any(Manager.class))).then(AdditionalAnswers.returnsFirstArg());
		Mockito.when(technicienRepository.save(Mockito.any(Technicien.class))).then(AdditionalAnswers.returnsFirstArg());
		
		
		//When
		manager=technicienService.addManager(ID_TECHNICIEN, MATRICULE);
		
		//Then
        Mockito.verify(technicienRepository,Mockito.times(1)).findOne(ID_TECHNICIEN);
        Mockito.verify(technicienRepository,Mockito.times(1)).findByMatricule(MATRICULE);
		
		Assertions.assertThat(manager.getEquipe()).hasSize(1);
		Assertions.assertThat(manager.getEquipe()).contains(technicien);
		
		Assertions.assertThat(technicien.getManager()).isEqualTo(manager);
		
		ArgumentCaptor<Technicien> technicienCaptor=ArgumentCaptor.forClass(Technicien.class); // 
		Mockito.verify(technicienRepository).save(technicienCaptor.capture()); // vérifie que la valeur  est récupérée après un save
		Assertions.assertThat(technicienCaptor.getValue().getManager()).isEqualTo(manager);
		
	}
	
	

}

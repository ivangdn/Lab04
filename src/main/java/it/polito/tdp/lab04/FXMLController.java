/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.lab04;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.lab04.model.Corso;
import it.polito.tdp.lab04.model.Model;
import it.polito.tdp.lab04.model.Studente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML // fx:id="chkMatricola"
    private CheckBox chkMatricola; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCorsi"
    private ComboBox<Corso> cmbCorsi; // Value injected by FXMLLoader

    @FXML // fx:id="txtCognome"
    private TextField txtCognome; // Value injected by FXMLLoader

    @FXML // fx:id="txtMatricola"
    private TextField txtMatricola; // Value injected by FXMLLoader

    @FXML // fx:id="txtNome"
    private TextField txtNome; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCercaCorsi(ActionEvent event) {
    	txtResult.clear();
    	
    	String matricola = txtMatricola.getText();
    	Integer matricolaNumerica;
    	try {
    		matricolaNumerica = Integer.parseInt(matricola);
    		Studente s = this.model.getStudenteByMatricola(matricolaNumerica);
    		if(s == null) {
    			txtResult.setText("La matricola inserita non esiste.");
    			return;
    		}
    	
    		List<Corso> corsiStudente = this.model.getCorsiFromStudente(s);
    		StringBuilder sb = new StringBuilder();
    		
    		for(Corso c : corsiStudente) {
    			sb.append(String.format("%-10s", c.getCodins()));
    			sb.append(String.format("%-5s", c.getCrediti()));
    			sb.append(String.format("%-45s", c.getNome()));
    			sb.append(String.format("%s", c.getPd()));
    			sb.append("\n");
    		}
    		txtResult.setText(sb.toString());
    		
    	} catch (NumberFormatException e) {
    		txtResult.setText("Inserisci una matricola numerica."); 		
    	} catch (RuntimeException e) {
    		txtResult.setText("ERRORE DI CONNESSIONE AL DATABASE");
    	}
    }

    @FXML
    void doCercaIscrittiCorso(ActionEvent event) {
    	txtResult.clear();
    	
    	Corso c = cmbCorsi.getValue();
    	if(c == null || c.getNome().isEmpty()) {
    		txtResult.setText("Selezionare un corso.");
    		return;
    	}
    	
    	try {
    		List<Studente> studenti = this.model.getStudentiIscrittiAlCorso(c);
    		StringBuilder sb = new StringBuilder();
    		
    		for(Studente s : studenti) {
    			sb.append(String.format("%-10s", s.getMatricola()));
    			sb.append(String.format("%-20s", s.getNome()));
    			sb.append(String.format("%-20s", s.getCognome()));
    			sb.append(String.format("%s", s.getCDS()));
    			sb.append("\n");
    		}
    		txtResult.setText(sb.toString());
    		
    	} catch (RuntimeException e) {
    		txtResult.setText("ERRORE DI CONNESSIONE AL DATABASE");
    	}
    }

    @FXML
    void doCompleta(ActionEvent event) {
    	txtNome.clear();
    	txtCognome.clear();
    	txtResult.clear();
    	
    	String matricola = txtMatricola.getText();
    	Integer matricolaNumerica;
    	try {
    		matricolaNumerica = Integer.parseInt(matricola);
    		Studente s = this.model.getStudenteByMatricola(matricolaNumerica);
    		if(s == null) {
    			txtResult.setText("La matricola inserita non esiste.");
    			return;
    		}
    	
    		txtNome.setText(s.getNome());
    		txtCognome.setText(s.getCognome());
    		
    	} catch (NumberFormatException e) {
    		txtResult.setText("Inserisci una matricola numerica."); 		
    	} catch (RuntimeException e) {
    		txtResult.setText("ERRORE DI CONNESSIONE AL DATABASE");
    	}  	
    }

    @FXML
    void doIscrivi(ActionEvent event) {
    	txtResult.clear();
    	
    	Corso c = cmbCorsi.getValue();
    	if(c == null || c.getNome().isEmpty()) {
    		txtResult.setText("Selezionare un corso.");
    		return;
    	}
    	
    	String matricola = txtMatricola.getText();
    	Integer matricolaNumerica;
    	
    	try {
    		matricolaNumerica = Integer.parseInt(matricola);
    		Studente s = this.model.getStudenteByMatricola(matricolaNumerica);
    		if(s == null) {
    			txtResult.setText("La matricola inserita non esiste.");
    			return;
    		}
    		
    		Boolean isIscritto = this.model.isStudenteIscrittoACorso(s, c);
    		if(isIscritto) {
    			txtResult.setText("Studente già iscritto a questo corso.");
    			return;
    		}
    		
    		Boolean iscrivi = this.model.inscriviStudenteACorso(s, c);
    		if(!iscrivi) {
    			txtResult.setText("Errore durante l'iscrizione al corso.");
    			return;
    		} else {
    			txtResult.setText("Studente iscritto al corso!");
    		}
    		
    	} catch (NumberFormatException e) {
    		txtResult.setText("Inserisci una matricola numerica.");
    	} catch (RuntimeException e) {
    		txtResult.setText("ERRORE DI CONNESSIONE AL DATABASE");
    	}	
    } 

    @FXML
    void doReset(ActionEvent event) {
    	txtResult.clear();
    	txtMatricola.clear();
    	txtNome.clear();
    	txtCognome.clear();
    	chkMatricola.setSelected(false);
    	cmbCorsi.getSelectionModel().clearSelection();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	assert chkMatricola != null : "fx:id=\"chkMatricola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCorsi != null : "fx:id=\"cmbCorsi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCognome != null : "fx:id=\"txtCognome\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMatricola != null : "fx:id=\"txtMatricola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNome != null : "fx:id=\"txtNome\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbCorsi.getItems().clear();
    	List<Corso> corsi = model.getTuttiICorsi();
    	Collections.sort(corsi);
    	Corso campoVuoto = new Corso("", 0, "", 0);
    	cmbCorsi.getItems().add(campoVuoto);
    	for(Corso c : corsi) {
    		cmbCorsi.getItems().add(c);
    	}
    }

}

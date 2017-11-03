package gui;

import java.util.Observable;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import riskGame.Country;

public interface IStartGUI {
	

	/**
	 * Method sets up the combobox the way we want, including the properties of showing 
	 * the number of troops available on that location.
	 * 
	 * @param comboBoxName The name of the combobox you would like to set up
	 * @param country The name of the country that this combobox belongs to, as a country
	 * @param nameOfCountry The string name of the country
	 */
	//void setupComboBox(JComboBox<String> comboBoxName, Country country, String nameOfCountry);

	//setup labels showing number of troops
	//method needs to update at each phase 
	//void setupTerritoriesTroopLabel(JLabel labelName, Country countryname, int x, int y);

	/**
	 * updates my GUI ->  
	 */
	//void update(Observable o, Object arg);

}
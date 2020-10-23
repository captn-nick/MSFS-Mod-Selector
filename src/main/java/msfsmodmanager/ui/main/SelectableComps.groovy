package msfsmodmanager.ui.main

import groovy.transform.CompileStatic

import msfsmodmanager.model.*

@CompileStatic
class SelectableComps {
    public static List<SelectableCheckBox> create(Continent continent) {
        return createForCountries(continent) + createForCities(continent)
    }
    
    private static List<SelectableCheckBox> createForCountries(Continent continent) {
        return continent.countries.collect {
            if (Cities.byCountry(it).empty) {
                return new SelectableCheckBox(it, true)
            }
            else {
                return new CountryPanel(it)
            }
        }
    }
    
    public static List<SelectableCheckBox> createForCities(Continent continent) {
        return Cities.byContinent(continent).collect { String it ->
            new SelectableCheckBox(it, false)
        }
    }
    
    public static List<SelectableCheckBox> createForCities(String country) {
        return (Cities.byCountry(country)).collect {
            new SelectableCheckBox(it, false)
        }
    }
    
    public static Selection getSelection(List<ContinentPanel> panels) {
        Selection selection = new Selection(getSelectedContinents(panels))
        
        panels.each { continentPanel -> 
            selection.countries.addAll(getSelectedCountries(continentPanel))
            continentPanel.selectableComponents.each { countryPanel ->
                if (countryPanel in CountryPanel) {
                    // then it has cities
                    selection.cities.addAll(getSelectedCities((CountryPanel)countryPanel))
                }
            }
            selection.cities.addAll(getSelectedCities(continentPanel))
        }
        
        return selection
    }
    
    public static List<String> getSelectedContinents(List<ContinentPanel> panels) {
        return panels.findAll { it.selected }*.name
    }
    
    public static List<String> getSelectedCountries(ContinentPanel panel) {
        getSelected(panel, false)
    }
    
    public static List<String> getSelectedCities(ContinentPanel panel) {
        getSelected(panel, true)
    }
    
    public static List<String> getSelectedCities(CountryPanel panel) {
        getSelected(panel, true)
    }
    
    private static List<String> getSelected(CheckBoxTitledBorderPanel panel, boolean city) {
        return (((List<SelectableComp>)(panel.selectableComponents)).findAll { 
                SelectableComp it -> it.selected && it.city == city
            }).collect {
                it.name
            }
    }
}


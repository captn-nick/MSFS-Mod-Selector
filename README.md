# MSFS-Mod-Selector

<img align="right" src="documentation/screenshot.png"/>

The MSFS mod selector solves a common problem in **Microsoft Flight Simulator 2020 (MSFS)**: even though only a few weeks after release, there are hundreds of high-quality **community-created mods** to enhance MSFS experience even further, installing a large number of them considerably slows down MSFS startup time to an unbearable point.

A simple manual solution is to manually copy only the mods you want to use at one time before starting the simulator.

The Mod Selector assists you in this by **automating the selection process**: it allows you to easily group mods by type and (for world enhancements which would be most mods) by continent, country, and even by city as well as by aircraft type (for aircraft mods).

Here’s how this works:
* You must register each mod once with the Mod Selector.
* Then, start up the Mod Selector before starting MSFS.
* Simply select all the mod categories you want to activate.
* The Mod Selector will automatically copy the relevant mods into your Community folder / move away all unwanted mods into a temp folder.
* Then, start MSFS and enjoy a significantly decreased startup time.

## Usage warning
This program is very early beta. USE AT YOUR OWN RISK!
It is highly recommended that you create a backup of your mods (MSFS Community folder) before using this program.

**Until V. 1.0 is reached, please expect breaking changes with each version jump!**

## Project roadmap
All of this is subject to change.
* V. 0.5: Improved error handling.
* V. 0.6: Config file cleanup
* V. 0.7: Create a central online repository for mod lookup so individual users don’t have to create their own mods.txt themselves manually.
* V. 0.8: Make continent sub-panel sizes adjustable; show mod selection feedback.
* V. 0.9: Intelligent wizard for adding mods to mods.txt.
* V. 0.10: Local / online individual mod search by keywords.

## Prerequisites
Mod Selector requires Java to run.

## Download
Download the [latest version ZIP file](https://github.com/captn-nick/MSFS-Mod-Selector/releases/download/0.5/MsfsModSelector.0.5.zip) from the [Releases page](https://github.com/captn-nick/MSFS-Mod-Selector/releases).

## Content
The program consists of 3 components:
* **MsfsModSelector.jar**: the program file.
* **mods.txt**: a text file containing information about all the mods managed by the Mod selector.
* **config.properties**: a text file containing all configurations for the Mod selector.

## Setup
No setup is required.

1. **Simply unzip the content of the ZIP file into the MSFS “Packages” folder.**
1.	**You will need to add all mods which you want to manage manually to mods.txt first, see below.**
1. **Finally, start the Mod Selector by double-clicking the program file or via the command line, see below.**

Make sure that the directory structure is correct:
* Packages/
  * MsfsModSelector.jar
  * config.properties
  * mods.txt
  * Community/
  * _Temp/_
  * (other folders of MSFS)

If you placed the Mod Selector into your MSFS “Packages” folder, the Mod Selector will recognize the sub-folder “Community” as the directory to place all active mods into, and the sub-folder “Temp” as the directory to temporarily store deactivated mods. Mod Selector will create the “Temp” directly if it doesn't exist already.

Alternatively, you can config those paths manually, see below.

## Config
The **config.properties** files contains the Mod Selector configuration. A single config consists of a

```key=value```

entry.

The following keys are of note:
* ```path.community```: the full path of the Community mod directory
* ```path.temp```: the full path of the temporary mod directory. Disabled mods will be stored here.
* ```ModType.defaultSelection```: defines which mod types are selected by default.
* ```ModType.XY```: defines the name of mod type XY as it is displayed in the UI.
* ```AircraftType```: defines the title of the “aircraft” section as it is displayed in the UI.
* ```AircraftType.XY```: defines the name of aircraft type XY as it is displayed in the UI.
* ```Continent.XY.Countries```: defines which countries to show in the UI for individual selection and in which order for continent XY.
* ```Continent.XY```: defines the name of continent XY as it is displayed in the UI.
* ```Country.XY```: defines the name of country XY as it is displayed in the UI.
* ```Country.XY.showCities```: set to ```=false``` to not show any individual cities for this particular country in the UI. Defaults to ```=true```.
* ```Cities.minMods```: defines the minimum number of mods which must be linked to a city in order to show it individually in the UI.

Note:
* For the paths, backslashes have to be doubled (```\\``` instead of ```\```).
* You are free to define your own countries. As long as at least one mod is designated to this country (and defines a continent as well), Mod Selector will be able to link this country to its continent, i.e. a country does not have to be listed explicitly in any Continent.XY.Countries entry.
* You are free to define your own cities simply by linking them to any mod (see below). For cities, no config is required. All cities explicitly linked to any mod will show up in the UI in alphabetical order.
* The US are their own continent simply because that way we can group mods on two levels by state and city which seems to make sense.

## Adding a mod
In order to add a mod under Mod Selector management, you must:
* place the mod folder in the Community or Temp directory
* add an entry to the **mods.txt** file

A mods.txt file entry consists of a

```Type\tContinent\tCountry\tName\tCityOrIcaoOrAircrafttype ## Description\tAuthor\tWebsite```

entry whereas
* ```\t```: a single tab character. All elements are _separated by tabs_.
* ```Type```: abbreviation of one of the supported types. Supported types are:
  * ```AP```: airport
  * ```LM```: individual landmarks
  * ```CT```: entire cities
  * ```LS```: individually modeled big landscape features, e.g. mountains (typically contain big texture files)
  * ```LF```: general landscape fixes, e.g. river elevation fixes (typically don’t contain any texture files)
  * ```AL```: aircraft livreries
  * ```AM```: aircraft models
  * ```OT```: other
* ```Continent```: abbreviation of one of the supported continents. Leave blank for livrery mods.
* ```Country```: abbreviation of a country. Leave blank for livrery mods
* ```Name```: The exact folder name of the mod in question.
* ```CityOrIcaoOrAircrafttype```: one of the following:
  * ```City```: the actual name (no abbreviation!) of the city. Available for landmarks, cities, landscapes, and landscape fixes only. Omit this and the preceding tab if not used. 
  * ```Icao```: the ICAO code. Available for airports only. Omit this and the preceding tab if not used.
  * ```Aircrafttype```: abbreviation of one of the supported aircraft types. Available for livreries and aircraft models only. Omit this and the preceding tab if not used.
* ```##``` (two hash signs, optionally surrounded by space): a separator. Separates off the following section of optional information. Omit if none of the following information is given.
* ```Description```: a short description or “title” of the mod, e.g. the mod title as found on the download website.
* ```Author```: the mod author’s name.
* ```Website```: the website the mod can be downloaded from. Only https://flightsim.to sub-pages are supported.

Examples (note: this project is not affiliated with these mods in any way):
* ```AP\tEU\tUK\tairport-eght-tresco-superspud``` for https://flightsim.to/file/598/eght-tresco-isles-of-scilly-uk, an airport in the UK, Europe.
* ```LM\tOZ\tNZ\taucklandharbourbridge\tAuckland ## Auckland Harbour Bridge\tspas3manhttps://flightsim.to/file/146/auckland-harbour-bridge``` for https://flightsim.to/file/146/auckland-harbour-bridge, the Harbour Bridge landmark in Auckland, New Zealand, Oceania.
* ```LS\tUS\t\tdevilstower-wyoming ## Devil's Tower\tVFXSimmer\thttps://flightsim.to/file/186/devil-s-tower``` for https://flightsim.to/file/186/devil-s-tower, the Devil's Tower, a landscape feature in the US (here, as an example, I omitted the "country" part).
* ```LI\t\t\tliveries-xcub``` for the X-Cub part of the Livreries mega pack, as featured on https://flightsim.to/.

(Note to replace ```\t``` with a actual tab in these examples.)

[You can find an example of **a complete mods.txt file** here](https://github.com/captn-nick/MSFS-Mod-Selector/blob/master/external-test-resources/mods.txt).

## Start and usage
* Start the Mod Selector by double-clicking the program file or via the command line, see below.
* If there are startup errors / warnings, see below.
* Otherwise, the main UI will show up.
* In the top bar, you can globally select / deselect entire mod categories. Note that liveries are not bound to any of the geographical areas below.
* In the main UI, you can select / deselect mods by continent, country, and city.
* Click on Apply to move all activated mods into the Community folder and all deactivated mods into the Temp folder.
* (Click on Deactivate all to quickly move all mods from the Community folder into the Temp folder, regardless of your current selection.)
* Then, startup MSFS.

Please note the way geographical selection works. Geographical selection is actually “subtractive”. That means: If you don’t activate a sub-region (country / city) that sub-region will be deactivated even if the parent region is activated. Selecting a parent region will only activate all mods which are not explicitly part of one of the listed sub-regions.

For instance, if you select Europe and France but not Germany, no mods from Germany will be activated; however, all mods from France will be activated plus all mods from regions of Europe which are not explicitly shown in the UI. For instance, if you had a mod linked to Liechtenstein and Liechtenstein is not explicitly listed as a country of Europe it will be activated only when Europe as a continent is selected.

Same goes for cities: if you select Europe and France but not Paris, and also not Germany but Berlin, no mods from Germany will be loaded but mods explicitly linked to Berlin will; all mods from France will be loaded but none explicitly linked to Paris will; as in the example above, Liechtenstein mods would also be loaded.

Deselect Europe and all of its countries and cities except for Berlin to activate no single mod in the entirety of Europe except for all which are explicitly linked to Berlin.

In that way, selecting an entire continent / country (with cities) actually means “activate the rest of…”. I believe this is a good solution. Otherwise, we would always have to present you an explicit “the rest of” option for all “container” regions.

## Warnings and errors
At the start, the Mod Selector will check your mod registry and your directories for problems and show you an error message if it found any. You cannot start the Mod Selector without first resolving those issues.

Each error is designated with an ID. It can be found in the error UI’s header (or in the printout on the command line). Please note the following tips for helping you solve the issue at hand:
* **Error.001**: An unexpected freak error. Possibly, there is something very wrong with your system or configuration. Make sure you followed this README exactly for the setup. If you did so, this is possibly a bug. Please report it by opening an issue.
* **Error.010**: Found mods which weren't registered and have a corrupted directory structure: a combination of Error.030 and Error.050. see below on how to fix them individually.
* **Error.020**: Found mods which are duplicated (one in Community, one in Temp folder): as the text says. This may happen if you moved all mods into the Temp folder first (using the “Deactivate all” function, and then added new versions of these mods into the Community folder while the old version of the mod (with the same folder name) is still present in the Temp folder. In this case, the UI will present you with the additional option to delete the surplus mod folder in the Temp directory. Not that if you click that button, that mod folder will be deleted immediately and permanently!
* **Error.030**: Found unregistered mods: You have some mod folders which don’t correspond to an entry in your mods.txt file. Make sure to register each mod with a mods.txt entry first. See “Adding a mod” above, or delete the mod’s folder.
* **Error.040**: Found mods which were registered but aren't present in mod directory: The opposite of Error.030: You have a mods.txt entry for a mod which isn’t present as a sub-folder of either the Community or Temp directory. Either restore that folder from another place or delete the entry in question from the mods.txt file.
* **Error.050**: Found mods with corrupted directory structure: Each mod sub-folder must contain a manifest.json file on the first level of its folder hierarchy. If this is not the case, this was typically the result of the mod being incorrectly ZIPped / unzipped, i.e. with surplus directories. Make sure the directory structure for all mods is correct. Note that some mods may actually consist of more than one MSFS mod directory! In that case, you need to register each of these directories as individual mods in mods.txt.
* **Error.100**: General mods.txt line read error: Mod Selector cannot read an invidual line in your mods.txt file. Make sure the line corresponds exactly to the format described in “Adding a mod”. If you are sure it does, this is possibly a bug. Please report it by opening an issue.
* **Error.101**: mods.txt line information read error: Mod Selector can read an invidual line in your mods.txt file, but it can’t interpret a critical piece of information. Make sure the line corresponds exactly to the format described in “Adding a mod”. If you are sure it does, this is possibly a bug. Please report it by opening an issue.

## Starting from the command line
Instead of double-clicking on the program file, you can start the program from the command line / PowerShell with

```java –jar MsfsModSelector.jar```

When started in command line mode, all errors will be printed to the console.

Supported command line arguments:
* ```--dontShowErrorPopups```: shows errors only on the command line, not as popups (in case you prefer it that way)

## Usage tips
For installing new mods:
* First, use the “Deactivate all” function to move all current mods into the Temp folder.
* Then, copy/move/unzip your newly-downloaded mods into the Community folder.
* Run the Mod Selector and watch out for its warning messages. Add mods to the mods.txt file (or correct mod sub-directory structure, if needed; or remove duplicated mods) to correct the errors step by step.
* Finally, when the UI shows up, you can be sure that you again have clean mod folders and that all mods are correctly registered with Mod Selector.

## Known limitations
This program is very early beta. I have created it as a very quick & dirty solution for my own personal use but eventually decided to release it publicly, even if of little use.
Known limitations include:
* No auto-discovery / assistance to add mods. All mods have to be added manually in the mods.txt file.
* Mod Selector will not remember the last selection but always start up in the default selection.
* Only limited possibilities to re-arrange geographical areas on the UI.
* Doesn’t help identifying individual mods. Mods are simply kept in their (oftentimes chaotically named) directory structures.
* (Development: unclean build process.)
* And probably a few more improvements which would be “nice to have”…

## Version history
* 0.5:
  * full UI-based error handling (additional error handling on command line as well);
  * detailed error feedback for mods.txt parsing;
  * assisted handling for updated / duplicated mods;
  * create Temp dir at startup if not present.
* 0.4:
  * added “aircraft” and “other” mod types;
  * added aircraft-specific mod sub-categories;
  * support for 3-character ICAO codes;
  * startup bugfix for empty mods.txt file.
* 0.3:
  * ability to add more meta-information to individual mods: ICAO code, description, author, website;
  * added new continent “other/fictional”.
* 0.2:
  * warning/error feedback through UI;
  * configuration for mod types, including default selection;
  * bugfix for UTF-8 support.
* 0.1: Initial release

## Upgrade guide
* 0.4 -> 0.5:
  * (fully backward-compatible)
* 0.3 -> 0.4:
  * Make sure to add the ```ModType.AL``` / ```ModType.AM``` / ```ModType.OT``` sections as well as the ```AircraftType``` entry and the ```AircraftType.XY``` sections to your config.properties file.
  * For livrery mods, please change the mod type id ```LI``` to the new id ```AL``` for your mods.txt entries.
    * For the moment, ```LI``` is still supported for backwards compatibility.
* 0.2 -> 0.3:
  * Make sure to add the ```Cities.minMods``` as well as the ```Continent.OF.Countries``` and ```Continent.OF=Other/Fictional``` entries to your config.properties file.
  * (Optional) You may want to add the ```##``` trailing section to all your mods.txt entries.
  * (Optional) You may want to add ICAO information (instead of city information) to your airport entries in mods.txt. For airports, city information will be ignored.
* 0.1 -> 0.2:
  * Make sure to add the ```ModType.defaultSelection``` and ```ModType.XY``` sections to your config.properties file.

## Troubleshooting
If there is a problem (e.g. the program doesn’t start up on double click), start it from the command line (see above) to make sure all errors are shown.

For details, see "Warnings and errors" above.

## For Developers
This project was built using the NetBeans IDE. JFrames and JPanels are created and edited with the IDE’s Swing editor.

## Closing note
This program will also be available for download through https://flightsim.to/.

## Happy Flying!

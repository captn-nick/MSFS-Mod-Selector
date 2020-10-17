# MSFS-Mod-Selector

<img align="right" src="documentation/screenshot.png" width="50%" height="50%"/>

The MSFS mod selector solves a common problem in **Microsoft Flight Simulator 2020 (MSFS)**: even though only a few weeks after release, there are hundreds of high-quality **community-created mods** to enhance MSFS experience even further, installing a large number of them considerably slows down MSFS startup time to an unbearable point.

A simple manual solution is to manually copy only the mods you want to use at one time before starting the simulator.

The Mod Selector assists you in this by **automating the selection process**: it allows you to easily group mods by type and (for world enhancements which would be most mods) by continent, country, and even by city as well as by aircraft type (for aircraft mods).

Here’s how this works:
* Start up the Mod Selector before starting MSFS.
* You must register each mod once with the Mod Selector.
  * If a mod is already in Mod Selector’s online mod database, it will register automatically.
* Simply select all the mod categories you want to activate.
* The Mod Selector will automatically copy the relevant mods into your Community folder / move away all unwanted mods into a temp folder.
* Then, start MSFS and enjoy a significantly decreased startup time.

## New and noteworthy!

**Starting with version 0.7, Mod Selector builds its own [open-source online mod meta-information database](https://github.com/captn-nick/MSFS-Mod-Repository) everyone can contribute to!**

**Mod Selector now auto-discovers meta-information already present in the database.**

A more comfortable UI for adding mods will follow soon. Stay tuned!

## Usage warning
This program is still early beta. USE AT YOUR OWN RISK!
It is highly recommended that you create a backup of your mods (MSFS Community folder) before using this program.

**Until V. 1.0 is reached, please expect breaking changes with each version jump!**

## Project roadmap
All of this is subject to change.
* V. 0.9: Wizard for adding mods to mods.txt.
* V. 0.10: Improve mods adding wizard with automatic online mod information lookup.
* V. 0.11: Make continent sub-panel sizes adjustable; show mod selection feedback.
* V. 0.12: Local / online individual mod search by keywords.

## Prerequisites
Mod Selector requires Java to run.

## Download
Download the [latest version ZIP file](https://github.com/captn-nick/MSFS-Mod-Selector/releases/download/0.8/MsfsModSelector.0.8.zip) from the [Releases page](https://github.com/captn-nick/MSFS-Mod-Selector/releases).

## Content
The program consists of 4 components:
* **MsfsModSelector.jar**: the program file.
* **mods.txt**: a text file containing information about all the mods managed by the Mod selector.
* **config.properties**: a text file containing all configurations for the Mod selector.
* **mod-repository\mods.db.txt**: a text file containing information about all the mods known to Mod Selector, whether you possess them or not. You can regularly update this file from a central repository on the internet to get the newest mod definitions.

## Setup
No setup is required.

1. **Simply unzip the content of the ZIP file into the MSFS “Packages” folder.**
1.	**You will need to add all mods which you want to manage manually to mods.txt first, see below.**
1. **Finally, start the Mod Selector by double-clicking the program file or via the command line, see below.**

Make sure that the directory structure is correct:
* Packages/
  * MsfsModSelector.jar
  * config.properties
  * _labels.properties_
  * mods.txt
  * Community/
  * _Temp/_
  * mod-repository/
    * mods-db.txt
  * (other folders of MSFS)

Files / folders in _italics_ are optional, see below.

If you placed the Mod Selector into your MSFS “Packages” folder, the Mod Selector will recognize the sub-folder “Community” as the directory to place all active mods into, and the sub-folder “Temp” as the directory to temporarily store deactivated mods. Mod Selector will create the “Temp” directly if it doesn't exist already.

Alternatively, you can config those paths manually, see below.

## Config
The **config.properties** files contains the Mod Selector configuration. Mod Selector comes with pre-defined default values. If you want to change these, read on.

A single config consists of a

```key=value```

entry.

The following keys are of note:
* ```path.community```: the full path of the Community mod directory
* ```path.temp```: the full path of the temporary mod directory. Disabled mods will be stored here.
* ```ModsDb.updateWithGit```: set to =true to use your locally installed Git client to update (pull) the central mod repository instead of downloading the file via HTTP. See “Contributing to Mod Selector’s online mod database” below.
* ```ModType.defaultSelection```: defines which mod types are selected by default.
* ```Continent.XY.Countries```: defines which countries to show in the UI for individual selection and in which order for continent XY.
* ```Country.XY.showCities```: set to ```=false``` to not show any individual cities for this particular country in the UI. Defaults to ```=true```.
* ```Cities.minMods```: defines the minimum number of mods which must be linked to a city in order to show it individually in the UI.

Note:
* For the paths, backslashes have to be doubled (```\\``` instead of ```\```).
* You are free to define your own countries. As long as at least one mod is designated to this country (and defines a continent as well), Mod Selector will be able to link this country to its continent, i.e. a country does not have to be listed explicitly in any ```Continent.XY.Countries``` entry. To do so, add a new country definition to your labels file (see below) and, optionally, use it in a ```Continent.XY.Countries``` definition.
* You are free to define your own cities simply by linking them to any mod (see below). For cities, no config is required. All cities explicitly linked to any mod will show up in the UI in alphabetical order.
* The US and Canada are their own continent simply because for the US, we want to group mods on two levels by state and city which seems to make sense for such a big and “moddable” country. For simplicity reasons, we added Canada as its own country to that “pseudo-continent” as well (otherwise it would stand alone in a separate “North America” continent).

## Labels
Mod Selector comes with pre-defined labels for mod types, continents, countries, and more. However, you can change those labels or add your own ones (e.g. to add available countries).

For this, simply create a **labels.properties** file in the folder which contains the program file.

labels.properties uses the same key-value entry format as config.properties.

The following keys are of note:
* ```ModType.XY```: defines the name of mod type XY as it is displayed in the UI.
* ```AircraftType```: defines the title of the “aircraft” section as it is displayed in the UI.
* ```AircraftType.XY```: defines the name of aircraft type XY as it is displayed in the UI.
* ```Continent.XY```: defines the name of continent XY as it is displayed in the UI.
* ```Country.XYZ``` / ```Country.XY```: defines the name of country XYZ /  US state XY as it is displayed in the UI.

Note:
* To add a country to Mod Selector, simply add its definition to labels.properties and use it in your mods.properties file. As long as at least one mod is designated to this country (and defines a continent as well), Mod Selector will be able to link this country to its continent. Optionally, add it to the ```Continent.XY.Countries``` definition in your config file (see above) to explicitly show it for individual selection in the UI.

[You can find **all predefined labels** here](https://github.com/captn-nick/MSFS-Mod-Selector/blob/master/src/main/resources/default_labels.properties)

[You can find an example of **a complete labels.properties file** here](https://github.com/captn-nick/MSFS-Mod-Selector/blob/master/external-test-resources/labels.properties).

### Concerning countries: An explicitly non-political statement
The default country definition Mod Selector uses, as defined by its labels.properties file, is based on the [ISO 3166-1 alpha-3 country ISO codes for countries](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3#Current_codes) (3 characters) and the [ISO 3166-2:US codes for US states](https://en.wikipedia.org/wiki/ISO_3166-2:US#Current_codes) (2 characters).

However, these have been slightly modified where it made sense from a geographical or usability point of view.

Such a reason could be: “Does it make sense to divide a single rather small landmass into even smaller parts just to respect political borders or would a simulator pilot rather load either all or no mods for the complete landmass in question?” / “Does it make sense to have a large landmass with a large amount of mods or would it be useful to divide it in an easily recognizable way?” / “Does it make sense to have a country labeled by its full official name even if it distorts the UI or is it more reasonable to use a short name anyone understands?” As you can see, these questions are in no way politically motivated.

Most importantly and most prominently, we have merged the two independent countries of Saint Martin (MAF) and Sint Maarten (SXM) into a single country definition ```Country.MAF=Saint Martin```. These are the two parts of an island of 34 sq mi in the Caribbean Sea, about equally divided into a French and a Dutch side. Clearly, it doesn’t seem to make sense to show them separately in the UI.

With that out of the way, let’s see some other examples:
* ```Country.GBR=UK  - mid/south```: for UK Great Britain without Scotland
* ```Country.Sco=UK - Scotland```: for Scotland
* ```Country.IRL=Ireland / N. Ireland```: for the entire island, not just the Republic of Ireland
* ```Country.IRN=Iran```: official prefix “Islamic Republic of” omitted
* (```Country.VAT=Holy See```: removed because really Vatican City is part of Rome, Italy)
* ...and several more.

As written above, you are free to completely change any of these definitions to your liking.

However, for uploading your own mod definition to the online central repository, contributions are restricted to these canonical definitions.

[Again, you can find **all predefined countries** here (see the ```Country.XYZ``` entries)](https://github.com/captn-nick/MSFS-Mod-Selector/blob/master/src/main/resources/default_labels.properties)

### Concerning continents: Another explicitly non-political statement
Similar to how we do with countries, we came up with our own definition of what are the available continents and what countries are part of them. They are of course based on actual geography, but also take aspects of Flight Simulator-oriented geography and usability into account.

![Canonical continent definitions](/documentation/Continents.png)

* **Europe, Asia, Africa, and South America** generally correspond to their usual definition.
* The **US and Canada** are their own continent simply because for the US, we want to group mods on two levels by state and city which seems to make sense for such a big and “moddable” country. For simplicity reasons, we added Canada as its own country to that “pseudo-continent” as well (otherwise it would stand alone in a separate “North America” continent).
* **Middle America** contains the Central America mainland countries as well as all Caribbean island countries, except for the few very close to the coast of South America.
* **Oceania** not only contains Australia and the actual Pacific Oceania region, but also very remote islands across all three oceans.
* **Arctica / Antarctica** contains the land around the Earth’s poles.

The following are general rules of thumb:
* **Island countries** belong to nearby continents; remote island countries belong to the generic Oceania continent. Some notable examples of links between countries and their respective continent:
  * All Caribbean countries belong to Middle America, but Aruba, Curaçao, and Trinidad and Tobago belong to South America.
  * Svalbard and Jan Mayen belong to Arctica / Antarctica, but Iceland belongs to Europe.
  * Mauritius belongs to Africa, but the Seychelles belong to Oceania.
  * Sri Lanka belongs to Asia, but the Maldives belong to Oceania.
  * The Solomon Islands belong to Asia, but Vanuatu and Nauru belong to Oceania.
* **Dependent countries / oversea departments** belong to their respective mother country and thus to their continent.

As written above, you are free to completely change country-continent links to your liking.

However, for uploading your own mod definition to the online central repository, contributions are restricted to these canonical definitions.

[Find the complete map of the world, divided into the canonical continents, here. (Note: This map ignores the “dependent country rule”.](https://github.com/captn-nick/MSFS-Mod-Selector/blob/master/documentation/Continents.png)

[Find the complete list of all canonical country-to-continents links here (see the ```Country.XYZ.Continent``` entries).](https://github.com/captn-nick/MSFS-Mod-Selector/blob/master/src/main/resources/default_labels.properties)

## Adding a mod
In order to add a mod under Mod Selector management, you must:
* place the mod folder in the Community or Temp directory
* add an entry to the mods.txt file, or use Mod Selector’s auto-registry function through the online mod database.

### Restrictions
Note the following restrictions. See also "Manually adding a mod" below for more details. When violated, they cause a warning / error, see further down.
* Mod name must not be empty.
* Mod names must be unique across all mods.txt entries.
* Mod type must be one of the supported mod type.
* Continent must be one of the supported continents or empty.
* Cities must be uniquely linked to a country and a continent across all mods.txt entries, i.e. a city must not be linked to one country/continent in one entry, and to another in another entry.
* Countries must be uniquely linked to a continent across all mods.txt entries, i.e. a country must not be linked to one continent in one entry, and to another in another entry.

### Using Mod Selector’s online mod database
![Adding a mod](/documentation/screenshot2.png)

Mods can be auto-registered by Mod Selector if found in the central mods DB.

First, start up Mod Selector. If it finds a mod that is not registered in mods.txt yet, the “Error.030” window will pop up.

Mod Selector will try to find registry information about all unknown mods in your central mods DB which contains information about all MSFS mods.

If it can’t find information about a particular mod, try updating your mods DB by pressing the “Update…” button. Note that Mod Selector does not directly search for information online, but rather, updates its mods DB with information from the internet and then uses it as a local source. That’s why it’s important to update your local mods DB regularly. Think of it as your virus scanner’s virus definition database.

For all mods found in the DB, Mod Selector will show the full information found for each mod. You can then:
* Modify the information for any line
* Delete any line
* Leave any line as is

and press the “Auto-add” button to add registration information for these (modified) lines to mods.txt.

For mods unknown to the mods DB, only the mod line will be shown. You have to add these mods manually to mods.txt.

### Contributing to Mod Selector’s online mod database
If you decided to contribute your own mod definitions back to the central repository, first of all, thank you!

Mod Selector uses a simple Git integration interface to allow you to download / upload mod definitions. Git is a tool used by software developers to work collaboratively on text / source code files. It is also used as the underlying mechanism of GitHub, the platform which hosts the Mod Selector source code. [Learn more about Git here](https://git-scm.com/). This chapter assumes you are familiar with Git. If you do not want to use Git to upload your mod definitions to the central repository, simply skip to the next chapter.

In order to integrate Mode Selector with Git to use it to download / upload mod definitions, you must:
* Install an external Git client.
* Checkout Mod Selecor’s mod-repository subfolder (in the Packages folder) as a copy of the Git repository https://github.com/captn-nick/MSFS-Mod-Repository.
* In your config.properties file, change the following property: ModsDb.updateWithGit=true.
This latter property change will make sure that when clicking “Update mods DB”, rather than simply HTTP-downloading the latest mods-db.txt file content, Mod Selector will actually make a Git pull in its folder instead. This is done so to make sure both download and upload are managed by Git to prevent version conflicts.
If you are in “Git mode”:
* To download the newest mod definitions, click on the “Update mods DB” button. This simply does a Git pull. The concole output will be presented in a separate Window. If the pull command failed, please use your local Git client to solve the problem.
* To upload your own mod definitions, click on “Contribute to mods DB” in the main window. This will add all your own mod-definitions to mods-db.txt, check them for consistency, and sort them. Note that for adding mods to the central repository, additional restrictions on mod meta-information are in place. [Read more about these restrictions here.](https://github.com/captn-nick/MSFS-Mod-Repository#restrictions-for-contributions) You then have to use your local Git client to actuall upload (push) that file to the remote repository (and file a pull request).

### Manually adding a mod
A mods.txt file entry consists of a

<code>Type&#9;Continent&emsp;Country&emsp;Name&emsp;CityOrIcaoOrAircrafttype ## Description&emsp;Author&emsp;Website</code>

entry whereas
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
  * ```Aircrafttype```: abbreviation of one of the supported aircraft types. Available for livreries and aircraft models only. Omit this and the preceding tab if not used. Supported aircraft types are:
    * ```PR```: Propeller plane
    * ```TP```: Turboprop
    * ```JT```: Jet
    * ```AL```: Airliner
* ```##``` (two hash signs, optionally surrounded by space): a separator. Separates off the following section of optional information. Omit if none of the following information is given.
* ```Description```: a short description or “title” of the mod, e.g. the mod title as found on the download website.
* ```Author```: the mod author’s name.
* ```Website```: the website the mod can be downloaded from. Only https://flightsim.to sub-pages are supported.

**Note: All elements are _separated by tabs spaces_.** (Note 2: when copying examples from this README into a notepad editor, you may get weird characters. Make sure to replace them with actual tab spaces! - or copy the examples of an actual example .txt file, see below.)

Examples (note: this project is not affiliated with these mods in any way):
* <code>AP	EU	GBR	addparking_eglm	EGLM	##	EGLM White Waltham Airfield	frododo	https://flightsim.to/file/1058/white-waltham-airfield</code> for https://flightsim.to/file/1058/white-waltham-airfield, an airport in the UK, Europe.
* <code>LM	EU	NLD	amsterdamtower	Amsterdam	##	Amsterdam Tower + Centraal Station	master414	https://flightsim.to/file/318/amsterdam-tower-centraal-station</code> for https://flightsim.to/file/318/amsterdam-tower-centraal-station, the Centraal Station landmark in Amsterdam, Netherlands, Europe.
* <code>LS	US	WY	devilstower-wyoming	##	Devil's Tower	VFXSimmer	https://flightsim.to/file/186/devil-s-tower</code> for https://flightsim.to/file/186/devil-s-tower, the Devil's Tower, a landscape feature in the US (here, as an example, I omitted the "country" part).

[You can find **a complete example mods.txt file** here](https://github.com/captn-nick/MSFS-Mod-Selector/blob/master/external-test-resources/mods.txt).

[You can find **a mods.txt file with 500+ flightsim.to mods** here](https://github.com/captn-nick/MSFS-Mod-Repository/blob/master/mods-db.txt). (This is the database used by Mod Selector for mod registry information lookup.)

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
* **Error.110**: Duplicate mod definition found: Mod Selector found two entries with the name mod name in mods.txt. Remove one of the duplicates.
* **Error.111**: Inconsistent mod information found: Mod Selector found two entries in mods.txt which contradict each other. This can be e.g. because the two use the same city, but belong to a different country / continent. Correct the entries in question. Note that city names must be unique among all countries and continents.

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
* First startup initializing may take some time.
* No auto-discovery / assistance to add mods. All mods have to be added manually in the mods.txt file.
* Mod Selector will not remember the last selection but always start up in the default selection.
* Only limited possibilities to re-arrange geographical areas on the UI.
* Doesn’t help identifying individual mods. Mods are simply kept in their (oftentimes chaotically named) directory structures.
* (Development: unclean build process.)
* And probably a few more improvements which would be “nice to have”…

## Version history
* 0.8:
  * added mods.txt to mods-db.txt (repository) export functionality, including more restrictive mod meta-information rules;
  * added optional Git integration for mods-db.txt update;
  * merged continents “USA” and “North America” into new continent “USA / Canada”;
  * added new continent “Middle America”;
  * removed / merged countries ALA, IMN;
  * more precise error feedback for missing/illegal mod data;
  * fixed feedback for mods DB parsing errors;
  * fixed positioning, modal behavior, and default button selection of all UI windows;
  * fixed / restricted “Update mod DB” button visibility.
* 0.7:
  * added a mod meta-information database which can be updated from the internet;
  * for newly-added mods, meta-information is auto-collected from the database and presented to the user with the option to automatically add mods information to mods.txt;
  * support for 3-5-character ICAO codes / French “Altisurface” style ICAO codes.
* 0.6:
  * config split into I18N labels (typically interchangeable between users) and local config (typically specific to a user’s setup and preferences);
  * added pre-defined names/labels for all countries and US states;
  * automated country / city consistency check;
  * added error recognition for duplicate mods and illegal continents;
  * added line number information to all mods.txt line parsing errors;
  * general UI error handling improved.
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
* 0.7 -> 0.8:
  * Make sure to add the ```Continent.UC.Countries``` and ```Continent.UC=U.S.A. / Canada``` entries to your config.properties file.
    * Note that the US states and Canada have been merged into the new Continent.UC= U.S.A. / Canada continent. In this continent, individual US states are treated as separate countries, and Canada is a country of that continent as well.
  * Make sure to add the ```Continent.MA.Countries``` and ```Continent.MA=Middle America``` entries to your config.properties file.:
    * Note that “Middle America” countries now belong to the new Continent.MA=Middle America continent. If you want to use the official definition (recommended), this most prominently includes ```Country.MEX``` (Mexico).
  * Remove the ```Continent.NA.Countries``` and ```Continent.NA``` as well as the ```Continent.US.Countries``` and ```Continent.US``` entries from your config.properties file.
* 0.6 -> 0.7:
  * For livrery mods, change the mod type id ```ModType.LI``` to ```ModType.AL``` for your mods.txt entries.
* 0.5 -> 0.6:
  * Move your ```ModType.XY```, ```AircraftType```, ```AircraftType.XY```, ```Continent.XY```, and ```Country.XY``` definitions from config.properties into labels.properties or simply remove them to use the implicit pre-defined labels (recommended).
  * Make sure that your ```Country.XY``` definitions in labels.properties as well as your country references in mods.txt use the new ```Country.XYZ``` 3-letter abbreviation of country codes if you want to use the official definition (recommended). ```Country.XY``` 2-letter abbreviations are now designated for US states only.
  * Note that moreover several former default “XY” definitions have changed. Most notably: ```Continent.OC``` (formerly Continent.OZ), ```Country.AK``` (formerly ```Country.AL```), ```Country.DE``` (used to abbreviate “Germany”, now abbreviates “Delaware”).
  * Note that a mods.txt entry with a duplicate mod name or an illegal continent will now explicitly raise an error.
  * Note that moreover if in mods.txt there is a conflict between linking a city/country to a continent or a city to a country, an error will be raised.
* 0.4 -> 0.5:
  * Note that a mods.txt entry with an illegal / empty mod name or mod type will now explicitly raise an error.
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
This program is also available for download at https://flightsim.to/file/1124/mod-selector.

## Happy Flying!

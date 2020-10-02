# MSFS-Mod-Selector

<img align="right" src="documentation/screenshot.png"/>

The MSFS mod selector solves a common problem in **Microsoft Flight Simulator 2020 (MSFS)**: even though only a few weeks after release, there are hundreds of high-quality **community-created mods** to enhance MSFS experience even further, installing a large number of them considerably slows down MSFS startup time to an unbearable point.

A simple manual solution is to manually copy only the mods you want to use at one time before starting the simulator.

The Mod Selector assists you in this by **automating the selection process**: it allows you to easily group mods by type and (for world enhancements which would be most mods) by continent, country, and even by city.

Here’s how this works:
* You must register each mod once with the Mod Selector.
* Then, start up the Mod Selector before starting MSFS.
* Simply select all the mod categories you want to activate.
* The Mod Selector will automatically copy the relevant mods into your Community folder / move away all unwanted mods into a temp folder.
* Then, start MSFS and enjoy a significantly decreased startup time.

## Usage warning
This program is very early beta. USE AT YOUR OWN RISK!
It is highly recommended that you create a backup of your mods (MSFS Community folder) before using this program.

## Prerequisites
Mod Selector requires Java to run.

## Download
Download the [latest version ZIP file](https://github.com/captn-nick/MSFS-Mod-Selector/releases/download/0.1/MsfsModManager.0.1.zip) from the [Releases page](https://github.com/captn-nick/MSFS-Mod-Selector/releases).
No installation is requires. Simply unzip the content of the ZIP file into the MSFS “Package” folder.
Components
The program consists of 3 components:
* **MsfsModSelector.java**: the program file.
* **mods.txt**: a text file containing information about all the mods managed by the Mod selector.
* **config.properties**: a text file containing all configurations for the Mod selector.

## Setup
By default, no setup is required.

Start the Mod Selector by double-clicking the program file or via the command line, see below.

If you placed the Mod Selector into your MSFS “Package” folder, the Mod Selector will recognize the sub-folder “Community” as the directory to place all active mods into, and the sub-folder “Temp” as the directory to temporarily store deactivated mods. You need to create the “Temp” sub-folder if it doesn’t exist already.

Alternatively, you can config those paths manually, see below.

Afterwards, the Mod Selector will start.

## Config
The config.properties files contains the Mod Selector configuration. A single config consists of a

```key=value```

entry.

The following keys are of note:
* ```path.community```: the full path of the Community mod directory
* ```path.temp```: the full path of the temporary mod directory. Disabled mods will be stored here.
* ```Continent.XY.Countries```: defines which countries to show in the UI for individual selection and in which order for continent XY.
* ```Continent.XY```: defines the name of continent XY as it is displayed in the UI.
* ```Country.XY```: defines the name of country XY as it is displayed in the UI.

Note:
* For the paths, backslashes have to be doubled (```\\``` instead of ```\```).
* You are free to define your own countries. As long as at least one mod is designated to this country (and defines a continent as well), Mod Selector will be able to link this country to its continent, i.e. a country does not have to be listed explicitly in any Continent.XY.Countries entry.
* You are free to define your own cities simply by linking them to any mod (see below). For cities, no config is required. All cities explicitly linked to any mod will show up in the UI in alphabetical order.
* The US are their own continent simply because that way we can group mods on two levels by state and city which seems to make sense.

## Adding a mod
In order to add a mod under Mod Selector management, you must:
* place the mod folder in the Community or Temp directory
* add an entry to the mods.txt file
A mods.txt file entry consists of a
type	continent	country	name	city
entry whereas
* type: abbreviation of one of the supported types. Supported types are:
  * ```AP```: airport
  * ```LM```: individual landmarks
  * ```CT```: entire cities
  * ```LS```: individually modeled big landscape features, e.g. mountains (typically contain big texture files)
  * ```LF```: general landscape fixes, e.g. river elevation fixes (typically don’t contain any texture files)
  * ```LI```: airplane livreries
* continent: abbreviation of one of the supported continents. Leave blank for livrery mods.
* country: abbreviation of a country. Leave blank for livrery mods
* name: The exact folder name of the mod in question.
* city: The actual name (no abbreviation!) of the city. Omit this and the preceding tab if not used.
These 5 elements are separated by tabs.

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
You have to start Mod Selector from the command line to see warning / error messages however. Use the following command:
```java –jar MsfsModSelector.jar```

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
* Default selection for mod types cannot be changed.
* Only limited possibilities to re-arrange geographical areas on the UI.
* Doesn’t help identifying individual mods. Mods are simply kept in their (oftentimes chaotically named) directory structures.
* Only very rudimental error handling and feedback.
* (Development: unclean build process.)
* And probably a few more improvements which would be “nice to have”…

## For Developers
This project was built using the NetBeans IDE. JFrames and JPanels are created and edited with the IDE’s Swing editor.

## Happy Flying!

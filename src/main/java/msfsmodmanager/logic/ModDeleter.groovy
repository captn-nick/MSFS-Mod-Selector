package msfsmodmanager.logic

import groovy.transform.CompileStatic

import msfsmodmanager.state.FileSystem

@CompileStatic
class ModDeleter {
    public static void deleteInTempDirectory(List<String> modNames) {
        modNames.each { String name ->
            new File(FileSystem.TEMP_DIR + $/\/$ + name).deleteDir()
        }
    }
}

